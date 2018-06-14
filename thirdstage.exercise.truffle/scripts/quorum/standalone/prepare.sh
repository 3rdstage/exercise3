#! /bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)
source "${script_dir}/config.sh"

# TODO Check OS and ...
# Currently working correctly only on Ubuntu 16.04

if [ ! -d ~/git/mago/mago.fork.jpmorganchase.quorum ]; then
  echo "Git clone is expected to be done before this procedures"
  exit 1
fi

cd ~/git/mago/mago.fork.jpmorganchase.quorum
git checkout ${quorum_src[branch]}
# make all

sudo cp build/bin/geth /usr/local/bin/geth
sudo cp build/bin/bootnode /usr/local/bin/bootnode

# Install Constellation
cd "${HOME}"
if [ ! -d Downloads ]; then mkdir Downloads; fi

if [ ! -f "Downloads/${constellation[download]/*\//}" ]; then
  curl -L "${constellation[download]}" -o "Downloads/${constellation[download]/*\//}"
fi

# TODO If there's already constellation binary with same version in /usr/local/bin, skip the following
sudo tar -xzf "Downloads/${constellation[download]/*\//}" -C /usr/local/bin --strip-components=1

# TODO Check '/usr/local/bin' contains 'geth', 'bootnode', and 'constellation-node'
