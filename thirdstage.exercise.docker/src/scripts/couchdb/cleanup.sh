#!/bin/bash

# Remove large files that doesn't to be shared such as test data and result data.


readonly script_dir=$(cd `dirname $0` && pwd)

rm -f ${script_dir}/1987.csv
rm -f ${script_dir}/cpuprofile.png