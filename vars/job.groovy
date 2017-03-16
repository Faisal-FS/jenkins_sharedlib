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
    
    def list = []
    for(emp in args) {
      
      if (emp.toString().contains('stage'))
        {
          list.add("$emp")
          
          echo("$list")
        }
    }
    
    stage ('Bootstrap')
    {
      echo "bootstrap stage"
    }
    
    stage ('Build-Hadoop')
    {
      echo "Build-Hadoop stage"
    }
    
    stage ('Setup-Hadoop')
    {
      echo "Setup-Hadoop stage"
    }
    
    stage ('Build-HiBench')
    {
      echo "Build-HiBench stage"
    }
    
    stage ('Setup-Hibench')
    {
      echo "Setup-Hibench stage"
    }
    
    stage ('Tests')
    {
      echo "Testing stage" 
    }
    
    archiveArtifacts allowEmptyArchive: true, artifacts:'testing/*'
  }
}
