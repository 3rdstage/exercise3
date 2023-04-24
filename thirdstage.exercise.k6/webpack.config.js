
const path = require('path');

module.exports = {
  mode: 'production',
  entry: {
    "ethers-2" : './scripts/ethers-2.js'
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    libraryTarget: 'commonjs',
    filename: '[name].bundle.js'
  },
  module: {
    rules: [{test: /\.js$/, use: 'babel-loader'}]
  },
  target: 'web',
  externals: /k6(\/.*)?/

}