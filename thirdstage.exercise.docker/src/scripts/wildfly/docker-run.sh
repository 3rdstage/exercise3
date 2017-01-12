#!/bin/sh

mkdir -p /c/var/opt/jboss/wildfly/deployments

docker run -itP --rm --privileged --name butterfly \
  -v /sys/fs/cgroup -v /c/var/opt/jboss/wildfly/deployments:/var/opt/jboss/wildfly/deployments:rw \
  -p 10022:22 -p 18080:8080 -p 19990:9990 \
  wildfly:0.13
  

 