
function get_deps() {
    rm -rf deps
    mkdir deps 
    cd deps
    wget http://repo1.maven.org/maven2/org/apache/thrift/libthrift/0.9.0/libthrift-0.9.0.jar
    wget https://repository.cloudera.com/content/groups/public/org/apache/hive/hive-service/0.10.0-cdh4.3.0/hive-service-0.10.0-cdh4.3.0.jar
    wget https://repository.cloudera.com/content/groups/public/org/apache/hive/hive-metastore/0.10.0-cdh4.3.0/hive-metastore-0.10.0-cdh4.3.0.jar 
    wget http://www.java2s.com/Code/JarDownload/slf4j/slf4j.api-1.6.1.jar.zip
    unzip slf4j.api-1.6.1.jar.zip
    wget http://apache.crihan.fr/dist//commons/lang/binaries/commons-lang3-3.1-bin.tar.gz
    tar xzf commons-lang3-3.1-bin.tar.gz
    cd ..
}

get_deps
