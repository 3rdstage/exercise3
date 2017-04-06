#!/bin/bash

dmip=$(docker-machine ip)

if ! (npm ls -g json &>/dev/null);
then (npm install -g json);
fi

curl "http://${dmip}:7050/chaincode" -d @- << REQUEST_BODY
{
  "jsonrpc": "2.0",
  "method": "deploy",
  "params": {
    "type": 1,
    "chaincodeID":{
      "name":"official-example02",
        "path":"github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02"
    },
    "ctorMsg": {
        "function":"init",
        "args":["a", "1000", "b", "2000"]
    }
  },
  "id": 2
}
REQUEST_BODY