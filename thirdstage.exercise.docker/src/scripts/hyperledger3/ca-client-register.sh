#! /bin/bash

# https://hyperledger-fabric.readthedocs.io/en/latest/Setup/ca-setup.html#client


client_home_base=~/var/lib/fabric-ca-client
server_name=ca0
server_url=http://localhost:7054
admin_passwd=admin1234


export FABRIC_CA_CLIENT_HOME=$client_home_base/$server_name/admin

#rm -rf $FABRIC_CA_CLIENT_HOME

if [ ! -d $FABRIC_CA_CLIENT_HOME ]; then
   mkdir -p $FABRIC_CA_CLIENT_HOME
fi

#fabric-ca-client enroll -u http://admin:$admin_passwd@localhost:7054 --caname ca0

declare -a users
users[0]='user1 user1!@#$ user false'
users[1]='user2 user1!@#$ user false'
users[2]='user3 user1!@#$ user false'
users[3]='user4 user1!@#$ user false'
users[4]='user5 user1!@#$ user false'

for user in "${users[@]}"; do
  echo $user
done

