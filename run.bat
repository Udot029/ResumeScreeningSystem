@echo off
setlocal
set "JAVA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.2\jbr"
if not exist "%JAVA_HOME%\bin\java.exe" (
  echo Error: Java runtime not found at %JAVA_HOME%\bin\java.exe
  exit /b 1
)
if not exist out (
  echo Output folder not found. Run build.bat first.
  exit /b 1
)
"%JAVA_HOME%\bin\java.exe" -cp out com.screener.Main
if errorlevel 1 (
  echo Application failed to start.
  exit /b 1
)
exit /b 0
