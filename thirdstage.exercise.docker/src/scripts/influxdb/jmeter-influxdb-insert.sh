#!/bin/bash

readonly script_dir=$(cd `dirname $0` && pwd)

# TODO How to specify the thread group to execute.

jmeter -n -t Insert Test ${script_dir}/influxdb-test.jmx -l ${script_dir}/jmeter-influxdb-insert.jtl
