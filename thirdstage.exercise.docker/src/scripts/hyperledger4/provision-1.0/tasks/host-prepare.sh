#! /bin/bash

# @UnderConstruction

# Check sudoer configuration
# TODO Check OS
# TODO Update apt index
# TODO Check Golang
# TODO Check Docker and Docker-Compse
# TODO Check Node.js and npm
# TODO Check GNU parallel
# TODO Check dumpe2fs
# Optimize Disk IO

### Check sudoer configuration
if [ $(sudo ls / &> /dev/null; echo $?) -ne 0 ]; then
  echo "Current user is NOT 'sudoer'." # TODO Update message
  exit 1
fi

if [ $(sudo cat /etc/sudoers | grep -E "^`whoami`\s*ALL=NOPASSWD" | wc -l) -lt 1 ]; then
  echo "Current user is 'sudoer' but is NOT allowed to bypass password check."
  echo "Check '/etc/sudoers' file."
  exit 1
fi

### Check OS
readonly distro=`sudo cat /etc/*-release | grep DISTRIB_ID | sed 's/^DISTRIB_ID=//'`
if [ ${distro} != "Ubuntu" ]; then
  echo "Unsupported OS : ${distro}"
  echo "Currently only Ubuntu is supported."
  exit 1
fi

readonly os_ver=`sudo cat /etc/*-release | grep DISTRIB_RELEASE | sed 's/^DISTRIB_RELEASE=//'`
if [ ${os_ver} != "16.04" ]; then
  echo "Unsupported Ubuntu version : ${os_ver}"
  echo "Currently only Ubuntu 16.04 is supported."
  exit 1
fi


### Optimize disk I/O
readonly disk_name=`sudo lsblk -r -o NAME,MOUNTPOINT,PKNAME | awk '{ if($2 == "/") print $3 }'`

## Optimize ext4 filesystem
# TODO Apply the following lines
#        - sudo tune2fs -o journal_data_writeback /dev/xvda2
#        - sudo tune2fs -f -O ^has_journal /dev/xvda2
# TODO Externalize 'ext4_opts'
# TODO Add 'journal_data_writeback' related option
readonly ext4_opts="rw,noatime,delalloc,barrier=0"
readonly is_disk_journaled=`sudo dumpe2fs /dev/${disk_name} -h | grep has_has_journal | wc -l`

if [ `grep -E '^\S+\s+/\s+ext4.*' /etc/fstab | wc -l` -ne 1 ]; then
  echo "Unexpected filesystem type for '/' which is expected to be 'ext4'."
  exit 1
else
   if [ ! -f /etc/fstab.old ]; then
     sudo cp /etc/fstab /etc/fstab.old
     echo "Copied current 'etc/fstab' to 'etc/fstab.old'."
   fi
   sudo sed -r -i 's/^(\S+\s+\/\s+ext4\s+)\S+(.*)/\1'${ext4_opts}'\2/' /etc/fstab
fi

## Optimize virtual memory configuration
# TODO Externalize kernel parameter values
declare -Ar vm_params=( # kernel parameters for virtual memory
  [swappiness]=60 # 60(default) or 1(recommended)
  [dirty_ratio]=20 # 20(default) or 10(recommended)
  [dirty_background_ratio]=10 # 10(default) or 5(recommended)
  [min_free_kbytes]=67584 # 67584(default) or 262144(256MB, recommended)
)

for param in "${!vm_params[@]}"; do
  echo "Updating 'vm.${param}' to '${vm_params[${param}]}'."
  sudo sysctl -wq vm.${param}=${vm_params[${param}]}

  if [ `grep -E "^\\s*vm.${param}\\s*=\\s*${vm_params[${param}]}.*" /etc/sysctl.conf | wc -l` -ne 1 ]; then
    sudo sed -r -i 's/^\s*(vm\.'"${param}"'.*)/#\1/g' /etc/sysctl.conf
    sudo sh -c "echo 'vm.${param}=${vm_params[${param}]}' >> /etc/sysctl.conf"
  fi
done

## Verify 'vm.swappiness' of docker containers
# TODO Verifi other parameters also
readonly containers=`docker ps --format 'table {{.Names}}' | grep -E '^(kafka|orderer|couch|peer|prometheus|grafana).*'`
for container in ${containers[@]}; do
  val=`docker exec ${container} cat /proc/sys/vm/swappiness`
  if [ "${val}" != "${vm_params[swappiness]}" ]; then
    echo "Unexpected value of vm.swappiness for '${container}' docker container. ${vm_params[swappiness]} is expected, but ${val} is specified."
    exit 1
  else
    echo "Successfully configured 'vm.swappiness=${vm_params[swappiness]}' for '${container}' docker container."
  fi
done

## Optimize block device
# TODO Externalize queue parameter values
declare -Ar queue_params=( #kernel parameters for block device
  [nr_requests]=128 #queue/nr_requests, 128(default)
  [read_ahead_kb]=128 #queue/read_ahead_kb, 128(default) or 4096
)

for param in "${!queue_params[@]}"; do
  echo "Updating 'queue/${param}' to '${queue_params[${param}]}'."
  # NOTE For 'nr_requests', following line causes error on CloudZ. It may be restricted by SoftLayer.
  sudo sh -c "echo ${queue_params[${param}]} > /sys/block/${disk_name}/queue/${param}"

  cnt=`grep -E "^\\s*echo\\s*${queue_params[${param}]}\\s*>\\s*/sys/block/${disk_name}/queue/${param}.*" /etc/rc.local | wc -l`
  if [ ${cnt} -ne 1 ]; then
    sudo sed -r -i 's/^\s*(echo\s+\S+\s+>\s+\/sys\/block\/'"${disk_name}"'\/queue\/'"${param}"'.*)/#\1/g' /etc/rc.local
    sudo sh -c "echo 'echo ${queue_params[${param}]} > /sys/block/${disk_name}/queue/${param}' >> /etc/rc.local"
  fi
done
sudo sed -r -i 's/^\s*exit\s+0.*//' /etc/rc.local
sudo sh -c 'echo "exit 0" >> /etc/rc.local'



