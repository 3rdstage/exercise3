#!/bin/bash

# For influxdb docker image, refer 'https://hub.docker.com/r/library/influxdb/'


# For web admin, access 'http://localhost:8083/
# For Go profiling, access 'http://localhost:8086/debug/pprof' first


readonly image_name="influxdb-profiling"
readonly image_ver="1.2.4"

if [ `docker images -q ${image_name}:${image_ver} | wc -l` -eq 0 ]; then
  echo "Building docker images of '${image_name}:${image_ver}'. The may take a few miniutes."
  docker build -t ${image_name}:${image_ver} -f influxdb-profiling.dockerfile .
fi

docker run -d \
--name ${image_name} \
-p 8086:8086 \
-p 8088:8088 \
-p 8083:8083 \
-e INFLUXDB_DB=test1 \
-e INFLUXDB_HTTP_AUTH_ENABLED=false \
-e INFLUXDB_ADMIN_ENABLED=true \
-e INFLUXDB_ADMIN_USER=admin \
-e INFLUXDB_ADMIN_PASSWORD=admin!@34 \
-e INFLUXDB_USER=user \
-e INFLUXDB_USER_PASSWORD=user!@34 \
-e INFLUXDB_HTTP_PPROF_ENABLED=true \
-v /var/docker/influxdb:/var/lib/influxdb \
${image_name}:${image_ver}
