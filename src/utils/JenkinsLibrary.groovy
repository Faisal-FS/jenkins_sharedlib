package utils;

import java.nio.charset.StandardCharsets
// Importing external jar file with the help of grapes
@Grab(group='org.yaml', module='snakeyaml', version='1.17')
import org.yaml.snakeyaml.*

import groovy.json.JsonBuilder

def sample_return(String statement)
{
    echo "$statement"
}

def object_to_json(Object data)
{
    def builder = new groovy.json.JsonBuilder(data)
    echo "$builder.toString()"
}


return this;
