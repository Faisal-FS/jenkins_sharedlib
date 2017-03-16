package utils;

import java.nio.charset.StandardCharsets

@NonCPS
def create_stages(def list)
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
        echo ("$n")
    }
    
}
return this;
