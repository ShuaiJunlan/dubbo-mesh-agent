#!/bin/bash

ETCD_HOST=etcd
ETCD_PORT=2379
ETCD_URL=http://$ETCD_HOST:$ETCD_PORT

echo ETCD_URL = $ETCD_URL

if [[ "$1" == "consumer" ]]; then
  echo "Starting consumer agent..."
  java -jar \
       -server \
       -Xms1536M \
       -Xmx1536M \
       -XX:+UseParallelGC \
       -XX:+AggressiveOpts \
       -XX:+UseFastAccessorMethods \
       -Dtype=consumer \
       -Dserver.port=20000\
       -Detcd.url=$ETCD_URL \
       -Dlogs.dir=/root/logs \
       -Dagent.type=client \
       /root/dists/mesh-agent.jar
elif [[ "$1" == "provider-small" ]]; then
  echo "Starting small provider agent..."
  java -jar \
       -server \
       -Xms512M \
       -Xmx512M \
       -XX:+UseParallelGC \
       -XX:+AggressiveOpts \
       -XX:+UseFastAccessorMethods \
       -Dtype=provider \
       -Ddubbo.protocol.port=20880 \
       -Detcd.url=$ETCD_URL \
       -Dagent.type=server \
       -Dagent.port=30000 \
       -Dagent.dubbo.client.threads=50 \
       -Dagent.provider.epoll.threads=6 \
       -Dagent.provider.executors=50 \
       -Dlogs.dir=/root/logs \
       /root/dists/mesh-agent-provider.jar
elif [[ "$1" == "provider-medium" ]]; then
  echo "Starting medium provider agent..."
  java -jar \
       -server \
       -Xms1536M \
       -Xmx1536M \
       -XX:+UseParallelGC \
       -XX:+AggressiveOpts \
       -XX:+UseFastAccessorMethods \
       -Dtype=provider \
       -Ddubbo.protocol.port=20880 \
       -Detcd.url=$ETCD_URL \
       -Dlogs.dir=/root/logs \
       -Dagent.type=server \
       -Dagent.port=30000 \
       -Dagent.dubbo.client.threads=100 \
       -Dagent.provider.epoll.threads=6 \
       -Dagent.provider.executors=100 \
       /root/dists/mesh-agent-provider.jar
elif [[ "$1" == "provider-large" ]]; then
  echo "Starting large provider agent..."
  java -jar \
       -server \
       -Xms2560M \
       -Xmx2560M \
       -XX:+UseParallelGC \
       -XX:+AggressiveOpts \
       -XX:+UseFastAccessorMethods \
       -Dtype=provider \
       -Ddubbo.protocol.port=20880 \
       -Detcd.url=$ETCD_URL \
       -Dlogs.dir=/root/logs \
       -Dagent.type=server \
       -Dagent.port=30000 \
       -Dagent.dubbo.client.threads=150 \
       -Dagent.provider.epoll.threads=8 \
       -Dagent.provider.executors=150 \
       /root/dists/mesh-agent-provider.jar
else
  echo "Unrecognized arguments, exit."
  exit 1
fi
