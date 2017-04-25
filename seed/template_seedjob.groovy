#!groovy

@Grab(group='org.apache.commons', module='commons-io', version='1.3.2')
def sout = new StringBuilder(), serr= new StringBuilder()

print "hello there muneeb"

def projectRoot = WORKSPACE + "/$PROJECT/"
def clone = "git clone $PROJECTURL".execute(null, new File(WORKSPACE + "/"))
clone.consumeProcessOutput(sout, serr)
clone.waitFor()
println "out> $sout err> $serr"

folder("$PROJECT")
{
    displayName("$PROJECT")
    description("pipeplines for $PROJECT")
}

new File("$projectRoot/alfred/pipelines").eachFile()
{   file->
    println "Pipeline config:"
    println file.text
    def config = new ConfigSlurper().parse(file.text)
    

    // Creating workload based folders inside the repo folder
    folder("$PROJECT/$config.job.workload")
    {
        displayName("$config.job.workload")
        description("Pipeplines for $config.job.workload")
    }

    // Creating Pipeline
    pipelineJob("$PROJECT/$config.job.workload/$config.job.name")
    {
        logRotator(30,-1,-1,-1)
        if( config.job.pipeline_type == "periodic" )
        {
            definition
            {
                cpsScm
                {
                    label(config.job.label)
                    scm
                    {
                        git
                        {
                            branch(BRANCH)
                            remote
                            {
                                credentials('jenkins')
                                url(PROJECTURL)
                            }
                        }
                    }
                    scriptPath("alfred/pipelines/" + org.apache.commons.io.FilenameUtils.getBaseName(file.name))
                }
            }
            triggers
            {
                cron(config.job.schedule)
            }
            publishers
            {
                wsCleanup()
                archiveArtifacts
                {
                    pattern(config.job.artifacts)
                }
            }
        }
    }
}
