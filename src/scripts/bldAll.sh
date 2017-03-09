#see .bash_profile sample - to be saves as "~/.bash_profile", quit and restart terminal app to run this file after save.

#set in .bash_profile
#export BLD_HOME=
#export WORKSPACE_HOME=
echo $BLD_HOME
This_Script_Home=`dirname $0`
echo "This_Script_Home $This_Script_Home"

echo "bld $BLD_HOME"
echo "w home $WORKSPACE_HOME "
echo "THIS_SCRIPT $This_Script_Home"
cd  $This_Script_Home/../../../
cp -f $This_Script_Home/pomAll.xml pomAll.xml
echo "now at "
pwd
sleep 2
mvn -f ./pomAll.xml clean install -Dmaven.test.skip=true
rm ./pomAll.xml