const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const config = require('config');

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');
const restRouters = require('./routes/rest');
const ethRouters = require('./routes/eth');

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: true })); 
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/rest', restRouters);
app.use('/eth', ethRouters);

console.log("Loaded application");
console.log("Cofiguration : ");
console.log(config.util.toObject());

module.exports = app;
