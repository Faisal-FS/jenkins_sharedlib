#!groovy

// Groovy Closures to read config files present in jenkinsfiles inside closures
def call(body) {

  def args = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = args
  body()
  // Loading jenkinsLibrary
  def lib = new utils.JenkinsLibrary()

  // Repo to clone for Alfred
  def repourl = "ssh://git@172.19.0.77:29418/source/${args.clone_repos}.git"

  // Specifies the label which executes commands enclosed inside
  node(args.label)
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
        lib.stag(iter,args)
      }
      catch (Exception err) 
      {
        lib.archive_artifacts(args.artifacts)
        error("Pipeline failed")
      }
    }
   lib.archive_artifacts(args.artifacts)
  }
}
