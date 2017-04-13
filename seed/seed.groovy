#!groovy

// Clone Project, according to Gerrit Trigger
//def repoUrl = "$GERRIT_SCHEME://$GERRIT_HOST:$GERRIT_PORT/$GERRIT_PROJECT"
//def projectRoot = WORKSPACE

// Reading through the ci_enablied.list
def alfred_list = readFileFromWorkspace('alfred_enabled.list')
String[] split_file = alfred_list.split(System.getProperty("line.separator"));

for (def line:split_file)
{
  String[] line_split = line.split(" ")
  repourl = line_split.getAt(1)
  repo = line_split.getAt(0)
  println repourl
  println repo
}
