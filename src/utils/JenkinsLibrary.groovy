package utils;

import java.nio.charset.StandardCharsets

@NonCPS
def create_stages(def args)
{   
   def list = []
    def count = 0 
    
    for(emp in args) {
      
      if (emp.toString().contains('stage'))
        {
          list.add("$emp")
          count = list.size()
          echo("$count")
        }
    }
    
    for (n in list) {
       
       def (stage_name, command) = n.split('=')
       echo ("$stage_name")
       echo ("$command")
       
       stage($stage_name)
       {
         sh '$command'  
       }
       
       
    }
    
}
return this;
