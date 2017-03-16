package utils;

import java.nio.charset.StandardCharsets

@NonCPS 
def countStages(def args)
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


@NonCPS 
def stag(def a, def args)
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

@NonCPS 
def create_stages(def number, def value)
{   
   
   //def (stage_name, command) = value.split('=')
   //def (key,stage) = stage_name.split('_')
   //echo ("$stage")
   //echo ("$command")
   
   stage ('Bootstrap') { echo "bootstrap stage" }
   //stage ('123')
   //{
    //echo ("$command")  
   //}
    
}
return this;
