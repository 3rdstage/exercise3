#!/bin/bash

# For more, refer the followings
#   - http://docs.grafana.org/http_api/data_source/
#   - https://www.brianchristner.io/how-to-setup-docker-monitoring/
#   - https://github.com/grafana/grafana-docker/issues/11

# Check whether or not the influxdb is already created.
code=$(curl -s -o /dev/null -w '%{http_code}' admin:admin@localhost:3000/api/datasources/id/influxdb)

if [ "$code" != "200" ]; then
  curl -X POST -H "Accept: application/json" \
       -H "Content-Type: application-json" \
       http://admin:admin@localhost:3000/api/datasources -d @- <<REQ_BODY
  {
     "name": "influxdb",
     "type": "influxdb",
     "isDefault": true,
     "url": "http://influxsrv:8086",
     "access": "proxy",
     "basicAuth": true,
     "basicAuthUser": "admin",
     "basicAuthPassword": "admin",
     "database": "cadvisor",
     "user": "root",
     "password": "root"
  }
REQ_BODY
fi
