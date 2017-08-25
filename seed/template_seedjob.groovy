#!groovy
  
 +// Load default configurations
 +@Grab(group='org.apache.commons', module='commons-io', version='1.3.2')
 +def cfg_file = readFileFromWorkspace('config.ini')
 +def config = new ConfigSlurper().parse(cfg_file)
 +
  // Get all contents of Alfred enabled list
  def alfred_list = readFileFromWorkspace('alfred_enabled.list')
  String[] split_file = alfred_list.split(System.getProperty("line.separator"));
 -def alfredRepo = "ssh://git@code.xgrid.co:29418/source/alfred.git"
 +def alfredRepo = config.git_url + "alfred.git"
  def branch_map = [:]
  
  // Creating a map for repos against branches
 @@ -28,7 +33,7 @@ for ( project in branch_map.keySet() )
          environmentVariables
          {
              env('PROJECT', project)
 -            env('PROJECTURL', "ssh://git@code.xgrid.co:29418/source/${project}.git")
 +            env('PROJECTURL', config.git_url + "${project}.git")
              env('BRANCH', branch_map[project])
          }
  
