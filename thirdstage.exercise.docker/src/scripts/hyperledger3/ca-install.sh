#!/bin/bash

# Referneces
#   * https://hyperledger-fabric.readthedocs.io/en/latest/Setup/ca-setup.html#install


# Check go version
go version >/dev/null

if [ $? -ne 0 ]; then
  echo 'This program requires Golang' >&2
  exit 1
fi

# Check GOPATH env. variable
if [ -z $GOPATH ]; then
  echo 'GOPATH is should be defined' >&2
  exit 1
fi

ca_version=${FABRIC_VERSION:-v1.0.0-alpha2}

# Install fabric-ca-client
echo "Starting downloading 'fabric-ca-client' source which may take some minutes depending on the situation."
go get -d github.com/hyperledger/fabric-ca/cmd/fabric-ca-client
if [ $? -eq 0 ]; then
  echo "Finished dowloading 'fabric-ca-client' source."
fi
git -C $GOPATH/src/github.com/hyperledger/fabric-ca checkout -f ${ca_version}

echo "Starting installing 'fabric-ca-server' ${ca_version} which may take some minutes depending on the situation."
go install github.com/hyperledger/fabric-ca/cmd/fabric-ca-server
if [ $? -eq 0 ]; then
  echo "Finished installing 'fabric-ca-server'."
  echo "Check $GOPATH/bin/fabric-ca-server'."
fi

echo "Starting installing 'fabric-ca-client' ${ca_version} which may taske some minutes depending on the situation."
go install github.com/hyperledger/fabric-ca/cmd/fabric-ca-client
if [ $? -eq 0 ]; then
  echo "Finished installing 'fabric-ca-client'."
  echo "Check $GOPATH/bin/fabric-ca-client'."
fi







