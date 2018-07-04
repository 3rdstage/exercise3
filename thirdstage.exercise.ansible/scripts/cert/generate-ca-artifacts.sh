#! /bin/bash

# References
#   https://help.ubuntu.com/community/OpenSSL
#   https://www.openssl.org/docs/man1.1.0/apps/openssl-req.html

readonly script_dir=$(cd `dirname $0` && pwd)

cd ${script_dir}

if [[ -f ${script_dir}/test-ca.key || -f ${script_dir}/test-ca.crt ]]; then
  echo "Previously generated test CA key and cert files (test-ca.key, test-ca,crt) exists."
  echo "To generated new key and cert files, remove those files first and try again."
  echo ""
  exit 101
fi  

# TODO Warn if OpenSSL 1.1 or more is available or not
# TODO Warn and stop if there exist previously generated files

# Generate key and self-signed certificate for CA
openssl req \
  -newkey rsa \
  -keyout test-ca.key -keyform PEM \
  -nodes -sha512 \
  -config test-ca.cnf \
  -x509 -days 3650 \
  -out test-ca.crt -outform PEM

# Modify permissions of generated file
chmod 444 test-ca.{key,crt}

# Display the contents of CA certificate
openssl x509 -in test-ca.crt -text -purpose -noout
   
# TODO(Done) Print out the pull paths for the generated files
if [ $? -eq 0 ]; then
  echo ""
  echo "Key and certificate files for Test CA are generated successfully."
  echo "  key file: '${script_dir}/test-ca.key'"
  echo "  certiciate file: '${script_dir}/test-ca.crt'"
  echo ""
  echo "You can try 'openssl x509 -in test-ca.crt -text -purpose -noout' to confirme the details of generated certificate."
  echo ""
fi



