const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');
const restSimpleRouter = require('./routes/rest/simple');
const restUserRouter = require('./routes/rest/users');
const restMessageRouter = require('./routes/rest/messages');

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: true })); 
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/rest/simple', restSimpleRouter);
app.use('/rest/users', restUserRouter);
app.use('/rest/messages', restMessageRouter);

module.exports = app;
