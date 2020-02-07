@ECHO OFF


REM detect the jar
SET JAR=
SET FIND_CMD="dir /s /b | findstr spacetaxi | findstr jar-with-dependencies.jar"
FOR /F "delims=" %%F IN ('%FIND_CMD%') do (
    SET JAR=%%F
    GOTO :RUN
)

:RUN

if "%JAR%"=="" (
  echo No jar found. Make sure that you are in the correct working directory and have built the application first: mvn clean install.
  GOTO :DONE
)

REM execute jar with all parameters supplied
java -jar %JAR% %*
:DONE