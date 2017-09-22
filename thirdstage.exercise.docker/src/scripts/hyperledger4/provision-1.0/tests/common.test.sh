#! /bin/bash

. $(cd `dirname $0` && pwd)/../tasks/common.sh

eval_json_path_or_default "param1" #illegal call

if [ $? -eq 0 ]; then
  echo "Unexpected result."
fi

json_str='{id="100", name="apple", color="red"}'
path='.color'
default='blue'

color=`eval_json_path_or_default $json_str $path $default`

echo $color