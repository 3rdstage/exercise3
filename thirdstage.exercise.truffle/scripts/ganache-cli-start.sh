#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
readonly data_dir=${script_dir}/data/ethereum/ganache

rm -Rf "${data_dir}"
mkdir -p "${data_dir}"

# https://github.com/trufflesuite/ganache-cli#using-ganache-cli
# private key candidate
#   - b92c249c190dd162da9d28bf3e78a730f39444a9c8ead96d6b5616ec0f25b370
ganache-cli --networkId 37 \
            --port 8555 \
            --gasPrice 20000000000 \
            --gasLimit 90000 \
            --deterministic \
            --accounts 3 \
            --secure --unlock 0 --unlock 1 --unlock 2 \
            --defaultBalanceEther 1000000 \
            --blocktime 0 \
            --db "${data_dir}"
