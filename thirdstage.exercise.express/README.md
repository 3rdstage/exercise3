Samples on RESTful API
=====

[How to create a REST API with Express.js in Node.js](https://www.robinwieruch.de/node-express-server-rest-api)

~~~~bash

$ curl http://localhost:3000/rest/messages

$ curl http://localhost:3000/rest/messages -X POST -H "Content-Type:application/json" -d '{"text": "Hello, World!"}' 

$ curl http://localhost:3000/eth/basic | jq .

$ curl -H "Accept: application/json" 'http://localhost:3000/eth/test-contract/sum?a=10&b=20' | jq .

~~~~