require("babel-polyfill");
require("babel-register")({
  "presets": ["env"],
  "plugins": ["syntax-async-functions","transform-regenerator"]
});

module.exports = {
    // http://truffleframework.com/docs/advanced/configuration
    networks: {
      development: {
        host: "192.168.56.101",
        port: 8545,
        network_id: 1991,
        from: "58de4669321d33f91d05d51d85b0be8dd304b511",
        gasPrice: 0,
        gas: 0x10000000
      }
    },

    solc: {
      optimizer: {
        enabled: true,
        runs: 200
      }
    }
};
