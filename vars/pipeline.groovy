#!groovy

def call(body) {

  def args = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = args
  body()

  // Loading jenkins jenkinsLibrary
  def lib = new utils.Jenkinslibrary()
  echo "The config file is: $args"
  lib.sample_return("this is testing")
}
