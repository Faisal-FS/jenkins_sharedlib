package utils;

import java.nio.charset.StandardCharsets

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
// Function   : archiveArtifacts
// Purpose    : archives artifacts on to Alfred master
// Usage      : archiveArtifacts(dir)
// Parameters : dir for artifacts specified in config file
// Return     : None
// ***********************************************************************
def archive_artifacts(def dir)
{
   // Artifacts to be archived for Alfred Master
   archiveArtifacts allowEmptyArchive: true, artifacts: dir
}
return this;
;
