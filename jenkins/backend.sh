#!/bin/bash
backendPort=8081

projectName="agilemail"
postfix="0.0.1-SNAPSHOT.jar"
target="$projectName-$postfix"

destPath=$HOME/$projectName
srcPath=$WORKSPACE/target

destFile=$destPath/$target

destLog=$destPath/springboot.log

echo " "
echo "Build Backend"
cd "$WORKSPACE" || exit
mvn clean package -DskipTests

echo " "
# stop backend
echo "Stopping backend process"
ps aux | grep -i "$target" | grep -v grep | awk '{print $2}' | xargs kill
sleep 3
echo " "

# delete file
echo "Deleting $destFile"
rm -rf "$destFile"
echo "Deleting $destLog"
rm -rf "$destLog"
echo " "

# copy file
echo "Copying files from $srcPath/$target"
cp "$srcPath/$target" "$destFile"
echo " "

# change permission
echo "Changing File Permission: chmod 777 $destFile"
chmod 777 "$destFile"
echo " "

# run
nohup nice java -jar $destFile --server.port=$backendPort > $destLog 2>&1 &
echo "COMMAND: nohup nice java -jar $destFile --server.port=$backendPort > $destLog 2>&1 &"
echo " "
