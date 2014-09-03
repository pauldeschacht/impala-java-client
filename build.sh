#!/bin/bash

function get_deps() {
    rm -rf deps
    mkdir deps 
    cd deps
    wget http://repo1.maven.org/maven2/org/apache/thrift/libthrift/0.9.1/libthrift-0.9.1.jar
    wget https://repository.cloudera.com/content/groups/public/org/apache/hive/hive-service/0.10.0-cdh4.3.0/hive-service-0.10.0-cdh4.3.0.jar
    wget https://repository.cloudera.com/content/groups/public/org/apache/hive/hive-metastore/0.10.0-cdh4.3.0/hive-metastore-0.10.0-cdh4.3.0.jar 
    wget http://www.java2s.com/Code/JarDownload/slf4j/slf4j.api-1.6.1.jar.zip
    unzip slf4j.api-1.6.1.jar.zip
    wget http://apache.crihan.fr/dist//commons/lang/binaries/commons-lang3-3.3.2-bin.tar.gz
    tar xzf commons-lang3-3.3.2-bin.tar.gz
    cd ..
}


function generate_java() {
    #for details see https://github.com/cloudera/impala/blob/impala-v1.0/common/thrift/CMakeLists.txt
    rm -rf gen-java
    rm -rf classes

    thrift -gen java ./thrift/ImpalaService.thrift
    thrift -gen java ./thrift/beeswax.thrift
    thrift -gen java ./thrift/Status.thrift
    thrift -gen java ./thrift/cli_service.thrift
}

get_deps
generate_java
ant compile
ant jar 

cd test
./build.sh
