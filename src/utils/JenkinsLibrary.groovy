package utils;

import java.nio.charset.StandardCharsets

@NonCPS 
def countStages(def args)
{
   def count = 0  
   def li = stageList(args)
   count = li.size()
   return count;
}

@NonCPS
def stageList(def args)
{
   def list = []
   for(emp in args) {
      if (emp.toString().contains('stage'))
        {
          list.add("$emp")
        }
    }
   return list;
}

@NonCPS 
def stag(def iter, def args)
{
   def li = stageList(args)
   def cmd = li[iter]
   def (fullstage, command) = cmd.split('=')
   def (key, stage_name) = fullstage.split('_') 
   create_stages(stage_name,command)  
}

//@NonCPS 
def create_stages(def name, def command)
{   

   try
   {
      
      stage ("$name")
      {
         sh "$command"     
      }
   } catch (e)
   {
      lib.archive_artifacts(args)
      throw e  
   }
}

def archive_artifacts(def args)
{
   echo "Pipeline Ended/Failed"
   // Artifacts to be archived for Alfred Master
   archiveArtifacts allowEmptyArchive: true, artifacts: args.artifacts
}

return this;
