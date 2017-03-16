package utils;

import java.nio.charset.StandardCharsets

@NonCPS def count(def args)
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


@NonCPS def Stages(def args)
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
   echo ("$number")
   echo ("$value")
   
   stage("$number")
   {
    echo "hello"  
   }
   
   return 0;
    
    //String s1 = ""
    //for (def n in list) {
      //  echo ("$n")
       // def (stage_name, command) = n.split('=')
       
       //s1 = "stage ('testing') { echo \"test\" } "
       
       //return s1
      //   stage("$stage_name")
       //  {
        //  echo ("$command")
        // }
    //}
    
}
return this;
