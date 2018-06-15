#! /bin/bash

# References
#   https://help.ubuntu.com/community/OpenSSL
#   https://www.openssl.org/docs/man1.1.0/apps/openssl-req.html

readonly script_dir=$(cd `dirname $0` && pwd)

cd ${script_dir}

# Generate key and self-signed certificate for CA
openssl req \
  -config test-ca.cnf \
  -newkey rsa \
  -keyout test-ca.key -keyform PEM \
  -x509 -days 3650 -sha512 \
  -out test-ca.crt -outform PEM

# Display the contents of CA certificate
openssl x509 -in test-ca.crt -text -noout
   
