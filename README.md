impala-java-client
==================

Java client to connect directly to Impala using thrift (similar to the impala-shell is doing in python)

The Java client is build using trift interface.

Download the necessary dependencies into the deps directory

- mkdir deps; cd deps
- wget http://repo1.maven.org/maven2/org/apache/thrift/libthrift/0.9.0/libthrift-0.9.0.jar
- wget https://repository.cloudera.com/artifactory/libs-release-local/org/apache/thrift/libthirft/0.9.0/libthrift-0.9.0.jar
- wget https://repository.cloudera.com/content/groups/public/org/apache/hive/hive-service/0.9.0-cdh4.1.3/hive-service-0.9.0-cdh4.1.3-sources.jar
- wget https://repository.cloudera.com/content/groups/public/org/apache/hive//hive-metastore/0.9.0-cdh4.1.3/hive-metastore-0.9.0-cdh4.1.3.jar
- wget http://www.java2s.com/Code/JarDownload/slf4j/slf4j.api-1.6.1.jar.zip
- unzip slf4j.api-1.6.1.jar.zip

Generate the thrift java code for the client

- thrift -gen java ./thrift/ImpalaService.thrift
- thrift -gen java ./thrift/beeswax.thrif
- thrift -gen java ./thrift/Status.thrift
- thrift -gen java ./thrift/cli_service.thrift

Use ant to compile the generated code and build the jar file. The dependencies must be in the deps directory 

- ant compile
- ant build 

TODO: Use maven so that the project can easily be imported in clojure / clojar.



