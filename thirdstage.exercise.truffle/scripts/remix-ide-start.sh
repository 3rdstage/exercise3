#! /bin/bash

readonly base_dir=$(cd `dirname $0` && cd .. && pwd)

# the format of remix-ide url : https://..., chrome-extension://...
remix-ide $base_dir
