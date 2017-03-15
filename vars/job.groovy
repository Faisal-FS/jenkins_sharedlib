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
    stage ('stage 1')
    {
      sh "mkdir -p testing"
      sh "touch testing/somth"
      sh "ls"
    }
    archiveArtifacts allowEmptyArchive: true, artifacts:'testing/*'
  }
}
