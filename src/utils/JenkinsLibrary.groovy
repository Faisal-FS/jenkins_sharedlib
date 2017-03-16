package utils;

import java.nio.charset.StandardCharsets
// Importing external jar file with the help of grapes
@Grab(group='org.yaml', module='snakeyaml', version='1.17')
import org.yaml.snakeyaml.*

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
          echo("$count")
        }
    }
   return list; 
}

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
