require("babel-polyfill");
require("babel-register")({
  "presets": ["env"],
  "plugins": ["syntax-async-functions","transform-regenerator"]
});

module.exports = {
    networks: {
      development: {
        host: "192.168.56.101",
        port: 8545,
        network_id: 1991,
        from: "e38e22817778c0ea8e422eebe0ad924df50ad239",
        gasPrice: 0,
        gas: 0x2fefd8
      },
      ganache: {
        host: "127.0.0.1",
        port: 8555,
        network_id: 37,
        from: "e38e22817778c0ea8e422eebe0ad924df50ad239",
        gasPrice: 20000000000,
        gas: 90000
      }
    },

    solc: {
      optimizer: {
        enabled: true,
        runs: 200
      }
    }


};
