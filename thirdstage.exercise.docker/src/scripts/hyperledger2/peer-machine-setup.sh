#! /bin/bash

# Scripts to setup node machine for peer from minimal Ubuntu 16.04
# Performs the followings
#   - Install Go, if necessary
#   - Install Docker CE, if necessary
#   - Install Node.js, if ncessary
#   - Install Python, if necessary
#   - Run Prometheus Node Exporter as a Docker container

# Before proceed, add 'administrator ALL=(ALL) NOPASSWD: ALL' into '/etc/sudoers' using 'sudo visudo'
# to avoid password prompt during script execution.

# TODO Check whether '/etc/sudoers' contains 'ALL=(ALL) NOPASSWD: ALL' for current login user.
# TODO Check the OS is Ubuntu or not

# Define target versions of softwares
UBUNTU_VER="16.04"
GO_VER="1.8"
DOCKER_VER="17"
NODEJS_VER="6.10"
PYTHON3_VER="3.5"

# Check the version of Ubuntu
VER=$(sudo lsb_release -r | awk '{print $2}')

if [ "$VER" != $UBUNTU_VER ]; then
 echo "Ubuntu 16.04 is expected, but found $VER"
 return 1
fi

echo "Updating apt package list"
sudo apt update

# Install Go, if necessary
CNT=$(sudo dpkg --get-selections | grep -v deinstall | grep golang-1.8-go | wc -l)

if [ $CNT -lt 1 ]; then
  echo "Installing Go $GO_VER"
  # For more on installing Golang on Ubuntu, refer https://github.com/golang/go/wiki/Ubuntu
  sudo add-apt-repository ppa:longsleep/golang-backports
  sudo apt-get update
  sudo apt-get install -y golang-$GO_VER-go=$GO_VER*
fi

# TODO Check $GOPATH

# Install Docker, if necessary
CNT=$(sudo dpkg --get-selections | grep -v deinstall | grep docker-ce | wc -l)

if [ $CNT -lt 1 ]; then
  echo "Installing Docker CE $DOCKER_VER"
  # For more on installing Docker on Ubuntu, refer https://docs.docker.com/engine/installation/linux/ubuntu/#prerequisites
  sudo apt-get install apt-transport-https ca-certificates curl software-properties-common
  sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
  sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
  sudo apt-get update
  sudo apt-get install -y docker-ce=$DOCKER_VER*
fi

# Install Node.js, if necessary
CNT=$(sudo dpkg --get-selections | grep -v deinstall | grep nodejs | wc -l)

if [ $CNT -lt 1 ]; then
  echo "Installing Node.js $NODEJS_VER"
  # For more on installing Node.js on Ubuntu, refer
  #  - https://nodejs.org/en/download/package-manager/#debian-and-ubuntu-based-linux-distributions
  #  - https://www.digitalocean.com/community/tutorials/how-to-install-node-js-on-an-ubuntu-14-04-server
  curl -sL https://deb.nodesource.com/setup_6.x | sudo -E bash -
  sudo apt-get install -y nodejs=$NODEJS_VER*
  sudo apt-get install -y build-essential
fi

# Install Python, if necessary
CNT=$(sudo dpkg --get-selections | grep -v deinstall | grep python$PYTHON3_VER | wc -l)

if [ $CNT -lt 1 ]; then
  echo "Installing Python $PYTHON3_VER"
  sudo apt-get install -y python$PYTHON3_VER
fi

# Run Docker container for Prometheus Node Exporter
# References
#  - https://github.com/prometheus/node_exporter
#  - https://www.digitalocean.com/community/tutorials/how-to-install-prometheus-using-docker-on-ubuntu-14-04#step-2-%E2%80%94-setting-up-node-exporter
docker run -d -p ${NODE_EXPORTER_PORT:-9100}:9100 \
  -v "/proc:/host/proc" -v "/sys:/host/sys" -v "/:/rootfs" --net="host" \
  prom/node-exporter \
  -collector.procfs /host/proc \
  -collector.sysfs /host/proc \
  -collector.filesystem.ignored-mount-points "^/(sys|proc|dev|host|etc)($|/)" \
  -collectors.enabled diskstats,filesystem,meminfo,sockstat,stat,time \
  -web.listen-address :9100

sleep 1

CODE=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:${NODE_EXPORTER_PORT:-9100})

if [ $CODE -eq 200 ]; then
  echo "Successfully loaded 'prom/node-exporter' container. Check http://localhost:${NODE_EXPORTER_PORT:-9100}/"
else
  echo "Fail to load 'prom/node-exporter' container. Check Docker logs."
fi

