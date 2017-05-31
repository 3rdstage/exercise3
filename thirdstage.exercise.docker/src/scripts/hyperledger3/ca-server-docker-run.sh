#! /bin/bash

# References
#  * https://hyperledger-fabric.readthedocs.io/en/latest/Setup/ca-setup.html#fabric-ca-server-s-configuration-file-format
#  * https://hyperledger-fabric.readthedocs.io/en/latest/Setup/ca-setup.html#configuration-settings-precedence

port=7054 #exposed port for Fabric CA server

usage(){
  echo ""
  echo "Usage: ${0##*/} -n name [-r] [-p port]"
  echo ""
  echo "Start Docker container for Fabric CA server"
  echo ""
  echo "Options: "
  echo "  -n name   Docker container name"
  echo "  -p port   Exposed port for Fabric CA server - default : $port"
  echo "  -r        Remove data in host file system made by previous Docker containers"
}

while getopts ":n:p:rh" opt; do
  case $opt in
    n)
      #echo "-n option for docker container name is set to $name."
      name=${optarg%% }
      ;;
    p)
      port=${optarg%% }
      ;;
    r)
      #echo "-r option for refresh is set and all pervious data in host will be removed before starting the docker container."
      refresh=true;
      ;;
    h)
      usage
      exit 0
      ;;
    \?)
      echo "Invalid option: -$optarg" >&2
      echo "See 'run-ca-server.sh -h'." >&2
      exit 1
      ;;
    :)
      echo "Missing option argument: -$optarg requires an argument." >&2
      echo "See 'run-ca-server.sh -h'." >&2
      exit 1
      ;;
  esac
done

if [ -z $name ] ; then
  echo "Missing mandatory option: -n name." >&2
  echo "See 'run-ca-server.sh -h'." >&2
  exit 1
fi

if [ $(docker ps -qaf "name=^/$name$" | wc -l) -ne 0 ]; then
  echo "The docker container named $name already exists. Check 'docker ps -a'" >&2
  exit 1
fi

if [ $port -lt 1 ]; then
  echo "Iellgal option argument: -p $port, port number should be positive" >&2
  exit 1
fi

if [ $(netstat -ano | grep $port | wc -l) -ne 0 ]; then
  echo "The specified port $port is already in use. Check 'netstat -ano | grep $port'"
  exit 1
fi

if [ $refresh ]; then
  config_basedir_host=~/docker/etc/hyperledger/fabric-ca-server
  if [ -d $config_basedir_host/$name ]; then
    if [ -d $config_basedir_host/archived/$name ]; then
      sudo rm -Rf $config_basedir_host/archived/$name
    fi
    sudo mkdir $config_basedir_host/archived 2>/dev/null
    sudo mv -f $config_basedir_host/$name $config_basedir_host/archived/$name
    #rm -Rf $config_basedir_host/$name
  fi
fi

docker run -itd \
--name $name \
--hostname lego.$name \
-e FABRIC_CA_SERVER_HOME=/etc/hyperledger/fabric-ca-server \
-e FABRIC_CA_SERVER_PORT=7054 \
-e FABRIC_CA_SERVER_DEBUG=${FABRIC_CA_SERVER_DEBUG:-false} \
-e FABRIC_CA_SERVER_TLS_ENABLED=${FABRIC_CA_SERVER_TLS_ENABLED:-false} \
-e FABRIC_CA_SERVER_CA_NAME=$name \
-e FABRIC_CA_SERVER_CA_KEYFILE=$name-key.pem \
-e FABRIC_CA_SERVER_CA_CERTFILE=$name-cert.pem \
-e FABRIC_CA_SERVER_CA_CHAINFILE=$name-chain.pem \
-e FABRIC_CA_SERVER_REGISTRY_MAXENROLLMENTS=0 \
-e FABRIC_CA_SERVER_REGISTRY_IDENTITIES_NAME=admin \
-e FABRIC_CA_SERVER_REGISTRY_IDENTITIES_PASS=admin1234 \
-e FABRIC_CA_SERVER_DB_TLS_ENABLED=false \
-e FABRIC_CA_SERVER_LDAP_ENABLED=false \
-e FABRIC_CA_SERVER_CSR_CN=lego.$name \
-e FABRIC_CA_SERVER_CSR_NAMES_C=KR \
-e FABRIC_CA_SERVER_CSR_NAMES_ST=Kyunggi-do \
-e FABRIC_CA_SERVER_CSR_NAMES_L=Sungnam-si \
-e FABRIC_CA_SERVER_CSR_NAMES_O=DT \
-e FABRIC_CA_SERVER_CSR_NAMES_OU=Solution-Lab \
-v ~/docker/etc/hyperledger/fabric-ca-server/$name:/etc/hyperledger/fabric-ca-server \
-p $port:7054 \
hyperledger/fabric-ca:${FABRIC_CA_VERSION:-x86_64-1.0.0-alpha2} \
fabric-ca-server start -b admin:admin1234 \
--ca.certfile=$name-cert.pem --ca.keyfile=$name-key.pem



