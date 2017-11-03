#!/bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)


# Downloading sample data for performance test from web site
# The file contains more than 76,000 lines
# For more, refer https://docs.influxdata.com/influxdb/v1.2/query_language/data_download/
if [ ! -f ${script_dir}/1987.csv ]; then
   echo "Downloading sample data from remote web server. This may take a few minutes."
   curl http://stat-computing.org/dataexpo/2009/1987.csv.bz2 -o ${script_dir}/1987.csv.bz2
   bzip2 -d ${script_dir}/1987.csv.bz2
fi

if [ `(sudo dpkg -l | awk '{print$2}' | grep ^p7zip-full$ | wc -l)` -eq 0 ]; then
  echo "Installing 'p7zip' to unzip .7z file"
  sudo apt-get -y install p7zip-full
fi


# Downloading sample data for performance test from Stock Exchange site
# The file contains more than 76,000 lines
# For more, refer https://archive.org/details/stackexchange
if [ ! -f ${script_dir}/Posts.xml ]; then
   if [ ! -f ${script_dir}/bitcoin.stackexchange.com.7z ]; then
      echo "Downloading sample data from Stack Exchange(https://archive.org/details/stackexchange). This may take a few minutes."
      curl -L http://archive.org/download/stackexchange/bitcoin.stackexchange.com.7z -o ${script_dir}/bitcoin.stackexchange.com.7z
   fi
   7za e bitcoin.stackexchange.com.7z Posts.xml

   if [ $? -ne 0 ]; then
      echo "Fail to decompress the Post file from 'bitcoin.stackexchange.com.7z'"
   fi
fi




