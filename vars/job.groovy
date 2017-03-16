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
    
    //def list = lib.countStages(args)
    
    String strin = ""
    strin = lib.create_stages(args)
    
    echo ("$strin")
    //Eval.me("$strin")
          
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
