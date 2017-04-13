#!groovy

// Importing Config Slurper using grapes
@Grab(group='org.apache.commons', module='commons-io', version='1.3.2')

def sout = new StringBuilder(), serr= new StringBuilder()

// Clone Project, according to Gerrit Trigger
def projectRoot = WORKSPACE + "/$PROJECT/"
println projectRoot
def clone = "git clone $PROJECTURL".execute(null, new File(WORKSPACE + "/"))
clone.consumeProcessOutput(sout, serr)
clone.waitFor()

folder("$PROJECT") {
    displayName("$PROJECT")
    description("pipeplines for $PROJECT")
}

new File("$projectRoot/jenkins/pipelines").eachFile() { file->
  println "Jenkins File Text:"
  println file.text

  def config = new ConfigSlurper().parse(file.text)

  println(config.job.name)

 pipelineJob("$config.job.name") {
 logRotator(30,-1,-1,-1)
    if( config.job.pipeline_type == "periodic" )
    {
        definition
        {
            cpsScm
            {
              label(config.job.label)
                scm {
                    git {
                      branch('master')
                      remote
                      {
                         credentials('jenkins')
                         url(repoUrl)
                      }

                      }
                    }
                    // Adding Jenkinsfile script path to be used by the new job
                scriptPath("jenkins/pipelines/" + org.apache.commons.io.FilenameUtils.getBaseName(file.name))
            }
        }
        triggers
        {
            cron(config.job.schedule)
        }

        publishers{
          wsCleanup()
          archiveArtifacts {
            pattern(config.job.artifacts)
          }
      }
    }
}
}
