impala-java-client
==================

Java client to connect directly to Impala using thrift (similar to the impala-shell is doing in python) and does not depend on the HiveServer2 

The Java client is build using trift interface.

The build script will download the necessary dependencies, generate the thrift java code and compile into a jar.
The test show hows to use the impala java client  

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


TODO: Use maven so that the project can easily be imported in clojure / clojar.



