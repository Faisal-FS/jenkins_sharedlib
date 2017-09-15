#!groovy

// Groovy Closures to read config files present in jenkinsfiles inside closures
def call(body) {

  def args = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = args
  body()

  // Loading jenkinsLibrary
  def lib = new utils.JenkinsLibrary()
  def skippedRepos = ['carmos','ats']

  // Default value 2 days
  def stuckPipelineTimeout = 2
  if (args.stuckPipelineTimeout)
  {
    stuckPipelineTimeout=args.stuckPipelineTimeout.toInteger()
  }

  // Default timeout value is 24 hours
  def timeValue = 24
  if (args.timeout)
  {
    timeValue=args.timeout.toInteger()
  }

  // Repo to clone for Alfred
  def repourl = "ssh://git@172.19.0.77:29418/source/${args.clone_repos}.git"

  // Enforce timeout for pipelines that are stuck waiting for executors
  timeout(time: stuckPipelineTimeout, unit: 'DAYS')
  {
    // Specifies the label which executes commands enclosed inside
    node(args.label)
    {
      // Enforcing pipeline timeout
      timeout (time: timeValue, unit: 'HOURS')
      {
        // Cleanup workspace at the start of the job
        step([$class: 'WsCleanup'])

        // Clone stage to fetch latest repository to be consumed by the pipeline
        stage ('Clone')
        {
            git credentialsId: 'jenkins', url: repourl
        }

        // Count of total stages found in jenkinsfile
        def total_stages = lib.countStages(args)

        // Dynamically creating stages
        for(int iter = 0; iter<total_stages; iter++)
        {
          try
          {
            lib.prepareStages(iter,args)
          }
          catch (err)
          {
            lib.postPipeline(args,"FAILED")
            if(skippedRepos.contains(args.clone_repos) == false)
            {
              lib.ticketGeneration(args)
            }
            error("Pipeline failed! Exiting ......")
          }
        }
        if (args.downstream)
        {
          lib.triggerDownstream(args.downstream)
        }

        // Post pipeline steps required for Alfred
        lib.postPipeline(args,"PASSED")
      }
    }
  }
}
