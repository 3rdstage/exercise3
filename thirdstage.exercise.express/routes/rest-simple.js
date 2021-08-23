var express = require('express');
var router = express.Router();

/* GET users listing. */
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