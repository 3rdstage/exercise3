#!/bin/bash

dmip=$(docker-machine ip)

if ! (npm ls -g json &>/dev/null);
then (npm install -g json);
fi

curl "http://${dmip}:7050/chaincode" -d @- << REQUEST_BODY
{
  "jsonrpc": "2.0",
  "method": "invoke",
  "params": {
      "type": 1,
      "chaincodeID": {
        "name": "official-example02"
      },
      "ctorMsg": {
        "function": "query",
        "args": ["a"]
      },
      "secureContext": "lukas"
  },
  "id": 7
}
REQUEST_BODY