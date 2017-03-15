#!groovy

def call(body) {

  def args = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = args
  body()

  // Loading jenkins jenkinsLibrary
  def lib = new utils.JenkinsLibrary()
  
  node('gcc_slaves')
  {
    stage ('stage 1')
    {
      sh "ls"
    }
    
  }
}
