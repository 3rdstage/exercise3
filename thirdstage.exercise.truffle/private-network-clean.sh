#! /bin/bash

# Remove to clean the configuration and data for standalone private Ethereum network

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/private-network-config.sh"

if [ `ps | grep geth | wc -l` -gt 0 ]; then
  echo "There seems to be currently running Ethereum node(s)."
  echo "Before removing the data and configuration for the local single Ethereum network, stop it or them."
  exit 1
fi

rm -Rf "${data_dir}"
rm "${script_dir}/genesis.json"