#! /bin/bash

# Install Ansible : http://docs.ansible.com/ansible/2.4/intro_installation.html
# sudo apt-get update
# sudo apt-get install software-properties-common
# sudo apt-add-repository ppa:ansible/ansible
# sudo apt-get update
# sudo apt-get install ansible


# Install Ansible Container : http://docs.ansible.com/ansible-container/installation.html
sudo pip install ansible-container[docker,k8s]