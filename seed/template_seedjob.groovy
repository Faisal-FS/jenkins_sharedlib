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
        // Common pipeline code for all type of pipelines
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
        // Code chuck for periodic pipelines
        if( config.job.pipeline_type == "periodic" )
        {
            // Defining pipeline triggers
            triggers
            {
                cron(config.job.schedule)
            }

        }
        // Code chuck for ondemand pipelines
        if(config.job.pipeline_type == "on-demand")
        {
            // Parsing Ondemand variables
            def ondemand_string = config.job.ondemand_variables
            def ondemand_array = ondemand_string.split(';')
            parameters
            {
                // Iterating over ondemand variables to be used
                for (String value:ondemand_array)
                {
                    def ondemand_des = ""

                    // Ensuring ondemand variable is trimmed properly and split on first occurrence only
                    def ondemand_var = value.trim().split('=',2)

                    // Adding ondemand variable description
                    if(ondemand_var[1].contains("("))
                    {

                        //Extracting ondemand variable description from the ondemand variable value
                        ondemand_des = ondemand_var[1].substring(ondemand_var[1].indexOf('(')+1,ondemand_var[1].indexOf(')'))

                        //Extracting only ondemand variable value without description
                        ondemand_var[1] = ondemand_var[1].substring(0,ondemand_var[1].indexOf('(')).trim()
                    }

                    // Creating ondemand parameter list for Alfred with default values
                    stringParam(ondemand_var[0],ondemand_var[1],ondemand_des)
                }
            }
        }
        // Post Build Cleanup
        publishers
        {
            wsCleanup()
        }
    }
}
