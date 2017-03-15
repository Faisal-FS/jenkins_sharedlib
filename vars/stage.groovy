
#!groovy

def call(body) {

  def args = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = args
  body()

 node('ats_slaves')
  {
    echo "$args" 
  }
}
