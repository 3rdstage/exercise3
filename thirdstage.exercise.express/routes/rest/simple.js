const express = require('express');
const router = express.Router();

router.get('/simple', function(req, res, next) {
  res.send('Received GET method.\n');
});

router.post('/simple', function(req, res, next){
  res.send('Received POST method.\n');
});

router.put('/simple', function(req, res, next){
  res.send('Received PUT method.\n');
});

router.delete('/simple', function(req, res, next){
  res.send('Received DELETE method.\n');
});

module.exports = router;