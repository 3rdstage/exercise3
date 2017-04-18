#!/bin/bash

# For more, refer the followings
#   - http://docs.grafana.org/http_api/data_source/
#   - https://www.brianchristner.io/how-to-setup-docker-monitoring/
#   - https://github.com/grafana/grafana-docker/issues/11

# Check whether or not the influxdb is already created.
code=$(curl -s -o /dev/null -w '%{http_code}' admin:admin@localhost:3000/api/datasources/id/prometheus)

if [ "$code" != "200" ]; then
  curl -X POST -H "Accept: application/json" \
       -H "Content-Type: application-json" \
       http://admin:admin@localhost:${GRAFANA_GUI_PORT:-3000}/api/datasources -d @- <<REQ_BODY
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
