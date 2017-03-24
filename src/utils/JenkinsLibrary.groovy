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
   def cmd = li[iter].toString()
   create_stages(iter,cmd)  
}

//@NonCPS 
def create_stages(def number, def valueString)
//def create_stages()
{   
   //def (fullstage, command) = valueString.split('=')
   //def (key, stage_name) = fullstage.split('_') 
   stage ("$number")
   {
      echo "$valueString"
      //sh "$"
     
   } 
}
return this;
