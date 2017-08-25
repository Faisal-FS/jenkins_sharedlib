package utils;

import java.nio.charset.StandardCharsets
@Grab(group='org.apache.commons', module='commons-io', version='1.3.2')

//************************************************************************
// Function   : countStages
// Purpose    : count the number of stages present inside the jenkinsfile
// Usage      : countStages(jenkinfile config)
// Parameters : contents of jenkinsfile
// Return     : number of stages
// ***********************************************************************
@NonCPS
def countStages(def args)
{
   def count = 0
   def li = stageList(args)
   count = li.size()
   return count;
}

//************************************************************************
// Function   : stageList
// Purpose    : convert jenkinsfile contents into a list
// Usage      : stageList(jenkinfile config)
// Parameters : contents of jenkinsfile
// Return     : list of jenkinsfile data
// ***********************************************************************
@NonCPS
def stageList(def args)
{
   def list = []
   for(emp in args) {
      if (emp.toString().contains('stage'))
        {
          list.add("$emp")
        }
    }
   return list;
}

//************************************************************************
// Function   : prepareStages
// Purpose    : calls a function that creates stages using a list
// Usage      : prepareStages(number,config)
// Parameters : stage number, and jenkinsfile data
// Return     : None
// ***********************************************************************
@NonCPS
def prepareStages(def iter, def args)
{
   def li = stageList(args)
   def cmd = li[iter]
   def (fullstage, command) = cmd.split('=')
   def (key, stage_name) = fullstage.split('_')
   createStages(stage_name,command)
}

//************************************************************************
// Function   : createStages
// Purpose    : creating stages dynamically and excuting the shell command
// Usage      : createStages(stage_name,command)
// Parameters : name of the stage, command to run in stage
// Return     : None
// ***********************************************************************
def createStages(def stage_name, def command)
{
  stage ("$stage_name")
  {
    sh "$command"
  }
}

//************************************************************************
// Function   : ticketGeneration
// Purpose    : Parses config file and triggers ticketing stage for Alfred
// Usage      : ticketGeneration()
// Parameters : contents of jenkinsfile
// Return     : None
// ***********************************************************************
@NonCPS
def ticketGeneration(def args)
{
  def cfg_file = libraryResource 'alfred.ini'
  def config = new ConfigSlurper().parse(cfg_file)
  def bugzilla_server_ip = config.bugzilla_ip
  def bugzilla_api_key = config.api_key
  ticketingStage(bugzilla_server_ip,bugzilla_api_key,args)
}

//************************************************************************
// Function   : ticketingStage
// Purpose    : Create ticket generation stage and trigger bugzilla script
// Usage      : ticketingStage()
// Parameters : bugzilla ip, api key and contents of config file
// Return     : None
// ***********************************************************************
def ticketingStage(def bugzilla_server_ip, def bugzilla_api_key, def args)
{
  def logWorkspace = "${WORKSPACE}/${args.artifacts}"
  def rts = "ssh://git@172.19.0.77:29418/source/rts.git"
  stage ('TicketGeneration')
  {
    dir('rts')
    {
      git credentialsId: 'jenkins', url: rts
      echo "Ticket generation underway for current build"
      sh "python scripts/bugzilla/ticketing.py --bugzilla_ip ${bugzilla_server_ip} --path ${logWorkspace} --api_key ${bugzilla_api_key}"
    }
  }
}

//************************************************************************
// Function   : archive_artifacts
// Purpose    : archives artifacts on to Alfred master
// Usage      : archive_artifacts(dir)
// Parameters : dir for artifacts specified in config file
// Return     : None
// ***********************************************************************
def archive_artifacts(def dir)
{
   // Artifacts to be archived for Alfred Master
   archiveArtifacts allowEmptyArchive: true, artifacts: dir
}

//************************************************************************
// Function   : postPipeline
// Purpose    : Pipeline steps required after pipeline run ends
// Usage      : postPipeline(args, builStatus)
// Parameters : configfile data and pipeline build status
// Return     : None
// ***********************************************************************
def postPipeline(def args, String buildStatus)
{
  postPipelineScript(args.post_pipeline_script)
  alfredInfo(args)
  archive_artifacts(args.artifacts)
  emailAlert(buildStatus, args.email)
}

//************************************************************************
// Function   : postPipelineScript
// Purpose    : Execute pipeline script which runs after every build
// Usage      : postPipelineScript(scriptName)
// Parameters : name of the script to be executed as part of post pipeline
// Return     : None
// ***********************************************************************
def postPipelineScript(def scriptName)
{
  if(scriptName)
  {
    stage ("PostPipelineScript")
    {
      sh "$scriptName"
    }
  }
  else
  {
    echo "No post pipeline script specified"
  }
}

//************************************************************************
// Function   : alfredInfo
// Purpose    : Generate alfred info file
// Usage      : alfredInfo(args)
// Parameters : configfile data
// Return     : None
// ***********************************************************************
def alfredInfo(def args)
{
  dir (args.artifacts)
  {
    def contents = "machine_arch = $args.machine_arch \n" +
                   "build_url = ${BUILD_URL} \n" +
                   "pipeline_type = $args.pipeline_type \n" +
                   "workload = $args.workload"

     writeFile file: 'alfred.info', text: "$contents"
  }
}

//************************************************************************
// Function   : triggerDownstream
// Purpose    : Trigger downstream job associated with the pipeline
// Usage      : triggerDownstream(args)
// Parameters : name of downstream pipeline to be triggered
// Return     : None
// ***********************************************************************
def triggerDownstream(def jobname)
{
  echo "Trigering Dowstream job: ${jobname}"
  build job: "${jobname}", wait: false
}

//************************************************************************
// Function   : emailAlert
// Purpose    : Email build status to pipeline owners
// Usage      : emailAlert(build_result, owners)
// Parameters : Pipeline build status and list of pipeline owners
// Return     : None
// ***********************************************************************
def emailAlert(String build_result,owners)
{
  if (owners)
  {
    def subject = "[Alfred] $JOB_NAME (# $BUILD_NUMBER)  - $build_result!"
    def body = "Hi Team,\n\n" +
                "Pipeline: ${JOB_NAME}\n" +
                "\n" +
                "Build number: ${BUILD_NUMBER}\n" +
                "\n" +
                "Build status: ${build_result}\n" +
                "\n" +
                "Console output: ${BUILD_URL}\n" +
                "\n" +
                "--\n" +
                "Alfred"

    mail body: "$body", subject: "$subject", to: "$owners"
  }
  else
  {
    echo "No email Recipents Sepecified!"
  }
}
return this;
;
