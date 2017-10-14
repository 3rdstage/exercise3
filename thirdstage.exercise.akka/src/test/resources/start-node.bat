

@ echo off
setLocal enableDelayedExpansion

if not exist "%JAVA_HOME%\bin\javaw.exe" (
  echo.
  echo Fail to run
  echo JDK 1.6 ^(32-bit^) or higher and 'JAVA_HOME' environment variable for it are required.
  goto :END
) else (
  echo JAVA_HOME = !JAVA_HOME!
)

set CLASSPATH=.

set PROJECT_DIR=%~dp0..\..\..
if exist "%PROJECT_DIR%\target" (
   set CLASSPATH=%CLASSPATH%;%PROJECT_DIR%\target\classes;%PROJECT_DIR%\target\test-classes;%PROJECT_DIR%\target\dependencies\*
)

set INSTALLED_HOME=%~dp0..
if exist "%INSTALLED_HOME%\lib" (
   set CLASSPATH=%CLASSPATH%;%INSTALLED_HOME%\lib\*
)
if exist "%INSTALLED_HOME%\conf" (
   set CLASSPATH=%CLASSPATH%;%INSTALLED_HOME%\conf
)

echo CLASSPATH = %CLASSPATH%

if not defined LOG_BASE (
   if exist "%INSTALLED_HOME%\log" (
      set LOG_BASE=%INSTALLED_HOME%\log
   ) else (
      set LOG_BASE=%TEMP%
   )
)
echo LOG_BASE = %LOG_BASE%

set JVM_ARGS=-server -Xms256m -Xmx256m -XX:MaxPermSize=256M
set JVM_ARGS=%JVM_ARGS% -XX:NewRatio=2 -XX:SurvivorRatio=8
set JVM_ARGS=%JVM_ARGS% -Dlog.level.default=DEBUG
:: set JVM_ARGS=%JVM_ARGS% -Dlog.level.root=WARN

set MAIN_CLASS=thirdstage.exercise.akka.wordanalysis.ClusterNodeLauncher

echo ************************************************
echo '"%JAVA_HOME%\bin\java" %JVM_ARGS% -cp "%CLASSPATH%" %MAIN_CLASS% %1 %2 %3'
echo ************************************************

"%JAVA_HOME%\bin\java" %JVM_ARGS% -cp "%CLASSPATH%" %MAIN_CLASS% %1 %2 %3

:END
endLocal
