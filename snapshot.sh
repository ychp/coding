# !/bin/sh
echo "跳转到源码目录"
cd /home/admin/works/cms

echo "请输入你要打包的分支名(master or develop):"
read branch
git checkout $branch

echo "git pull"
git pull

echo "mvn clean"
mvn clean

echo "请输入你要打包的环境(dev or snapshot or release):"
read config
echo "mvn install $config"
#echo "mvn install"
mvn install -Dmaven.test.skip -P$config
#mvn install -Dmaven.test.skip -Psnapshot

#end