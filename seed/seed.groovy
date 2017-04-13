#!groovy

// Reading through the ci_enablied.list
def alfred_list = readFileFromWorkspace('alfred_enabled.list')
String[] split_file = alfred_list.split(System.getProperty("line.separator"));

def branch_map = [:]
for (def line:split_file)
{
  String[] line_split = line.split(" ")
  repourl = line_split.getAt(2)
  repo = line_split.getAt(0)
  branch = line_split.getAt(1)
  branch_map[repo] = [branch]
}

println branch_map['ats']

for ( project in branch_map.keySet() ) {
    freeStyleJob("${project}-seed-job") {
       
        label("master")
        // Adding the Source code managment
        scm {
            git {
                remote {
                    name(project)
                  url("ssh://git@code.xgrid.co:29418/source/${project}.git")
                }
                branch (branch_map[project])
            }
        }

        // Adding dsl seed job
        steps {
            dsl{
                external('seed/seedjob.groovy')
                removeAction('DELETE')
            }
        }
    }
}
