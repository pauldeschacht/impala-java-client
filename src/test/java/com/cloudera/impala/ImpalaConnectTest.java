package com.cloudera.impala;

import java.util.List;

import org.apache.hive.service.cli.thrift.TCloseOperationReq;
import org.apache.hive.service.cli.thrift.TCloseSessionReq;
import org.apache.hive.service.cli.thrift.TExecuteStatementReq;
import org.apache.hive.service.cli.thrift.TExecuteStatementResp;
import org.apache.hive.service.cli.thrift.TFetchResultsReq;
import org.apache.hive.service.cli.thrift.TFetchResultsResp;
import org.apache.hive.service.cli.thrift.TOpenSessionReq;
import org.apache.hive.service.cli.thrift.TOpenSessionResp;
import org.apache.hive.service.cli.thrift.TOperationHandle;
import org.apache.hive.service.cli.thrift.TProtocolVersion;
import org.apache.hive.service.cli.thrift.TRow;
import org.apache.hive.service.cli.thrift.TRowSet;
import org.apache.hive.service.cli.thrift.TSessionHandle;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.junit.Test;

import com.cloudera.beeswax.api.Query;
import com.cloudera.beeswax.api.QueryHandle;
import com.cloudera.beeswax.api.QueryState;
import com.cloudera.beeswax.api.Results;
import com.cloudera.impala.thrift.ImpalaHiveServer2Service;
import com.cloudera.impala.thrift.ImpalaService;

public class ImpalaConnectTest
{
    private static String host="nceoricloud02";
    private static int port=21050;
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

            //ImpalaConnectTest.testConnectionBeeswax(host,port,stmt);
            ImpalaConnectTest.testConnectionHiveServer2(host,port,stmt);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void mainSuccess() {
    	ImpalaConnectTest.testConnectionHiveServer2(host, port, stmt);
    }

    protected static void testConnectionBeeswax(String host, int port, String statement){
        try {
            //open connection
            TSocket transport = new TSocket(host,port);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            //connect to client
            ImpalaService.Client client = new ImpalaService.Client(protocol);
            client.PingImpalaService();
            
            Query query = new Query();
            query.setQuery(statement); // hive statement: SELECT * FROM table LIMIT 10;
            
            QueryHandle handle = client.query(query);
            
            boolean done = false;
            while(done == false) {
                Results results = client.fetch(handle,false,100);
                QueryState queryState = client.get_state(handle);
                /*
                while(queryState != ImpalaService$Client.FINISHED) {
                    //sleep(0.5)
                    queryState = client.get_state(query);
                }
                */
            
                List<String> data = results.data;
            
                for(int i=0;i<data.size();i++) {
                    System.out.println(data.get(i));
                }

                if(results.has_more==false) {
                    done = true;
                }

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    protected static void testConnectionHiveServer2(String host, int port, String statement) {
        try {
            TSocket transport = new TSocket(host,port);
            
            transport.setTimeout(60000);
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            ImpalaHiveServer2Service.Client client = new ImpalaHiveServer2Service.Client(protocol);  
            
            transport.open();
            
            String username = "pauldeschacht";
            String password = "amadeus";
            TOpenSessionReq openReq = new TOpenSessionReq();
            openReq.setClient_protocol(TProtocolVersion.HIVE_CLI_SERVICE_PROTOCOL_V1);
            openReq.setUsername(username);
            openReq.setPassword(password);

            TOpenSessionResp openResp = client.OpenSession(openReq);
            org.apache.hive.service.cli.thrift.TStatus status = openResp.getStatus();
            if (status.getStatusCode() == org.apache.hive.service.cli.thrift.TStatusCode.ERROR_STATUS) {
                String msg = status.getErrorMessage();
                System.out.println(msg);
                return;
            }
            if(status.getStatusCode() != org.apache.hive.service.cli.thrift.TStatusCode.SUCCESS_STATUS) {
                System.out.println("No success");
                return;
            }
            TSessionHandle sessHandle = openResp.getSessionHandle();
            
            TExecuteStatementReq execReq = new TExecuteStatementReq(sessHandle, statement);
            TExecuteStatementResp execResp = client.ExecuteStatement(execReq);
            status = execResp.getStatus();
            if (status.getStatusCode() == org.apache.hive.service.cli.thrift.TStatusCode.ERROR_STATUS) {
                String msg = status.getErrorMessage();
                System.out.println(msg + "," + status.getSqlState() + "," + Integer.toString(status.getErrorCode()) + "," + status.isSetInfoMessages());
                System.out.println("After ExecuteStatement: " + statement);
                return;
            }

            TOperationHandle stmtHandle = execResp.getOperationHandle();

            if (stmtHandle == null) {
                System.out.println("Empty operation handle");
                return;
            }

            TFetchResultsReq fetchReq = new TFetchResultsReq();
            fetchReq.setOperationHandle(stmtHandle);
            fetchReq.setMaxRows(100);
            //org.apache.hive.service.cli.thrift.TFetchOrientation.FETCH_NEXT
            TFetchResultsResp resultsResp = client.FetchResults(fetchReq);

            status = resultsResp.getStatus();
            if (status.getStatusCode() == org.apache.hive.service.cli.thrift.TStatusCode.ERROR_STATUS) {
                String msg = status.getErrorMessage();
                System.out.println(msg + "," + status.getSqlState() + "," + Integer.toString(status.getErrorCode()) + "," + status.isSetInfoMessages());
                System.out.println("After FetchResults: " + statement);
                return;
            }

            TRowSet resultsSet = resultsResp.getResults();
            List<TRow> resultRows = resultsSet.getRows();
            System.out.println("Result size = " + Integer.toString(resultRows.size()) );
            for(TRow resultRow : resultRows){
                System.out.println(resultRow.toString());
            }
            
            TCloseOperationReq closeReq = new TCloseOperationReq();
            closeReq.setOperationHandle(stmtHandle);
            client.CloseOperation(closeReq);
            TCloseSessionReq closeConnectionReq = new TCloseSessionReq(sessHandle);
            client.CloseSession(closeConnectionReq);
            
            transport.close();    
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
