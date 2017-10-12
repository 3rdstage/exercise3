#! /bin/bash

. $(cd `dirname $0` && pwd)/../tasks/common.sh

a=`eval_json_path_or_default "param1"` #illegal call
echo $?
echo $a

json_str='{id="100", name="apple", color="red"}'
path='.color'
default='blue'

b=`eval_json_path_or_default "$json_str" "$path" "$default"`
echo $?
echo $b


