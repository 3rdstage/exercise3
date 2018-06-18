#! /bin/bash

# References
#   https://help.ubuntu.com/community/OpenSSL
#   https://www.openssl.org/docs/man1.1.0/apps/openssl-req.html

readonly init_dir=$(pwd)
readonly script_dir=$(cd `dirname $0` && pwd)

cd ${script_dir}

# TODO Check whether or not the previous files exist
# TODO Move back to the initial directory
# TODO Notify the full paths of created files

# Generate key and CSR
openssl req \
  -config sample.cnf \
  -newkey rsa \
  -keyout tls-server.key -keyform PEM \
  -days 7300 -sha512 \
  -out tls-server.csr -outform PEM
  
# Display the contents of CSR


  