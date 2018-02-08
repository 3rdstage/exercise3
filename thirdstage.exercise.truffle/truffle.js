module.exports = {
    networks: {
      development: {
        host: "127.0.0.1",
        port: 8545,
        network_id: 31,
        from: "d4535798e632789fbe5867e84ed4b9af1bbfed38",
        gasPrice: 0,
        gas: 0x2fefd8
      }
    },

    solc: {
      optimizer: {
        enabled: true,
        runs: 200
      }
    }


};
