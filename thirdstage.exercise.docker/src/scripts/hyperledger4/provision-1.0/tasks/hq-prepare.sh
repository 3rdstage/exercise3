#! /bin/bash

## Check whether the headerquarter host prepared (required softwares are installed) or not
## and make it prepared, if necessary.

# TODO libraries which may be necessary
#  - net-tools (nestat)


# Install 'jq', if not installed yet
#   - https://stedolan.github.io/jq/
#   - https://github.com/stedolan/jq
if [ $(sudo dpkg -l | awk '{print $2}' | grep ^jq$ | wc -l) -eq 0 ]; then
  echo "Installing 'jq'. For more on 'jshon' refer https://stedolan.github.io/jq/"
  sudo apt install -y jq

  if [ $? -ne 0 ]; then
    echo "Fail to install 'jq'."
    #exit 1
  fi

  echo "Successfully installed 'jq'."
else
  echo "Found 'jq' already installed."
fi

# Install 'GNU Parallel', if not installed yet
#   - https://www.gnu.org/software/parallel/
if [ $(sudo dpkg -l | awk '{print $2}' | grep ^parallel$ | wc -l) -eq 0 ]; then
  echo "Installing 'GNU Parallel'. For more on 'GNU Parallel' refer https://www.gnu.org/software/parallel/"
  sudo apt install -y parallel

  if [ $? -ne 0 ]; then
    echo "Fail to install 'GNU Parallel'."
    #exit 1
  fi

  echo "Successfully installed 'GNU Parallel'."
else
  echo "Found 'GNU Parallel' already installed."
fi
