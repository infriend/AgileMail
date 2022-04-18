projectName="agilemail"
destPath="$HOME/$projectName"
srcPath="$WORKSPACE/src/main/resources/agile-frontend"
target=build
log=$destPath/front.log

echo " "
echo "Build Frontend"
cd "$srcPath" || exit
npm run build

echo " "
# stop frontend
echo "Stopping frontend process"
ps aux | grep -i "serve -s $destPath/$target" | grep -v grep | awk '{print $2}' | xargs kill
sleep 3

# delete file
echo "Deleting $destPath/$target"
rm -rf "$destPath/$target"

# copy file
echo "Copying files from $srcPath/$target"
cp -r "$srcPath/$target" "$destPath"

# run
nohup nice serve -s $destPath/$target > $log 2>&1 &
echo "COMMAND: nohup nice serve -s $destPath/$target > $log 2>&1 &"
echo " "

cd "$WORKSPACE" || exit
