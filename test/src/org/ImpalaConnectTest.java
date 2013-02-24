package org.ImpalaConnectTest;

import java.util.List;

import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.*;
import com.cloudera.impala.thrift.*;
import com.cloudera.beeswax.api.*;

public class ImpalaConnectTest
{
    private static String host="nceoricloud02";
    private static int port=21000;
    private static String stmt="SELECT * FROM document LIMIT 5";

    public static void main(String [] args) 
    {
        if (args.length < 3) {
            System.out.println("Usage: ImpalaConnectTest host port");
            return;
        }
        
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
            stmt = args[2];
                
            //open connection
            TSocket transport = new TSocket(host,port);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            //connect to client
            ImpalaService$Client client = new ImpalaService.Client(protocol);
            client.PingImpalaService();
            
            Query query = new Query();
            query.setQuery(stmt); // hive statement: SELECT * FROM table LIMIT 10;
            
            QueryHandle handle = client.query(query);
            
            Results results = client.fetch(handle,false,100);
            
            List<String> data = results.data;
            
            for(int i=0;i<data.size();i++) {
                System.out.println(data.get(i));
            }
        }
        catch(Exception e) 
            {
                                e.printStackTrace();
        }

    }
}
