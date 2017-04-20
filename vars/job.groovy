#!groovy

def call(body) {

  def args = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = args
  body()

  // Loading jenkins jenkinsLibrary
  def lib = new utils.JenkinsLibrary()
  def env = System.getenv()
  
  node ('master')
  {
    
    sh 'printenv'
  }
  
  withEnv(['PROJECT=GIT_REPO']) {
    
    node(args.label)
    {
      step([$class: 'WsCleanup'])

      stage ('Clone')
      {
          git credentialsId: 'jenkins', url: 'ssh://git@172.19.0.77:29418/source/ats.git'
      }

      def value = lib.countStages(args)

      for(int iter = 0; iter<value; iter++)
      {
        lib.stag(iter,args)
      }

      archiveArtifacts allowEmptyArchive: true, artifacts: args.artifacts
    }
  }
}
