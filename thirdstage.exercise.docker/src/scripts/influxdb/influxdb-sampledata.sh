#!/bin/bash

# Downloading sample data file from web site
# The file contains more than 76,000 lines
# For more, refer https://docs.influxdata.com/influxdb/v1.2/query_language/data_download/

if [ ! -f ./NOAA_data.txt ]; then
   echo "Downloading sample data from remote web server. This may take a few minutes."
   curl https://s3.amazonaws.com/noaa.water-database/NOAA_data.txt -o NOAA_data.txt
   sed -i '1,8 d' NOAA_data.txt
fi
