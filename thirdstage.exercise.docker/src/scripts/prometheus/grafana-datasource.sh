#!/bin/bash

# For more, refer the followings
#   - http://docs.grafana.org/http_api/data_source/
#   - https://www.brianchristner.io/how-to-setup-docker-monitoring/
#   - https://github.com/grafana/grafana-docker/issues/11

ADDR=$(docker-machine ip)
if [ $? -ne 0 ]; then # with native Docker
  ADDR="localhost"
fi

# Check whether or not the prometheus datasourc is already created.
CODE=$(curl -s -o /dev/null -w '%{http_code}' admin:admin@localhost:3000/api/datasources/id/prometheus)

if [ $CODE -ne 200 ]; then
  curl -X POST -H "Accept: application/json" \
       -H "Content-Type: application-json" \
     http://admin:admin!@34@${ADDR}:${GRAFANA_GUI_PORT:-3000}/api/datasources -d @- <<REQ_BODY
  {
    "name": "prometheus",
    "type": "prometheus",
    "isDefault": true,
    "url": "http://localhost:9090",
    "access": "direct",
    "basicAuth": false
  }
REQ_BODY
fi
