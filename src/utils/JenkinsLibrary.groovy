package utils;

import java.nio.charset.StandardCharsets

@NonCPS def countStages(def args)
{
   def list = []
   def count = 0  
   for(emp in args) {
      if (emp.toString().contains('stage'))
        {
          list.add("$emp")
          count = list.size()
        }
    }
   return count;
}


@NonCPS def stages(def a, def args)
{
   def list = []
   def count = 0  
   for(emp in args) {
      if (emp.toString().contains('stage'))
        {
          list.add("$emp")
          count = list.size()
        }
    }
   create_stages(a,list[a])  
}

def create_stages(def number, def value)
{   
   echo ("$value")
   def (stage_name, command) = value.split('=')
   def (key,stage) = stage_name.split('_')
   echo ("$stage")
   echo ("$command")
   
   stage ("$stage")
   {
    echo "$command"  
   }
    
}
return this;
