package utils;

import java.nio.charset.StandardCharsets
// Importing external jar file with the help of grapes
@Grab(group='org.yaml', module='snakeyaml', version='1.17')
import org.yaml.snakeyaml.*

def sample_return(String statement)
{
    echo "$statement"
}

return this;
