const express = require('express');
const router = express.Router();

router.get('/', function(req, res, next) {
  res.send('Received GET method.\n');
});

router.post('/', function(req, res, next){
  res.send('Received POST method.\n');
});

router.put('/', function(req, res, next){
  res.send('Received PUT method.\n');
});

router.delete('/', function(req, res, next){
  res.send('Received DELETE method.\n');
});

module.exports = router;