const express = require('express');
const router = express.Router();
const { v4: uuidv4 } = require('uuid');

let messages = {};

router.get('/messages', (req, res) => {
  res.set('Content-Type', 'application/json');
  res.send(JSON.stringify(Object.values(messages), null, 2) + '\n');
  
});

router.post('/messages', (req, res) => {
  const id = uuidv4();
  const msg = {
    id,
    text: req.body.text,
  };
  
  messages[id] = msg;
  res.set('Content-Type', 'application/json');
  res.send(JSON.stringify(msg, null, 2) + '\n');
});

router.delete('/messages/:msgId', (req, res) => {
  const {
    [req.params.msgId] : msg,
    ...others
  } = messages;
  
  messages = others;
  res.set('Content-Type', 'application/json');
  res.send(JSON.stringify(msg, null, 2) + '\n');
});

module.exports = router;
