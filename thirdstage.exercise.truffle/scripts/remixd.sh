#! /bin/bash

readonly base_dir=$(cd `dirname $0` && cd .. && pwd)

remixd -s $base_dir --remix-ide chrome-extension://ckdanghfoahkcgkdkpnbgkolhdajlkjj
