#!groovy

def call(body) {

  def args = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = args
  body()

  // Loading jenkins jenkinsLibrary
  def lib = new utils.JenkinsLibrary()
  
  node(args.label)
  {
    step([$class: 'WsCleanup'])
    
    stage ('Clone')
    {
        git credentialsId: 'jenkins', url: 'ssh://git@code.xgrid.co:29418/source/ats.git'
    }
    
    def value = lib.countStages(args)
    
    for(int iter = 0; iter<value; iter++)
    {
      lib.stag(iter,args)
    }
    
    archiveArtifacts allowEmptyArchive: true, artifacts:'testing/*'
  }
}
