const express = require('express');
const router = express.Router();


let users = {
  1: { id: '1', username: 'Robin',},
  2: { id: '2', username: 'Dave',},
}


router.get('/users', function(req, res, next){
  res.set('Content-Type', 'application/json');
  res.send(JSON.stringify(Object.values(users), null, 2));
})

router.get('/users/:userId', function(req, res, next) {
  res.set('Content-Type', 'application/json');
  res.send(JSON.stringify(users[req.params.userId], null, 2));
});

router.post('/users', function(req, res, next){
  res.set('Content-Type', 'application/json');
  res.send('Created a new user.\n');
});

router.put('/users/:userId', function(req, res, next){
  res.set('Content-Type', 'application/json');
  res.send(`Updated user of '${req.params.userId}'.\n`);
});

router.delete('/users/:userId', function(req, res, next){
  res.set('Content-Type', 'application/json');
  res.send(`Removed user of '${req.params.userId}'.\n`);
});

module.exports = router;