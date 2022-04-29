@ECHO OFF

CD /D %~dp0

SET JAVA_HOME=c:\bin\jdk-18.0.1\
SET PATH=c:\bin\jdk-18.0.1\bin\;%PATH%

START .\bin\idea64.exe

EXIT
