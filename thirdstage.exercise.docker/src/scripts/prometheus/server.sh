#!/bin/bash

if [ ! -a ~/docker/etc/prometheus/prometheus.yml ]; then
	mkdir -p ~/docker/etc/prometheus
	cp ./prometheus.yml ~/docker/etc/prometheus
fi

docker-compose -f docker-compose-server.yaml up -d
