#!/bin/bash

port=8081
projectName="agilemail"
postfix="0.0.1-SNAPSHOT.jar"
frontendPath="src/main/resources/agile-frontend"

destPath=/home/fragile/$projectName

sourFile=$WORKSPACE/target/$projectName-$postfix
destFile=$destPath/$projectName-$postfix

logName=springboot.log
destLog=$destPath/$logName

echo " "
echo "Build Frontend"
cd $WORKSPACE/$frontendPath
npm run build
cp -r build/** ../public

echo " "
echo "Build Backend"
cd $WORKSPACE
mvn clean package

echo " "
# stop port
echo "Stopping process on port: $port"
fuser -n tcp -k $port > redirection &
echo " "
# delete file
echo "Deleting $destFile"
rm -rf $destFile
echo "Deleting $destLog"
rm -rf $destLog
echo " "
# copy file
echo "Copying files from $sourFile"
cp $sourFile $destFile
echo " "
# change permission
echo "Changing File Permission: chmod 777 $destFile"
chmod 777 $destFile
echo " "
# run
nohup nice java -jar $destFile --server.port=$port $> $destLog 2>&1 &
echo "COMMAND: nohup nice java -jar $destFile --server.port=$port $> $destLog 2>&1 &"
echo " "