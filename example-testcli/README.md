In order to use `test-cli`, you must first build the full `caliper-java-example` project.

`cd` into the root project directory, and run:  
`mvn clean install`

This will build `example-common`, `example-testcli`, and `example-webapp`.  
The resulting jar that you'll run will be in: `/example-testcli/target/caliper-java-example.jar`. You can run it with the following command:  

    java -jar \
        ./example-testcli/target/caliper-java-example.jar \
        -h http://example.com/endpoint \
        -k {apiKey}

If successful, you'll see something like this:  
![Test CLI Screen Shot](/example-testcli/test-cli.png?raw=true)
