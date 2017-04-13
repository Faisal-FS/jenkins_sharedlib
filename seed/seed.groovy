#!groovy

println test
def alfred_list = readFileFromWorkspace('alfred_enabled.list')
String[] split_file = alfred_list.split(System.getProperty("line.separator"));

def branch_map = [:]
for (def line:split_file)
{
  String[] line_split = line.split(" ")
  repo = line_split.getAt(0)
  branch = line_split.getAt(1)
  branch_map[repo] = [branch]
}

for ( project in branch_map.keySet() ) {
    freeStyleJob("${project}-seedjob") {
      label('master')
      scm {
            git {
                remote {
                    name(project)
                    credentials('jenkins')
                    url("ssh://git@code.xgrid.co:29418/source/${project}.git")
                }
                branch (branch_map[project])
            }
        }
        steps {
            dsl{
                external('seed/seedjob.groovy')
            }
        }
    }
}
