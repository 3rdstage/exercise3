#! /bin/bash

# https://hyperledger-fabric.readthedocs.io/en/latest/Setup/ca-setup.html#install

# Usage:
#  fabric-ca-server start [flags]
#
# Global Flags:
#      --address string                         Listening address of fabric-ca-server (default "0.0.0.0")
#  -b, --boot string                            The user:pass for bootstrap admin which is required to build default config file
#      --ca.certfile string                     PEM-encoded CA certificate file (default "ca-cert.pem")
#      --ca.chainfile string                    PEM-encoded CA chain file (default "ca-chain.pem")
#      --ca.keyfile string                      PEM-encoded CA key file (default "ca-key.pem")
#  -n, --ca.name string                         Certificate Authority name
#      --cacount int                            Number of non-default CA instances
#      --cafiles stringSlice                    CA configuration files
#  -c, --config string                          Configuration file (default "fabric-ca-server-config.yaml")
#      --csr.cn string                          The common name field of the certificate signing request to a parent fabric-ca-server
#      --csr.hosts stringSlice                  A list of space-separated host names in a certificate signing request to a parent fabric-ca-server
#      --db.datasource string                   Data source which is database specific (default "fabric-ca-server.db")
#      --db.tls.certfiles stringSlice           PEM-encoded list of trusted certificate files
#      --db.tls.client.certfile string          PEM-encoded certificate file when mutual authenticate is enabled
#      --db.tls.client.keyfile string           PEM-encoded key file when mutual authentication is enabled
#      --db.type string                         Type of database; one of: sqlite3, postgres, mysql (default "sqlite3")
#  -d, --debug                                  Enable debug level logging
#      --ldap.enabled                           Enable the LDAP client for authentication and attributes
#      --ldap.groupfilter string                The LDAP group filter for a single affiliation group (default "(memberUid=%s)")
#      --ldap.tls.certfiles stringSlice         PEM-encoded list of trusted certificate files
#      --ldap.tls.client.certfile string        PEM-encoded certificate file when mutual authenticate is enabled
#      --ldap.tls.client.keyfile string         PEM-encoded key file when mutual authentication is enabled
#      --ldap.url string                        LDAP client URL of form ldap://adminDN:adminPassword@host[:port]/base
#      --ldap.userfilter string                 The LDAP user filter to use when searching for users (default "(uid=%s)")
#      --parentserver.caname string             Name of the CA to connect to on fabric-ca-serve
#  -u, --parentserver.url string                URL of the parent fabric-ca-server (e.g. http://<username>:<password>@<address>:<port)
#  -p, --port int                               Listening port of fabric-ca-server (default 7054)
#      --registry.maxenrollments int            Maximum number of enrollments; valid if LDAP not enabled
#      --tls.certfile string                    PEM-encoded TLS certificate file for server's listening port (default "ca-cert.pem")
#      --tls.clientauth.certfiles stringSlice   PEM-encoded list of trusted certificate files
#      --tls.clientauth.type string             Policy the server will follow for TLS Client Authentication. (default "noclientcert")
#      --tls.enabled                            Enable TLS on the listening port
#      --tls.keyfile string                     PEM-encoded TLS key for server's listening port (default "ca-key.pem")

server_home_base=~/var/lib/fabric-ca-server
port=7054
admin_passwd=admin1234
refresh=   # never initilize into false to be used in if statement


usage(){
  echo ""
  echo "Usage: ${0##*/} -n name [-r] [-p port]"
  echo ""
  echo "Start Docker container for Fabric CA server"
  echo ""
  echo "Options: "
  echo "  -n name   Server name"
  echo "  -p port   Listening - default : $port"
  echo "  -r        Remove data in host file system made by previous Docker containers"
}

while getopts ":n:p:rh" opt; do
  case $opt in
    n)
      server_name=${OPTARG%% };
      export FABRIC_CA_SERVER_HOME=$server_home_base/$server_name;
      ;;
    p)
      port=${OPTARG%% }
      ;;
    r)
      refresh=true;
      ;;
    h)
      usage
      exit 0
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "See '${0##*/} -h'." >&2
      exit 1
      ;;
    :)
      echo "Missing option argument: -$OPTARG requires an argument." >&2
      echo "See '${0##*/} -h'." >&2
      exit 1
      ;;
  esac
done

if [ -z $server_name ]; then
  echo "Missing mandatory option: -n name." >&2
  echo "See '${0##*/} -h'." >&2
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
  if [ -d $FABRIC_CA_SERVER_HOME ]; then
    if [ -d $server_home_base/archived/$server_name ]; then
      rm -Rf $server_home_base/archived/$server_name
    fi
    mkdir $server_home_base/archived 2>/dev/null
    mv -f $FABRIC_CA_SERVER_HOME $server_home_base/archived/$server_name
    echo "Moved '$FABRIC_CA_SERVER_HOME' to '$server_home_base/archived/$server_name'"
  fi
fi

if [ ! -d $FABRIC_CA_SERVER_HOME ]; then
  mkdir -p $FABRIC_CA_SERVER_HOME

  # 'fabric-ca-server init' doesn't affect the generated configuration file ('fabric-ca-server-config.yaml') which is not expected behavior
  # So, there's no reason to exectue 'fabric-ca-server init' before 'fabric-ca-server start'

  # $GOPATH/bin/fabric-ca-server init -b admin:$admin_passwd \
  # -p $port \
  # --ca.name $server_name &>$FABRIC_CA_SERVER_HOME/fabric-ca.log
fi

$GOPATH/bin/fabric-ca-server start -b admin:$admin_passwd \
--debug \
--port $port \
--registry.maxenrollments 0 \
--ca.name $server_name &>>$FABRIC_CA_SERVER_HOME/fabric-ca.log & \

if [ $? -eq 0 ]; then
  echo ""
  echo "Fabric CA server named '$server_name' listening $port port started successfully."
  echo "Files for configuration and signing : '$FABRIC_CA_SERVER_HOME'"
  echo "Log file : '$FABRIC_CA_SERVER_HOME/fabric-ca.log'"
  echo ""
fi


