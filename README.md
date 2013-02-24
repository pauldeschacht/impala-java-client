impala-java-client
==================

A Java client that allows to connect directly to Impala. This is similar to the impala-shell, which is using Python. It does not depend on the HiveServer2.

Using
-----
The test shows how to use the impala java client.

        //from external dependencies
        import org.apache.thrift.transport.*;
        import org.apache.thrift.protocol.*;
        //from ImpalaConnect jar
        import com.cloudera.impala.thrift.*;
        import com.cloudera.beeswax.api.*;

        try {
            //open connection
            TSocket transport = new TSocket(host,port);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            //connect to client
            ImpalaService$Client client = new ImpalaService.Client(protocol);
            client.PingImpalaService();
            //send the query            
            Query query = new Query();
            query.setQuery("SELECT * FROM <table> LIMIT 5");
            //fetch the results
            QueryHandle handle = client.query(query);
            Results results = client.fetch(handle,false,100);
            List<String> data = results.data;
            for(int i=0;i<data.size();i++) {
                System.out.println(data.get(i));
            }
        }
        catch(Exception e) {
          e.printStackTrace();
        }
        

The dependencies at runtime are 
- libthrift-0.9.0.jar
- slf4j.api-1.6.1.jar
- slf4j-simple-1.6.1.jar
- ImpalaService.jar

See the test/build.sh script for the details.

The input parameters for the test are the Impala host and port and the hive/sql statement. 

        java -cp $CLASSPATH org.ImpalaConnectTest.ImpalaConnectTest nceoricloud02 21000 "SELECT * FROM document LIMIT 5"

Building
--------
Requirements:
- thrift
- ant

If you want to build the jar yourself, the build script downloads the necessary dependencies, generates the java code (thrift) and compiles into a jar. It does the same for the test.


TODO: Use maven so that the project can easily be imported in clojure / clojar.
TODO: Build JDBC driver connecting directly to Impala



