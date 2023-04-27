
const path = require('path');

// https://webpack.js.org/configuration/
// https://github.com/grafana/k6-template-typescript/blob/main/webpack.config.js

module.exports = {
  mode: 'production',
  entry: {
    "ethers-1" : './src/ethers-1.js',
    "ethers-2" : './src/ethers-2.js'
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    libraryTarget: 'commonjs',
    filename: '[name].js'
  },
  resolve: {
    extensions: ['.ts', '.js']
  },
  module: {
    rules: [{
      test: /\.js$/,
      use: 'babel-loader',
      //exclude: /node_modules/,
    }]
  },
  target: 'web',
  externals: /k6(\/.*)?/, // /^(k6|https?\:\/\/)(\/.*)?/,
  devtool: "source-map",
  stats: {
    colors: true
  },
  optimization: {
    minimize: false
  },
  performance: {
    maxAssetSize: 1500000,
    maxEntrypointSize: 1500000
  }
};