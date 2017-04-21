#!groovy

def call(body) {

  def args = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = args
  body()
  
  // Loading jenkins jenkinsLibrary
  def lib = new utils.JenkinsLibrary()
  
  def repo = "ssh://git@172.19.0.77:29418/source/${args.clone_repos}.git"
  
    node(args.label)
    {
      
      echo "$args.clone_repos"
      
      step([$class: 'WsCleanup'])

      stage ('Clone')
      {
          git credentialsId: 'jenkins', url: repo
      }

      def value = lib.countStages(args)

      for(int iter = 0; iter<value; iter++)
      {
        lib.stag(iter,args)
      }

      archiveArtifacts allowEmptyArchive: true, artifacts: args.artifacts
    }
  
}
