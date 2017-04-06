#!/bin/bash

dmip=$(docker-machine ip)

if ! (npm ls -g json &>/dev/null);
then (npm install -g json);
fi

curl -G -s 'http://'${dmip}':7050/chain' | json