@echo off
setlocal enabledelayedexpansion

:: 获取脚本目录并转换为短路径（消除括号/空格导致的语法错误）
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%.") do set "SCRIPT_DIR=%%~sI\"
set "JAVA_HOME=%SCRIPT_DIR%jdk-17.0.12"
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
set "SERVER_DIR=%SCRIPT_DIR%server"
set "JAR_PATH=%SERVER_DIR%\api\build\libs\surveyking-v1.12.0.jar"
set "PORT=7007"
set "DB_HOST=localhost"
set "DB_PORT=3306"
set "DB_NAME=surveyking"
set "DB_USER=root"
set "DB_PASS=123456"

title SurveyKing Launcher

echo ========================================
echo     SurveyKing Quick Start
echo ========================================
echo.

echo [1/8] Checking Java Environment...
if not exist "%JAVA_EXE%" (
    echo ERROR: Java not found at %JAVA_EXE%
    echo Please ensure jdk-17.0.12 is in the same folder as this script.
    pause
    exit /b 1
)
echo OK: Java is ready. [%JAVA_EXE%]
echo.

echo [2/8] Checking MySQL Installation...
set "MYSQL_SERVICE="

:: Check for common MySQL service names
for %%s in (MySQL MySQL80 MySQL57 MariaDB) do (
    sc query "%%s" >nul 2>&1
    if not errorlevel 1 (
        set "MYSQL_SERVICE=%%s"
        goto FOUND_SERVICE
    )
)

:FOUND_SERVICE
if defined MYSQL_SERVICE (
    echo OK: Found MySQL service: %MYSQL_SERVICE%
) else (
    echo INFO: MySQL not found. Installing MySQL...
    call :INSTALL_MYSQL
    if errorlevel 1 (
        echo ERROR: Failed to install MySQL.
        pause
        exit /b 1
    )
    echo INFO: MySQL installed with default password: %DB_PASS%
    set "MYSQL_SERVICE=MySQL"
)
echo.

echo [3/8] Starting MySQL Service...
sc query "%MYSQL_SERVICE%" | findstr "RUNNING" >nul 2>&1
if errorlevel 1 (
    echo INFO: Starting MySQL Service: %MYSQL_SERVICE%...
    net start "%MYSQL_SERVICE%" >nul 2>&1
    if errorlevel 1 (
        echo ERROR: Failed to start MySQL!
        echo Try running this script as Administrator.
        pause
        exit /b 1
    )
)
echo OK: MySQL is running.
echo.

echo [4/8] Enter Database Credentials
echo Current default: username=%DB_USER%, password=%DB_PASS%
set /p "DB_USER=Enter MySQL username (default: %DB_USER%): "
if not defined DB_USER set "DB_USER=root"

:INPUT_PASS
set "NEW_PASS="
set /p "NEW_PASS=Enter MySQL password (default: %DB_PASS%): "
if defined NEW_PASS set "DB_PASS=%NEW_PASS%"

echo.
echo [5/8] Testing Database Connection...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "SELECT 1" >nul 2>&1
if errorlevel 1 (
    echo ERROR: Connection failed!
    echo Try resetting your password or use default.
    goto INPUT_PASS
)
echo OK: Connection successful.
echo.

echo [6/8] Configuring MySQL...
call :CONFIGURE_MYSQL
echo OK: MySQL configured.
echo.

echo [7/8] Creating Database...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" >nul 2>&1
echo OK: Database created.
echo.

echo [8/8] Updating Configuration...
powershell -Command "(Get-Content '%SERVER_DIR%\api\src\main\resources\application-dev.yml') -replace 'username:\s*\S+', 'username: %DB_USER%' | Set-Content '%SERVER_DIR%\api\src\main\resources\application-dev.yml'"
powershell -Command "(Get-Content '%SERVER_DIR%\api\src\main\resources\application-dev.yml') -replace 'password:\s*\S*', 'password: %DB_PASS%' | Set-Content '%SERVER_DIR%\api\src\main\resources\application-dev.yml'"
powershell -Command "(Get-Content '%SERVER_DIR%\api\src\main\resources\application-dev.yml') -replace 'url:\s*jdbc:mysql://[^\s]+', 'url: jdbc:mysql://%DB_HOST%:%DB_PORT%/%DB_NAME%?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false' | Set-Content '%SERVER_DIR%\api\src\main\resources\application-dev.yml'"
echo OK: Configuration updated.
echo.

echo ========================================
echo Building and Starting Application...
echo ========================================

if exist "%JAR_PATH%" (
    echo Found existing JAR. Rebuild? (Y/N)
    set "REBUILD="
    set /p "REBUILD=> "
    if /i not "%REBUILD%"=="Y" (
        goto START_APP
    )
)

echo Building project...
cd /d "%SERVER_DIR%"
call gradlew.bat :api:bootJar -Pdev
if errorlevel 1 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)
echo OK: Build successful.

:START_APP
echo.
echo Starting SurveyKing...
echo Port: %PORT%
echo URL: http://localhost:%PORT%
echo Admin: http://localhost:%PORT%/admin
echo Default: admin / 123456
echo.

cd /d "%SCRIPT_DIR%"
"%JAVA_EXE%" -jar "%JAR_PATH%" --spring.profiles.active=dev --server.port=%PORT%

pause
exit /b 0

:: ========================================
:: Install MySQL using Chocolatey
:: ========================================
:INSTALL_MYSQL
echo Checking Chocolatey...
choco --version >nul 2>&1
if errorlevel 1 (
    echo Installing Chocolatey...
    powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
    if errorlevel 1 (
        echo ERROR: Failed to install Chocolatey.
        exit /b 1
    )
    echo OK: Chocolatey installed.
)

echo Installing MySQL...
choco install mysql -y --params="'/password:%DB_PASS%'"
if errorlevel 1 (
    echo ERROR: MySQL installation failed.
    exit /b 1
)
echo OK: MySQL installed.
exit /b 0

:: ========================================
:: Configure MySQL user and permissions
:: ========================================
:CONFIGURE_MYSQL
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "ALTER USER IF EXISTS '%DB_USER%'@'localhost' IDENTIFIED WITH mysql_native_password BY '%DB_PASS%'; FLUSH PRIVILEGES;" >nul 2>&1
if errorlevel 1 (
    echo INFO: Setting initial password...
    mysqladmin -u%DB_USER% password %DB_PASS% >nul 2>&1
    mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "ALTER USER '%DB_USER%'@'localhost' IDENTIFIED WITH mysql_native_password BY '%DB_PASS%'; FLUSH PRIVILEGES;" >nul 2>&1
)
exit /b 0
