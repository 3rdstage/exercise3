

const Chance = require('chance');
const chance = new Chance();

module.exports = {
  logRandomWord: logRandomWord,
  logHeaders: logHeaders
}

function logRandomWord(requestParams, context, ee, next) {
  console.log(chance.word());
  return next(); // MUST be called for the scenario to continue
}

function logHeaders(requestParams, response, context, ee, next) {
  console.log(response.headers);
  return next(); // MUST be called for the scenario to continue
}