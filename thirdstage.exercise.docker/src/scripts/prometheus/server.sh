#!/bin/bash

# if [ ! -a ~/docker/etc/prometheus/prometheus.yml ]; then
# 	mkdir -p ~/docker/etc/prometheus
# 	cp ./prometheus.yml ~/docker/etc/prometheus
# fi

mkdir -p ~/docker/etc/prometheus
mkdir -p ~/docker/prometheus
mkdir -p ~/docker/var/lib/grafana
cp ./prometheus.yml ~/docker/etc/prometheus

docker-compose -f docker-compose-server.yaml up -d

sleep 1
firefox --new-tab http://localhost:${PROMETHEUS_GUI_PORT:-9090}/consoles/node.html
firefox --new-tab http://localhost:${GRAFANA_GUI_PORT:-3000}/
