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

# Check installed softwares
# VER=$(go version)
# VER=$(docker version)
# VER=$(nodejs -v)
# VER=$(python -V)

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
  # Add GOPATH env. variable
  mkdir ~/go
  sed -i '$ a \\n' ~/.bashrc # add blank line after last line
  sed -i "$ a export GOPATH=\$HOME/go" ~/.bashrc
  sed -i "$ a export PATH=\$PATH:\$GOPATH/bin" ~/.bashrc
fi

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

  # For below post-installation steps, refer https://docs.docker.com/engine/installation/linux/linux-postinstall/
  sudo groupadd docker
  sudo usermod -aG docker $USER

  # Install Docker Compose
  # For more refer https://docs.docker.com/compose/install/
  curl -L https://github.com/docker/compose/releases/download/1.13.0-rc1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
fi

# Check whether or not Docker can be run by current user
docker run hello-world
if [ $? -ne 0 ]; then
  echo "Docker can't be run successfully by current user. Check Docker installation or confiuration."
else
  echo "Docker can be run successfully by current user."
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
#docker run -d -p ${NODE_EXPORTER_PORT:-9100}:9100 \
#  -v "/proc:/host/proc" -v "/sys:/host/sys" -v "/:/rootfs" --net="host" \
#  prom/node-exporter \
#  -collector.procfs /host/proc \
#  -collector.sysfs /host/proc \
#  -collector.filesystem.ignored-mount-points "^/(sys|proc|dev|host|etc)($|/)" \
#  -collectors.enabled diskstats,filesystem,meminfo,sockstat,stat,time \
#  -web.listen-address :9100

# Install Prometheus Node Exporter, if necessary
# Ubuntu 16.04 official repository provides Node Exporter 0.11 which is somewhat older
# References
#   - https://github.com/prometheus/node_exporter/tree/0.11.0
CNT=$(sudo dpkg --get-selections | grep -v deinstall | grep prometheus-node-exporter | wc -l)

if [ $CNT -lt 1 ]; then
  echo "Installing Prometheus Node Exporter"
  sudo apt-get install -y prometheus-node-exporter
  sudo cp /etc/default/prometheus-node-exporter /etc/default/prometheus-node-exporter.default
  unset ARG
  sudo sed -i '$ a \' /etc/default/prometheus-node-exporter
  sudo sed -i "$ a ARGS=\$ARGS' -collectors.enabled=diskstats,filesystem,meminfo,netdev,stat,time'" /etc/default/prometheus-node-exporter
  sudo sed -i "$ a ARGS=\$ARGS' -log.level=warn'" /etc/default/prometheus-node-exporter
  # sudo sed -i "$ a ARGS=\$ARGS' -auth.user=agent -auth.pass=agent!@34'" /etc/default/prometheus-node-exporter # for production env.
  # Installing Node Exporter makes init.d can control(start/stop/restart) it
  sudo /etc/init.d/prometheus-node-exporter restart
fi

sleep 1

CODE=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:${NODE_EXPORTER_PORT:-9100})
if [ $CODE -eq 200 ]; then
  echo "Successfully loaded Prometheus Node Exporter. Check http://localhost:${NODE_EXPORTER_PORT:-9100}/"
else
  echo "Fail to load Prometheus Node Exporter. Check logs."
fi

