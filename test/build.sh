
# get the dependencies

mkdir deps; cd deps;
wget http://www.java2s.com/Code/JarDownload/slf4j/slf4j-simple-1.6.1.jar.zip
unzip -n slf4j-simple-1.6.1.jar.zip
cd ..
# compile and build the jar
mkdir classes
javac -cp ../jar/ImpalaService.jar:../deps/libthrift-0.9.0.jar -d classes src/org/ImpalaConnectTest.java
mkdir jar
jar -cvfm ./jar/ImpalaConnectTest.jar manifest.txt  -C classes .
# run the example
java -cp ../deps/libthrift-0.9.0.jar:../deps/slf4j.api-1.6.1.jar:./deps/slf4j-simple-1.6.1.jar:../jar/ImpalaService.jar:./jar/ImpalaConnectTest.jar org.ImpalaConnectTest.ImpalaConnectTest nceoricloud02 21000 "SELECT * FROM OAG.OagReporting LIMIT 200"
