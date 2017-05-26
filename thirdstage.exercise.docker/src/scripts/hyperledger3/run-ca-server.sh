#! /bin/bash

# References
#  * https://hyperledger-fabric.readthedocs.io/en/latest/Setup/ca-setup.html#fabric-ca-server-s-configuration-file-format
#  * https://hyperledger-fabric.readthedocs.io/en/latest/Setup/ca-setup.html#configuration-settings-precedence

PORT=7054 // exposed port for Fabric CA server

usage(){
  echo ""
  echo "Usage: ${0##*/} -n name [-r] [-p port]"
  echo ""
  echo "Start Docker container for Fabric CA server"
  echo ""
  echo "Options: "
  echo "  -n name   Docker container name"
  echo "  -p port   Exposed port for Fabric CA server - default : $PORT"
  echo "  -r        Remove data in host file system made by previous Docker containers"
}

while getopts ":n:p:rh" OPT; do
  case $OPT in
    n)
      #echo "-n option for docker container name is set to $NAME."
      NAME=${OPTARG%% }
      ;;
    p)
      PORT=${OPTARG%% }
      ;;
    r)
      #echo "-r option for refresh is set and all pervious data in host will be removed before starting the docker container."
      REFRESH=true;
      ;;
    h)
      usage
      exit 0
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "See 'run-ca-server.sh -h'." >&2
      exit 1
      ;;
    :)
      echo "Missing option argument: -$OPTARG requires an argument." >&2
      echo "See 'run-ca-server.sh -h'." >&2
      exit 1
      ;;
  esac
done

if [ -z $NAME ] ; then
  echo "Missing mandatory option: -n name." >&2
  echo "See 'run-ca-server.sh -h'." >&2
  exit 1
fi

if [ $(docker ps -qaf "name=^/$NAME$" | wc -l) -ne 0 ]; then
  echo "The docker container named $NAME already exists. Check 'docker ps -a'" >&2
  exit 1
fi

if [ $PORT -lt 1 ]; then
  echo "Iellgal option argument: -p $PORT, port number should be positive" >&2
  exit 1
fi

exit

if [ $REFRESH ]; then
  CONFIG_BASEDIR_HOST=~/docker/etc/hyperledger/fabric-ca-server
  if [ -d $CONFIG_BASEDIR_HOST/$NAME ]; then
    if [ -d $CONFIG_BASEDIR_HOST/archived/$NAME ]; then
      rm -Rf $CONFIG_BASEDIR_HOST/archived/$NAME
    fi
    mkdir $CONFIG_BASEDIR_HOST/archived 2>/dev/null
    mv -f $CONFIG_BASEDIR_HOST/$NAME $CONFIG_BASEDIR_HOST/archived/$NAME
    #rm -Rf $CONFIG_BASEDIR_HOST/$NAME
  fi
fi

docker run -itd --name $NAME \
-e FABRIC_CA_SERVER_HOME=/etc/hyperledger/fabric-ca-server \
-e FABRIC_CA_SERVER_PORT=7054 \
-e FABRIC_CA_SERVER_DEBUG=${FABRIC_CA_SERVER_DEBUG:-false} \
-e FABRIC_CA_SERVER_TLS_ENABLED=${FABRIC_CA_SERVER_TLS_ENABLED:-false} \
-e FABRIC_CA_SERVER_CA_NAME=$NAME \
-e FABRIC_CA_SERVER_CA_KEYFILE=$NAME-key.pem \
-e FABRIC_CA_SERVER_CA_CERTFILE=$NAME-cert.pem \
-e FABRIC_CA_SERVER_REGISTRY_IDENTITIES_NAME=admin \
-e FABRIC_CA_SERVER_REGISTRY_IDENTITIES_PASS=admin1234 \
-e FABRIC_CA_SERVER_DB_TLS_ENABLED=false \
-e FABRIC_CA_SERVER_LDAP_ENABLED=false \
-v ~/docker/etc/hyperledger/fabric-ca-server/$NAME:/etc/hyperledger/fabric-ca-server \
-p $PORT:7054
hyperledger/fabric-ca:${FABRIC_CA_VERSION:-x86_64-1.0.0-alpha2}

