#!groovy

// Importing Config Slurper using grapes
@Grab(group='org.apache.commons', module='commons-io', version='1.3.2')

def sout = new StringBuilder(), serr= new StringBuilder()

// Clone Project, according to Gerrit Trigger
def projectRoot = WORKSPACE + "/$PROJECT/"
//println projectRoot
def clone = "git clone $PROJECTURL".execute(null, new File(WORKSPACE + "/"))
clone.consumeProcessOutput(sout, serr)
clone.waitFor()

println "out> $sout err> $serr"

folder("$PROJECT") {
    displayName("$PROJECT")
    description("pipeplines for $PROJECT")
}

