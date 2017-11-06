#!/bin/bash


readonly url_base="https://raw.githubusercontent.com/3rdstage/exercise3/master/thirdstage.exercise.docker/src/scripts/couchdb"
readonly scripts=("couch1-run.sh" "couch2-run.sh" "prepare-test-data.sh")

for f in ${scripts[@]}; do
  curl ${url_base}/${f} -o ${f}
done;
