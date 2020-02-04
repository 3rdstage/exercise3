#! /bin/bash


readonly script_dir=$(cd `dirname -- $0` && pwd)

cd "${script_dir}"
readonly conf_file='ganache-cli.properties'

readonly eth_host=`cat $conf_file | grep -E "^ethereum\.host\s*=" | sed -E 's/ethereum\.host\s*=\s*(\S*)\s*/\1/'`
readonly eth_port=`cat $conf_file | grep -E "^ethereum\.port\s*=" | sed -E 's/ethereum\.port\s*=\s*(\S*)\s*/\1/'`
readonly aleshio_port=`cat $conf_file | grep -E "^alethio\.port\s*=" | sed -E 's/alethio\.port\s*=\s*(\S*)\s*/\1/'`

# echo $eth_host
# echo $eth_port
# echo $aleshio_port

# NOTE!! For Windows Toolbox, the address is already in use for port forwarding from the host to docker machine which is Virtual Box guest machine
#
# Check whether the address is alreasy in use or not
#if [ `netstat -anp tcp | awk '$4 == "LISTENING" {print $2}' | grep -E "^($ethdlc_host|0.0.0.0):$aleshio_port$" | wc -l` -gt 0 ]; then
#  readonly pid=`netstat -anop tcp | awk '$4 == "LISTENING" {print $2 " " $5}' | grep -E "^($aleshio_port|0.0.0.0):$eth_port\s" | head -n 1 | awk '{print $2}'`
#  echo "The address '$eth_host:$aleshio_port' for Alehio lite explorer is already in use by the process of which PID is $pid."
#  echo "Fail to start ganache-cli."
#  exit 500
#fi

# TODO Execute 'docker ps' to check whether the previous container is running or not

docker run -id --rm -p $aleshio_port:80 -e APP_NODE_URL="http://$eth_host:$eth_port" alethio/ethereum-lite-explorer:latest

# If you want run the docker in 'host' network mode, execute the following instead
# docker run -id --rm --network host -e APP_NODE_URL="http://127.0.0.1:8545" alethio/ethereum-lite-explorer:latest

if [ $? -eq 0 ]; then
  echo ""
  echo "The Alethio lite explorer has started as a docker container."
  echo "Access 'http://$eth_host:$aleshio_port' to use lite explorer."
  echo ""
  echo "If you are using Docker Toolbox in Windows."
  echo "Port forwarding from host address of '$eth_host:$aleshio_port' to guest address of '0.0.0.0:$aleshio_port' should be added to 'default' machine of VirtualBox before."
fi
