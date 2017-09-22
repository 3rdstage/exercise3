#! /bin/bash

# References
#   -

readonly base_dir=$(cd `dirname $0` && pwd)/..
readonly config_file=${base_dir}/config.json

. ${base_dir}/tasks/hq-prepare.sh

# @TODO Warn that the CouchDB launcher scripts under 'generated/bin' directory would be overwritten or removed.
# @TODO Rename or move the old launcher scripts.

# Remove previous scripts
rm -f ${base_dir}/generated/bin/couch-run*
if [ $? -ne 0 ]; then
  echo "Fail to remove previously generated CouchDB launch scripts."
  exit 1
fi

readonly host_arch=`jq -r '."host-arch"' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse host architecture from configuration file at ${config_file}."
  echo "Check and correct 'host-arch' in the configuration."
  exit 1
else
  echo "Host architecture : ${host_arch}"
fi

readonly fabric_ver=`jq -r '."fabric-version"' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse Fabric version from configuration file at ${config_file}."
  echo "Check and correct 'fabric-ver' in the configuration."
  exit 1
else
  echo "Fabric version : ${fabric_ver}"
fi

readonly host_user=`jq -r '."host-account".username' ${config_file}`
if [ $? -ne 0 ]; then
  echo "Fail to parse host user from configuration file at ${config_file}."
  echo "Check and correct 'host-account' in the configuration."
  exit 1
else
  echo "Host user : ${host_user}"
fi

# TODO Find the query to exclude 'null'
readonly couchs=`jq -rc '.peers | .[] | .couchdb' ${config_file}`
if [[ -z ${couchs} ]]; then
  echo "CouchDB instances are not defined or defined illegally in ${config_file}."
  echo "Check and correct the configuration."
  exit 1
else
  echo "Found CouchDB configurations : ${couchs}"
  echo "Generating scripts to launch CouchDB instances."
fi
readonly couch_names=`echo ${couchs} | jq -rc '.name'`

# Parse default CouchDB configuration

# Generate launch script for each CouchDB instance
for couch in ${couchs[@]}; do
  name=`echo ${couch} | jq -r '.name'`
  echo ${name}

  if [ -f ${base_dir}/generated/bin/couch-run-${name}-${host_id}.sh ]; then
    echo "Succefully generated CouchDB launch script for '${name}'."
    cat ${base_dir}/generated/bin/couch-run-${name}-${host_id}.sh
  else
    echo "Fail to generate CouchDB launch script for '${name}' into 'generated/bin/couch-run-${name}-${host_id}'."
    #exit 1
  fi
done