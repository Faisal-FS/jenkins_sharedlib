#!groovy

// Get all contents of Alfred enabled list
def alfred_list = readFileFromWorkspace('alfred_enabled.list')
@Grab(group='org.apache.commons', module='commons-io', version='1.3.2')

def filer = readFileFromWorkspace('config.cfg')
def config = new ConfigSlurper().parse(filer)
print config.sourceurl

String[] split_file = alfred_list.split(System.getProperty("line.separator"));
def alfredRepo = "ssh://git@code.xgrid.co:29418/source/alfred.git"
def branch_map = [:]

// Creating a map for repos against branches
for (def line:split_file)
{
  String[] line_split = line.split(" ")
  repo = line_split.getAt(0)
  branch = line_split.getAt(1)
  branch_map[repo] = branch
}

// Iterating over each repo inside the map
for ( project in branch_map.keySet() )
{
    // Creating a freestylejob that acts as a seedjob for each repo
    freeStyleJob("${project}-seedjob")
    {
        // Restrict pipeline to run on master only
        label('master')

        // Injecting Environment variables to be used by Alfred
        environmentVariables
        {
            env('PROJECT', project)
            env('PROJECTURL', "ssh://git@code.xgrid.co:29418/source/${project}.git")
            env('BRANCH', branch_map[project])
        }

        // Pre build workspace cleanup
        wrappers
        {
            preBuildCleanup()
        }

        // Adding Alfred repo as default SCM for seedjob
        scm
        {
            git
            {
                remote
                {
                    credentials('jenkins')
                    url(alfredRepo)
                }
                branch ('master')
            }
        }

        // Groovy script to be called inside the seedjob
        steps
        {
            dsl
            {
                external('seed/template_seedjob.groovy')
            }
        }
    }
}

