module.exports = {
    networks: {
      development: {
        host: "127.0.0.1",
        port: @port@,
        network_id: @networkid@,
        from: @from@,
        gas: 0x2fefd8
      }
    }
};