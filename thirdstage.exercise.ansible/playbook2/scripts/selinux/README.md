## Prerequisite

* Set enforce mode
```bash
$ sudo setenfore 1
```

* Disable MLS
```bash
???
```

* Disallow unknown permission
```bash
$ sudo sed 's/^\s*handle-unknown\s*=\s*(.*)$/handle-unknown = deny/' /etc/selinux/semanage.conf
```

## Strategy

### Goal

* Prevent login as a ```root``` user even using ```ssh```.

* Prevent all Linux users from using ```su```.

* Prevent all Linux users from logging-on the system not using ```ssh```. 

* Prevent ```sshd``` from using the default port.

* Prevent Linux users except the quorum admin from logging-on the system using ```ssh```.
