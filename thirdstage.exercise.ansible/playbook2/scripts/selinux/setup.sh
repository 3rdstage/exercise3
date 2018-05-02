#! /bin/bash

readonly policy_name=default

# Change SELiux permissive mode
sudo setenforce 0

# Add a new SELinux user named 'quorum_u'
sudo semanage user -a -L s0 -r "SystemLow-SystemHigh" -R user_r quorum_u
  
# TODO Check 'sudo semanage user -l' include a row starting with 'quorum_u'

# Assign the Linux user to 'quorum_u' 
if [ `sudo semanage login -l | grep "^administrator" | wc -l` -eq 0 ]; then
  sudo semanage login -a -s quorum_u administrator
else
  sudo semanage login -m -s quorum_u administrator
fi

# TODO Check 'sudo semanage login -l' include a row starting with 'administrator'
  
# Update the contexts of administrator's home directory
sudo restorecon -RF /home/administrator/


# Add a general user to test the SELinux policy
sudo useradd -g users -m -s /bin/bash tom # Consider adding -N option (https://linux.die.net/man/8/useradd)
sudo usermod -a -G `groups administrator | sed -e 's/.*:\s*\(\w.*\w\)\s*/\1/' | sed -e 's/\s/,/g'` tom
sudo passwd tom
# TODO Add necessary groups to tom

# Remove 'unconfined_r:unconfined_t' from 'contexts/default_contexts' file after backup

# Comments out all contexts in 'contexts/users/unconfined_u' file.

# TODO Change SELinux enforcing mode for both current session and configuration
