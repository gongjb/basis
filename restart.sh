#!/bin/bash

if ! [ "${jarName}" ]; then
  jarName="ksms.jar"
fi

#找到进程号
server_id=$(ps -ef | grep ${jarName} | grep -v grep|awk '{print $2}')
#
echo ${server_id}
if [ "$server_id" ]; then
  kill -9 ${server_id}
  #检查是否结束进程
  server_restart=$(ps -ef | grep ${jarName} | grep -v grep|awk '{print $2}')
  if ! [ "$server_restart" ]; then
    echo "已经结束进程，开始重启！"
    nohup java -jar ${jarName} &
    echo "重启完成！"
  fi
else
  echo '未找到进程号'
fi


