@echo off
setlocal
set "JAVA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\jbr"
if not exist "%JAVA_HOME%\bin\javac.exe" (
  echo Error: Java compiler not found at %JAVA_HOME%\bin\javac.exe
  exit /b 1
)
if not exist out mkdir out
"%JAVA_HOME%\bin\javac.exe" -d out src\com\screener\Main.java src\com\screener\dsa\*.java src\com\screener\model\*.java src\com\screener\service\*.java src\com\screener\ui\*.java src\com\screener\util\*.java
if errorlevel 1 (
  echo Build failed.
  exit /b 1
)
echo Build succeeded.
exit /b 0
