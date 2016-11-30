# This file from http://stackoverflow.com/questions/16296753/can-you-run-gui-apps-in-a-docker-container

from ubuntu:14.04

run echo "deb http://archive.ubuntu.com/ubuntu precise main universe" > /etc/apt/sources.list

run apt-get update && apt-get install -y x11vnc xvfb firefox
run mkdir ~/.vnc

run x11vnc -storepasswd 1234 ~/.vnc/passwd
run bash -c 'echo "firefox" >> /.bashrc'