FROM influxdb:1.2.4
# influxdb 1.3 excluded built-in web admin

MAINTAINER 3rdstage
ENV container docker

RUN apt-get -y update && \
    apt-get -y install procps net-tools golang-1.8 && \
    sed -i '$ a \\nexport GOROOT=/usr/lib/go-1.8\nexport PATH=$PATH:$GOROOT/bin\n' ~/.bashrc

