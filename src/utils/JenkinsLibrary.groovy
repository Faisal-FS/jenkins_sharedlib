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
   create_stages(iter,li[iter])  
}

@NonCPS 
def create_stages(def number, def valueString)
{   
   def (fullstage, command) = valueString.split('=')
   def (key, stage_name) = fullstage.split('_') 
   stage ("$stage_name")
   {
      
      //timeout(time: 5, unit: 'SECONDS') {
      //   sh returnStatus: true, script: 'echo $WORKSPACE'
      //}
      sh ("$WORKSPACE/$command")
      //echo ("$command")
      
      //sh " echo $WORKSPACE"
   } 
}
return this;
