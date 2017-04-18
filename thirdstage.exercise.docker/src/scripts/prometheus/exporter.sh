#!/bin/bash

# References
#  - https://github.com/prometheus/node_exporter#using-docker
#  - https://www.digitalocean.com/community/tutorials/how-to-install-prometheus-using-docker-on-ubuntu-14-04#step-2-%E2%80%94-setting-up-node-exporter
docker run -d -p ${NODE_EXPORTER_PORT:-9100}:9100 \
-v "/proc:/host/proc" -v "/sys:/host/sys" -v "/:/rootfs" --net="host" \
prom/node-exporter \
-collector.procfs /host/proc \
-collector.sysfs /host/proc \
-collector.filesystem.ignored-mount-points "^/(sys|proc|dev|host|etc)($|/)"



