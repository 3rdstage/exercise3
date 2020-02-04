#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
readonly run_dir=$(mkdir -p "${script_dir}/../run/ganache" && cd "${script_dir}/../run/ganache" && pwd)
readonly data_dir=${run_dir}/data

options=$(getopt -o rb --long "refresh,background" --name 'ganache-cli-start-options' -- "$@");

if [ $? -ne 0 ]; then
  command=${0##*/}
  echo "Unable to parse command line, which expect '$command [-r|--refresh] [-b|--background]'."
  echo ""
  exit 400
fi

eval set -- "$options"

declare refreshes=0   #false
declare backgrounds=0   #false
while true; do
  case "$1" in
    -r | --refresh )
      refreshes=1
      shift ;;
    -b | --background )
      backgrounds=1
      shift ;;
    -- ) shift; break ;;
  esac
done

if [ ! -d "${data_dir}" ]; then
  echo "Created data directory on '${data_dir}'"
  mkdir -p "${data_dir}"
fi

if [ $refreshes -eq 1 ]; then
  echo "Removing all current data under '${data_dir}'"
  rm -Rf "${data_dir}"
  sleep 3
  mkdir -p "${data_dir}"
fi

cd "${script_dir}"

readonly eth_ver=`cat ganache-cli.properties | grep -E "^ethereum\.netVersion=" | sed -E 's/ethereum\.netVersion=//'`
readonly eth_host=`cat ganache-cli.properties | grep -E "^ethereum\.host=" | sed -E 's/ethereum\.host=//'`
readonly eth_port=`cat ganache-cli.properties | grep -E "^ethereum\.port=" | sed -E 's/ethereum\.port=//'`
readonly eth_mnemonic=`cat ganache-cli.properties | grep -E "^ethereum\.mnemonic=" | sed -E 's/ethereum\.mnemonic=//'`
readonly eth_gas_price=`cat ganache-cli.properties | grep -E "^ethereum\.gasPrice=" | sed -E 's/ethereum\.gasPrice=//'`
readonly eth_gas_limit=`cat ganache-cli.properties | grep -E "^ethereum\.gasLimit=" | sed -E 's/ethereum\.gasLimit=//'`

# check whether the address is alreasy in use or not
if [ `netstat -anp tcp | awk '$4 == "LISTENING" {print $2}' | grep -E "^($eth_host|0.0.0.0):$eth_port$" | wc -l` -gt 0 ]; then
  readonly pid=`netstat -anop tcp | awk '$4 == "LISTENING" {print $2 " " $5}' | grep -E "^($eth_host|0.0.0.0):$eth_port\s" | head -n 1 | awk '{print $2}'`
  echo "The address '$eth_host:$eth_port' is already in use by the process of which PID is $pid."
  echo "Fail to start ganache-cli."
  exit 500
fi

# echo $eth_ver;
# echo $eth_host;
# echo $eth_port;
# echo $eth_mnemonic;
# echo $eth_gas_price;
# echo $eth_gas_limit;

# Ganache CLI : https://github.com/trufflesuite/ganache-cli#using-ganache-cli
# BIP 32 : https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki
# BIP 39 : https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki
#
# Accounts
#   - 0xC5776C5d4ba76dD38424A160927c6B7054b55edD
#   - 0x99322780C19B664e9902Ff1031549da575De8F3B
#   - 0xf0f0717db9387ea3b095de1ff43786c63dc93e45
# Private keys
#   - 0xbbd0e1d8507416b8c64e88f63b4534969b9d88e4a79ebc67f4abff122f28cfb7
#   - 0xf8c91da1e73f5601a25cbffdac303138ffac30eeeda2680f1853b6ce325ac01b
#   - 0x572775a6686f4b5d3b26c46133e7419e97b88b5ba1db9e0f5d3ff9a109916a47
# Options
#   - gasLimit : The block gas limit (defaults to 0x6691b7)
#   - gasPrice: The price of gas in wei (defaults to 20000000000)

cmd="ganache-cli --networkId $eth_ver \
            --host '$eth_host' \
            --port $eth_port \
            --gasPrice $eth_gas_price \
            --gasLimit $eth_gas_limit \
            --mnemonic '$eth_mnemonic' \
            --accounts 3 \
            --secure --unlock 0 --unlock 1 --unlock 2 \
            --defaultBalanceEther 1000000 \
            --hardfork petersburg \
            --blockTime 0 \
            --verbose \
            --db '${data_dir}' >> '${run_dir}'/ganache.log 2>&1"

if [ $backgrounds -eq 0 ]; then
  echo $cmd
  eval $cmd
else
  cmd=$cmd' &'
  echo $cmd
  eval $cmd

  if [ $? -eq 0 ]; then
    sleep 3
    tail "${run_dir}"/ganache.log -n 50
    echo "The loacal Ganache has started."
    echo "The log file is located at '${run_dir}/ganache.log'."
  fi
fi


