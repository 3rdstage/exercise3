#! /bin/bash

# References
#   https://help.ubuntu.com/community/OpenSSL
#   https://www.openssl.org/docs/man1.1.0/apps/openssl-req.html

readonly init_dir=$(pwd)
readonly script_dir=$(cd `dirname $0` && pwd)
readonly file_name_only='tls-server'

cd ${script_dir}

# TODO Make configuration file and output file are read from parameters
# TODO Warn if OpenSSL 1.1 or more is available or not
# TODO Check whether or not the previous files exist
# TODO Move back to the initial directory
# TODO(Done) Notify the full paths of created files
# TODO(Done) Try to piplining 'openssl req' and 'openssl x509' not to write-down CSR file 

# Generate key and certificate
openssl req \
  -config sample-tls.cnf \
  -newkey rsa \
  -keyout ${file_name_only}.key -keyform PEM \
  -outform PEM | openssl x509 -req \
  -extfile sample-tls.cnf -extensions x509 \
  -days 7300 -sha512 \
  -CA test-ca.crt -CAkey test-ca.key -CAcreateserial \
  -inform PEM \
  -out ${file_name_only}.crt -outform PEM

# Display the contents of the generated certificate
openssl x509 -in ${file_name_only}.crt -text -purpose -noout

if [ $? -eq 0 ]; then
  echo ""
  echo "Key and certificate files for test or PoC are generated successfully."
  echo "  key file: '${script_dir}/${file_name_only}.key'"
  echo "  certiciate file: '${script_dir}/${file_name_only}.crt'"
  echo ""
  echo "You can try 'openssl x509 -in ${file_name_only}.crt -text -purpose -noout' to confirme the details of generated certificate."
  echo ""
fi
  