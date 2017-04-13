#!groovy

def alfred_list = readFileFromWorkspace('alfred_enabled.list')
String[] split_file = alfred_list.split(System.getProperty("line.separator"));
def alfredRepo = "ssh://git@code.xgrid.co:29418/source/alfred.git"
def branch_map = [:]

for (def line:split_file)
{
  String[] line_split = line.split(" ")
  repo = line_split.getAt(0)
  branch = line_split.getAt(1)
  branch_map[repo] = branch
}

for ( project in branch_map.keySet() )
{
    freeStyleJob("${project}-seedjob")
    {
        label('master')
        environmentVariables
        {
            env('PROJECT', project)
            env('PROJECTURL', "ssh://git@code.xgrid.co:29418/source/${project}.git")
            env('BRANCH', branch_map[project])
        }
        wrappers
        {
            preBuildCleanup()
        }
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
        steps
        {
            dsl
            {
                external('groovy_scripts/template_seedjob.groovy')
            }
        }
    }
}
