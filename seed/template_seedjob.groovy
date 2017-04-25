#!groovy

// Including grape for config slurper
@Grab(group='org.apache.commons', module='commons-io', version='1.3.2')

// Cloning PROJECT env variable into WORKSPACE
def sout = new StringBuilder(), serr= new StringBuilder()
def projectRoot = WORKSPACE + "/$PROJECT/"
def clone = "git clone $PROJECTURL".execute(null, new File(WORKSPACE + "/"))
clone.consumeProcessOutput(sout, serr)
clone.waitFor()
println "out> $sout err> $serr"

// Creating folders for each Repo on Alfred
folder("$PROJECT")
{
    displayName("$PROJECT")
    description("Workloads for $PROJECT")
}

// Iterating over all configs in the directory
new File("$projectRoot/alfred/pipelines").eachFile()
{   file->
    println "Pipeline config:"
    println file.text

    // Using configSlurper parsing contents of config file
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
        // Defines number of days to keep logs Default: 30 days
        logRotator(30,-1,-1,-1)

        // Code chuck for periodic pipelines
        if( config.job.pipeline_type == "periodic" )
        {
            definition
            {
                cpsScm
                {
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
                    // Adding configfile path to be used by the pipeline
                    scriptPath("alfred/pipelines/" + org.apache.commons.io.FilenameUtils.getBaseName(file.name))
                }
            }

            // Defining pipeline triggers
            triggers
            {
                cron(config.job.schedule)
            }

            // Post Build Cleanup
            publishers
            {
                wsCleanup()
            }
        }
    }
}

