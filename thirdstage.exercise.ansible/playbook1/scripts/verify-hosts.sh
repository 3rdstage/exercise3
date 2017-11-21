#! /bin/bash

# Checks if the machine is prepared properly or not
#   - Checks the OS
#   - Checks the version of OS
#   - Checks whether the necessary packages are installed or not
#      . Golang
#      . Nodejs
#      . Docker
#      . OpenSSH Server


username=${1:-administrator}
updates_apt=${2:-false} # boolean flag indicating whether to update APT index or not

# Chesks whether the current username is one that is expected or not.
if [ "${username}" != $(whoami) ]; then
  echo ""
  echo "Unexpected username. '${username}' is expected."
  echo ""
  #exit 1
fi

# Checks sudoer configuration
if [ $(sudo ls / &> /dev/null; echo $?) -ne 0 ]; then
  echo ""
  echo "Current user should be 'sudoer'." # TODO Update message
  echo ""
  #exit 1
fi

if [ $(sudo cat /etc/sudoers | grep -E "^$username\s*ALL=NOPASSWD" | wc -l) -lt 1 ]; then
  echo ""
  echo "Current user is 'sudoer' but is not set to ..."
  echo ""
fi

# Checks OS
distro=$(sudo cat /etc/*-release | grep DISTRIB_ID | sed 's/^DISTRIB_ID=//')
if [ ${distro} != "Ubuntu" ]; then
  echo ""
  echo "Unsupported OS : ${distro}"
  echo "Currently only Ubuntu is supported."
  echo ""
  #exit 1
fi

os_ver=$(sudo cat /etc/*-release | grep DISTRIB_RELEASE | sed 's/^DISTRIB_RELEASE=//')
if [ ${os_ver} != "16.04" ]; then
  echo ""
  echo "Unsupported Ubuntu version : ${os_ver}"
  echo "Currently only Ubuntu 16.04 is supported."
  echo ""
  #exit 1
fi


# TODO Checks the CPU cores are equal or more than 4
# TODO Checks total amount RAM is equal or more than 32GB
# TODO Checks the unused amount of disk space is more than ...
# TODO Checks the network interface card.

if [ $updates_apt ]; then
  sudo apt-get update -yq
fi

# Checks whether Golang is installed and configured properly or not
if [ $(sudo dpkg -l | awk '{print $2}' | grep -E '^golang-(1\.8-)?go$' | wc -l) -lt 1 ]; then
  echo ""
  echo "Golang 1.8 or higher should be installed."
  echo ""
  #exit 1
fi

if [ ! $GOPATH ]; then
  echo ""
  echo "'GOPATH' environment varialbe should be defined."
  echo ""
  #exit 1
fi

if [ ! $(which go) ]; then
  echo ""
  echo "Go toolchain should be on PATH."
  echo ""
  #exit 1
fi

go_ver=$(go version | sed 's/.*go\([1-9]\.[1-9][0-9]*\).*/\1/')
go_ver_major=$(echo ${go_ver} | sed 's/\([1-9]\)\.[1-9][0-9]*/\1/')
go_ver_minor=$(echo ${go_ver} | sed 's/[1-9]\.\([1-9][0-9]*\)/\1/')
if [ ${go_ver_major} -lt 1 ] || ([ ${go_ver_major} -eq 1 ] && [ ${go_ver_minor} -lt 8 ]); then
  echo ""
  echo "Go version should be 1.8 or higher, but current installed version is '${go_ver}'."
  echo ""
  #exit 1
else
  echo ""
  echo "Successfuly verified that Golang 1.8 or higher is intalled and configured properly."
  echo ""
fi


# Checks whether Node.js is installed an configured properly or not
if [ $(sudo dpkg -l | awk '{print $2}' | grep -E '^nodejs$' | wc -l) -lt 1 ]; then
  echo ""
  echo "Node.js 1.8 should be installed."
  echo ""
  #exit 1
fi

if [ ! $(which node) ]; then
  echo ""
  echo "Node command('node') should be on PATH."
  echo ""
  #exit 1
fi

nodejs_ver=$(node -v | sed 's/v\([1-9]\.[1-9][0-9]*\).*/\1/')
nodejs_ver_major=$(echo ${nodejs_ver} | grep -oE '^[1-9]')
nodejs_ver_minor=$(echo ${nodejs_ver} | grep -oE '[1-9][0-9]*$')
if [ ${nodejs_ver_major} -ne 6 ] || ([ ${nodejs_ver_major} -eq 6 ] && [ ${nodejs_ver_minor} -lt 9 ]); then
  echo ""
  echo "Node.js version should be 6.x where x is 9 or higher, but current installed version is ${nodejs_ver}"
  echo ""
  #exit 1
else
  echo ""
  echo "Successfuly verified that Node.js 6.x (x >= 9) is intalled and configured properly."
  echo ""
fi

if [ $(npm --version -g) != "3.10.10" ]; then
  echo ""
  echo "Updating npm to 3.10.10 version."
  sudo npm install npm@3.10.10 -g > /dev/null
  echo ""
fi


# Checks whether Docker is installed and configured properly or not
if [ $(sudo dpkg -l | awk '{print $2}' | grep -E '^docker-ce$' | wc -l) -lt 1 ]; then
  echo ""
  echo "Docker CE 17 or higher should be installed."
  echo ""
  #exit 1
fi

if [ ! $(which docker) ]; then
  echo ""
  echo "Docker command('docker') should be on PATH."
  echo ""
  #exit 1
fi

docker_ver=$(docker --version | sed 's/Docker version \([1-9][0-9]*\.[0-9][0-9]*\)\..*/\1/')
docker_ver_major=$(echo ${docker_ver} | grep -oE '^[1-9][0-9]*')
docker_ver_minor=$(echo ${docker_ver} | grep -oE '[1-9][0-9]*$')
# TODO Checks Docker version


# TODO Checks whether Docker Compose is installed and...


# TODO Checks Docker Compose version


# TODO Checks Fabric Docker images


# TODO Checks whether NTP server is setup or not


