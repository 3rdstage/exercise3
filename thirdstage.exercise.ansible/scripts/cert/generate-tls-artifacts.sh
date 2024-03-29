#! /bin/bash

# References
#   https://help.ubuntu.com/community/OpenSSL
#   https://www.openssl.org/docs/man1.1.0/apps/openssl-req.html

# TODO(Canceled) Make configuration file and output file are read from parameters
# TODO Warn if OpenSSL 1.1 or more is available or not
# TODO Check whether or not the previous files exist
# TODO Move back to the initial directory
# TODO(Done) Notify the full paths of created files
# TODO(Done) Try to pipelining 'openssl req' and 'openssl x509' not to write-down CSR file 
# TODO Add help 

# Check 'openSSL' is installed and available or not
readonly openssl_ver=`openssl version 2> /dev/null`

if [ $? -ne 0 ]; then
  echo "OpenSSL is not installed or 'openssl' is not in the PATH."
  echo "Check whether OpenSSL is installed or not, execute 'dpkg -l | grep -w openssl'." 
  echo "To install OpenSSL, execute 'sudo apt-get install openssl'."
  exit 101
fi

options=$(getopt -o hs:f: --long "subj:,filename:,help" --name 'generate-tls-artifacts-options' -- "$@");

if [ $? -ne 0 ]; then
  echo "Unable to parse command line, For help, try '-h' option."
  echo ""
  exit 300
fi

eval set -- "$options"

declare filename='test-tls'  # only file name part without extension or directory
declare subj=
while true; do
  case "$1" in
    -h | --help )
      echo "Show help"
      exit 0
      shift ;;
    -s | --subj )
      if [ -z "$2" ]; then
        echo "-s or --subj option requires argument like '-s \"/C=ZZ/ST=Unknown/L=Unknown/O=Unknown/OU=Unknown/CN=Unknown\"'."
        exit 301
      else
        subj=${2## }
        subj=${subj%% }
      fi
      shift 2 ;;
    -f | --filename )
      if [ -z "$2" ]; then
        echo "-f or --filename option requires argument like '-s test-tls-server'."
        exit 302
      else
        filename=$2
      fi
      shift 2 ;;
    -- ) shift; break ;;
   esac
done

# echo 'subj='${subj}
# echo 'filename='${filename}

if [ -z "${subj}" ]; then
  subj="/C=ZZ/ST=Unknown/L=Unknown/O=Unknown/OU=Unknown/CN=Unknown"
  echo "No subject (identity for the generated key and certifiate) is specified."
  echo "Default subject '/C=ZZ/ST=Unknown/L=Unknown/O=Unknown/OU=Unknown/CN=Unknown' will be used."
  echo "To specify subejct use -s or --subj option. For more, refer help using -h option."
  echo ""
fi

# Validate subject format
if [[ ! ${subj} =~ ^(/C=[^/=]+|/ST=[^/=]+|/L=[^/=]+|/O=[^/=]+|/OU=[^/=]+|/CN=[^/=]+)+$ ]]; then
  echo ""
  echo "Specified subject(via -s or --subj option) '${subj}' has wrong format."
  echo "Subject is expected to be in '/C=contry code/ST=state/L=city/O=company/OU=department/CN=common name' format"
  echo "where C: Country, ST: State or Province, L: Locality, O: Organization, OU: organizational unit, CN: Common Name"
  echo ""  
  echo "  Example : '/C=KR/ST=Gyeonggi-do/L=Sungnam/O=SK C&C/OU=Solution Lab/CN=Test TLS Server 1'"
  echo ""
  exit 301
fi

readonly init_dir=$(pwd)
readonly script_dir=$(cd `dirname $0` && pwd)
cd ${script_dir}

# Check previously generated key/cert files
if [[ -f "${filename}.key" || -f "${filename}.key" ]]; then
  echo ""
  echo "Previously generated key/cert files with SAME name already exists"
  echo ""
  echo "  key file: '${filename}.key'"
  echo "  certificate file: '${filename}.crt'"
  echo ""
  echo "Rename the above files or specify another name with '-f' or '--filename' option"
  echo ""
  exit 302
fi
  
echo ""
echo "Generating private key and X.509 certificate, using"
echo ""
echo "  - Subject : ${subj}"
echo "  - Filename: ${filename}"
echo ""

# Build command
declare command="openssl req \
  -newkey rsa \
  -keyout ${filename}.key -keyform PEM \
  -nodes -sha512 \
  -config test-tls.cnf \
  -subj \"${subj}\" \
  -outform PEM | openssl x509 -req \
  -extfile test-tls.cnf -extensions x509 \
  -days 7300 -sha512 \
  -CA test-ca.crt -CAkey test-ca.key -CAcreateserial \
  -inform PEM \
  -out ${filename}.crt -outform PEM"


# Generate key and certificate
# echo ${command}
eval ${command}

if [ $? -eq 0 ]; then
  echo ""
  echo "Successfully generated key file and certificate file."
  echo "These files can be used only for TLS servers or clients."
  echo "These files are only for test or PoC purpose. NEVER use these files for production system."
  echo ""   
  echo "  key file: '${filename}.key'"
  echo "  certificate file: '${filename}.crt'"
  echo ""
  echo "To review the details of generated certificate. Try the following command"
  echo ""
  echo "  'openssl x509 -in ${filename}.crt -text -purpose -noout'"
  echo ""
else
  echo ""
  echo "Fail to generate key and certificate files." 
fi
  
