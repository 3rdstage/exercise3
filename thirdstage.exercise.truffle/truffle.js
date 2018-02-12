module.exports = {
    networks: {
      development: {
        host: "127.0.0.1",
        port: 8545,
        network_id: 31,
        from: "d4535798e632789fbe5867e84ed4b9af1bbfed38",
        gasPrice: 0,
        gas: 0x2fefd8
      },
      ganache: {
        host: "127.0.0.1",
        port: 8555,
        network_id: 37,
        from: "90f8bf6a479f320ead074411a4b0e7944ea8c9c1",
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
