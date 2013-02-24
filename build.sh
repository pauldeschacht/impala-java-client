mkdir deps; 
cd deps
wget http://repo1.maven.org/maven2/org/apache/thrift/libthrift/0.9.0/libthrift-0.9.0.jar
wget https://repository.cloudera.com/content/groups/public/org/apache/hive/hive-service/0.9.0-cdh4.1.3/hive-service-0.9.0-cdh4.1.3-sources.jar
wget https://repository.cloudera.com/content/groups/public/org/apache/hive//hive-metastore/0.9.0-cdh4.1.3/hive-metastore-0.9.0-cdh4.1.3.jar
wget http://www.java2s.com/Code/JarDownload/slf4j/slf4j.api-1.6.1.jar.zip
unzip slf4j.api-1.6.1.jar.zip
cd ..

thrift -gen java ./thrift/ImpalaService.thrift
thrift -gen java ./thrift/beeswax.thrift
thrift -gen java ./thrift/Status.thrift
thrift -gen java ./thrift/cli_service.thrift

ant compile
ant build 

cd test
./build.sh
