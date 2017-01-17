
@ echo off
setLocal enableDelayedExpansion


if not exist "%SystemRoot%\System32\drivers\etc\hosts" (
   echo.
   echo Can't find 'hosts' file under '%SystemRoot%\System32\drivers'
   echo Check your computer has installed Microsoft Windows properly.
   goto :END
)

for /f "tokens=*" %%l in (%SystemRoot%\System32\drivers\etc\hosts) do (
   echo %%l

   for /f "tokens=1,2* delims=' '" %%a in ("%%l") do (
      echo %%a ","
   )
)

goto :END

if not exist "%VBOX_MSI_INSTALL_PATH%" (
   if not exist "%VBOX_INSTALL_PATH%" (
      echo.
      echo 'VBOX_INSTALL_PATH' or 'VBOX_MSI_INSTALL_PATH' environment variable is not set which may means VirtualBox is not installed yet.
      echo Check whether VirtualBox is intalled and add 'VBOX_INSTALL_PATH' or 'VBOX_MSI_INSTALL_PATH' properly to run this script.
      goto :END
   )
)

mkdir c:\var

"%VBOX_MSI_INSTALL_PATH%\VBoxManage" sharedfolder add default --name c/var --hostpath c:\var --automount

docker-machine ssh default sudo mkdir /c/var

docker-machine ssh defalut sudo mount -t vboxsf -o uid=1000,gid=50 c/var /c/var

:END
endLocal