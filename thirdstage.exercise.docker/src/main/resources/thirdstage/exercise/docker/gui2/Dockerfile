# This file from http://stackoverflow.com/questions/16296753/can-you-run-gui-apps-in-a-docker-container

from ubuntu:14.04

run echo "deb http://archive.ubuntu.com/ubuntu precise main universe" > /etc/apt/sources.list

run apt-get update && apt-get install -y x11vnc xvfb nautilus firefox
run mkdir ~/.vnc

run x11vnc -storepasswd 1234 ~/.vnc/passwd
run nautilus .

# Run the following at the docker terminal to start this image and x11vnc
# '$ docker build -t gui2 .
# '$ docker run -p 5901:5900 --name gui2 gui2 x11vnc -forever -usepw -create -o /var/log/x11vnc.log -v'
