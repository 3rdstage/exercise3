
@ echo off
setLocal enableDelayedExpansion

if not exist "%SystemRoot%\System32\drivers\etc\hosts" (
   echo.
   echo Can't find 'hosts' file under '%SystemRoot%\System32\drivers'
   echo Check your computer has installed Microsoft Windows properly.
   goto :END
)

set DOCKERHOST_DEFINED=false
for /f "tokens=*" %%l in (%SystemRoot%\System32\drivers\etc\hosts) do (
   rem echo %%l

   for /f "tokens=1,2" %%a in ("%%l") do (
      if "%%a" neq "#" (
         echo %%a : %%b
         if "%%a" equ "dockerhost" goto :DOCKERHOST_DEFINED
         if "%%b" equ "com.skcc.bigdata00" goto :DOCKERHOST_DEFINED
      )
   )
)

echo 'hosts' file contains no item for dockerhost, so adding '....'


:DOCKERHOST_DEFINED

echo 'dockerhost' is set to '...' in 'hosts' file.

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
   

:: choco -y install xming --version 6.9.0.31

:END
endLocal