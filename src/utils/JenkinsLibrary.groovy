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
          //echo("$count")
        }
    }
    
    String s1 = ""
    for (def n in list) {
        echo ("$n")
        def (stage_name, command) = n.split('=')
       
       //s1 = "stage ('testing') { echo \"test\" } "
       
       //return s1
         stage("$stage_name")
         {
          echo ("$command")
         }
    }
    
}
return this;
