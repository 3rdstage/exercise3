#!/bin/bash

# References
#  - https://github.com/prometheus/node_exporter
#  - https://www.digitalocean.com/community/tutorials/how-to-install-prometheus-using-docker-on-ubuntu-14-04#step-2-%E2%80%94-setting-up-node-exporter
docker run -d -p ${NODE_EXPORTER_PORT:-9100}:9100 \
  -v "/proc:/host/proc" -v "/sys:/host/sys" -v "/:/rootfs" --net="host" \
  prom/node-exporter \
  -collector.procfs /host/proc \
  -collector.sysfs /host/proc \
  -collector.filesystem.ignored-mount-points "^/(sys|proc|dev|host|etc)($|/)" \
  -collectors.enabled diskstats,filesystem,meminfo,netdev,sockstat,stat,time \
  -web.listen-address :9100

sleep 1

CODE=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:${NODE_EXPORTER_PORT:-9100})

if [ $CODE -eq 200 ]; then
  echo "Successfully loaded 'prom/node-exporter' container. Check http://localhost:${NODE_EXPORTER_PORT:-9100}/"
else
  echo "Fail to load 'prom/node-exporter' container. Check Docker logs."
fi



