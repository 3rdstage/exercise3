#!/bin/bash

if [ ! -a ~/docker/etc/prometheus/prometheus.yml ]; then
	mkdir -p ~/docker/etc/prometheus
	cp ./prometheus.yml ~/docker/etc/prometheus
fi

docker-compose -f docker-compose-server.yaml up -d

firefox --new-tab http://localhost:${PROMETHEUS_GUI_PORT:-9090}/consoles/node.html
firefox --new-tab http://localhost:${GRAFANA_GUI_PORT:-3000}/
