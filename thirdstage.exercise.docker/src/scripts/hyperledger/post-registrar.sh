#!/bin/bash

dmip=$(docker-machine ip)

if ! (npm ls -g json &>/dev/null);
then (npm install -g json);
fi

curl "http://${dmip}:7050/chaincode" -d @- << REQUEST_BODY
{
  "enrollId": "test_vp0",
  "enrollSecret": "MwYpmSRjupbT"
}
REQUEST_BODY