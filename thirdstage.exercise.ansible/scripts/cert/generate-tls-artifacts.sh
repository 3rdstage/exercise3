#! /bin/bash

# References
#   https://help.ubuntu.com/community/OpenSSL
#   https://www.openssl.org/docs/man1.1.0/apps/openssl-req.html

readonly script_dir=$(cd `dirname $0` && pwd)

cd ${script_dir}

# Generate key and CSR for 