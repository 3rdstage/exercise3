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
