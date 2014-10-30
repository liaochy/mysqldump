#!/bin/sh
cd `dirname $0`
cdir=`pwd`
MYSQLDUMP_HOME=`dirname $cdir`
export MYSQLDUMP_HOME
if [[ -z $1 ]]
then
CONF_FILE=$MYSQLDUMP_HOME
else
CONF_FILE=$1
fi
BEE_LIB=$MYSQLDUMP_HOME/lib
CLASSPATH=.
for jar in `ls $BEE_LIB/*.jar`
do
      CLASSPATH="$CLASSPATH:""$jar"
done
if [ "x$HADOOP_HOME" != "x" ]; then
	CLASSPATH=$CLASSPATH:$HADOOP_HOME/conf
else
      echo "HADOOP_HOME IS NULL"
      exit 1
fi
CLASSPATH=$CLASSPATH:$CONF_FILE/conf
JAVA_PLATFORM=`CLASSPATH=${CLASSPATH} java -Xmx32m org.apache.hadoop.util.PlatformName | sed -e "s/ /_/g"`
LZO_LIBRARY_PATH=${MYSQLDUMP_HOME}/lib/native/${JAVA_PLATFORM}
JAVA_LIBRARY_PATH=${LZO_LIBRARY_PATH}/lib

for jar in `ls $LZO_LIBRARY_PATH/*.jar`
do
      CLASSPATH="$CLASSPATH:""$jar"
done

echo =================================starting the bee keeper at `date` ========================================
java  -Djava.library.path=$JAVA_LIBRARY_PATH -classpath $CLASSPATH:$MYSQLDUMP_HOME/mysqldump.jar com.sohu.tw.mysqldump.MysqlDump
