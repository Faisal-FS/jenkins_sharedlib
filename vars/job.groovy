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
    stage ('Cline')
    {
        git credentialsId: 'jenkins', url: 'ssh://git@code.xgrid.co:29418/source/ats.git'
        sh "ls"
    }
    archiveArtifacts allowEmptyArchive: true, artifacts:'testing/*'
  }
}
