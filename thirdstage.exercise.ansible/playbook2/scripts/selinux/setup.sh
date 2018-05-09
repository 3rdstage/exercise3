#! /bin/bash

readonly policy_name=default
readonly quorum_linux_user=administrator  # Linux user, not SELinux user_u
readonly test_linux_user=tom

# Change SELiux permissive mode
sudo setenforce 0

# Add a new SELinux user named 'quorum_u'
sudo semanage user -a -L s0 -r "SystemLow-SystemHigh" -R user_r quorum_u
# sudo semanage user -m -L s0 -r "SystemLow-SystemHigh" -R "staff_r sysadm_r" quorum_u
  
# TODO Check 'sudo semanage user -l' include a row starting with 'quorum_u'

# Assign the Linux user to 'quorum_u' 
if [ `sudo semanage login -l | grep "^administrator" | wc -l` -eq 0 ]; then
  sudo semanage login -a -s quorum_u ${quorum_linux_user}
else 
  sudo semanage login -m -s quorum_u ${quorum_linux_user}
fi

# TODO Check 'sudo semanage login -l' include a row starting with 'administrator'
  
# Update the contexts of Linux user's home directory
sudo restorecon -RF /home/${quorum_linux_user}/

# Add a general user to test the SELinux polcd icy
sudo useradd -g users -m -s /bin/bash ${test_linux_user} # Consider adding -N option (https://linux.die.net/man/8/useradd)
sudo usermod -a -G `groups ${quorum_linux_user} | sed -e 's/.*:\s*\(\w.*\w\)\s*/\1/' | sed -e 's/\s/,/g'` ${test_linux_user}
sudo passwd ${test_linux_user}

# Remove 'unconfined_r:unconfined_t' from 'contexts/default_contexts' file after backup
if [ ! -f "/etc/selinux/${policy_name}/contexts/default_contexts.orig" ]; then
  cp "/etc/selinux/${policy_name}/contexts/default_contexts" "/etc/selinux/${policy_name}/contexts/default_contexts.orig"
fi


# Comments out all contexts in 'contexts/users/unconfined_u' file.

# TODO Change SELinux enforcing mode for both current session and configuration
