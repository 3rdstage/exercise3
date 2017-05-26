#!/bin/bash

# Referneces
#   * https://hyperledger-fabric.readthedocs.io/en/latest/Setup/ca-setup.html#install


# Check go version
go version

if [ $? -ne 0 ]; then
  echo 'This program requires Golang' >&2
  exit 1
fi

# Check GOPATH env. variable
if [ -z $GOPATH ]; then
  echo 'GOPATH is should be defined' >&2
  exit 1
fi

# Install fabric-ca-client
go get github.com/hyperledger/fabric-ca/cmd/fabric-ca-client
cd $GOPATH/src/github.com/hyperledger/fabric-ca/cmd/fabric-ca-client
git checkout ${FABRIC_VERSION:-v1.0.0-alpha2}
go build







