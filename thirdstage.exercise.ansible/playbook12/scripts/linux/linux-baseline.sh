#! /bin/bash

# For Ubuntu 16.04
# As 'root' user

declare -r GRP_NM=chainz
declare -r USER_NM=chainz

groupadd $GRP_NM

# https://linux.die.net/man/8/useradd
useradd -g $GRP_NM -G sudo -s /bin/bash -m chainz

passwd $USER_NM

apt-get install python
