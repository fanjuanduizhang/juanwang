@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul 2>&1

:: ============================================================
:: SurveyKing 一键启动脚本
:: ============================================================

:: 获取脚本目录（短路径，消除括号/空格导致的语法错误）
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%.") do set "SCRIPT_DIR=%%~sI\"

:: Java 路径（优先使用同目录下的便携 JDK，否则用系统 JAVA_HOME）
set "PORTABLE_JDK=%SCRIPT_DIR%jdk-17.0.12"
if exist "%PORTABLE_JDK%\bin\java.exe" (
    set "JAVA_EXE=%PORTABLE_JDK%\bin\java.exe"
) else if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_EXE=java"
)

:: 项目路径
set "SERVER_DIR=%SCRIPT_DIR%server"
set "JAR_PATH=%SERVER_DIR%\api\build\libs\surveyking-v1.12.0.jar"
set "PORT=7007"

:: 数据库默认配置
set "DB_HOST=localhost"
set "DB_PORT=3306"
set "DB_NAME=surveyking"
set "DB_USER=root"
set "DB_PASS=123456"

title SurveyKing 启动器

echo ========================================
echo        SurveyKing 一键启动
echo ========================================
echo.

:: ---- [1] 检查 Java ----
echo [1/6] 检查 Java 环境...
"%JAVA_EXE%" -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未找到 Java！
    echo 请将 jdk-17.0.12 文件夹放到脚本同目录，或安装 Java 17 并配置 JAVA_HOME。
    pause
    exit /b 1
)
echo [OK] Java 就绪
echo.

:: ---- [2] 检查 MySQL 服务 ----
echo [2/6] 检查 MySQL 服务...
set "MYSQL_SERVICE="
for %%s in (MySQL MySQL80 MySQL57 MySQL84 MariaDB) do (
    sc query "%%s" >nul 2>&1
    if not errorlevel 1 (
        set "MYSQL_SERVICE=%%s"
        goto FOUND_MYSQL
    )
)

:FOUND_MYSQL
if not defined MYSQL_SERVICE (
    echo [错误] 未找到 MySQL 服务！
    echo 请先安装 MySQL 5.7 / 8.0 并确保服务名为 MySQL、MySQL80 或 MySQL57。
    pause
    exit /b 1
)
echo [OK] 找到 MySQL 服务：%MYSQL_SERVICE%
echo.

:: ---- [3] 启动 MySQL ----
echo [3/6] 确保 MySQL 正在运行...
sc query "%MYSQL_SERVICE%" | findstr "RUNNING" >nul 2>&1
if errorlevel 1 (
    echo 正在启动 MySQL 服务...
    net start "%MYSQL_SERVICE%" >nul 2>&1
    if errorlevel 1 (
        echo [错误] 启动 MySQL 失败，请以管理员身份运行本脚本。
        pause
        exit /b 1
    )
)
echo [OK] MySQL 运行中
echo.

:: ---- [4] 输入数据库密码 ----
echo [4/6] 输入数据库连接信息
echo 当前默认：用户名=%DB_USER%，密码=%DB_PASS%
echo （直接按 Enter 使用默认值）
echo.
set /p "INPUT_USER=MySQL 用户名 [%DB_USER%]: "
if defined INPUT_USER set "DB_USER=%INPUT_USER%"

:INPUT_PASS
set "INPUT_PASS="
set /p "INPUT_PASS=MySQL 密码 [%DB_PASS%]: "
if defined INPUT_PASS set "DB_PASS=%INPUT_PASS%"

:: ---- [5] 测试数据库连接 ----
echo.
echo [5/6] 测试数据库连接...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "SELECT 1" >nul 2>&1
if errorlevel 1 (
    echo [错误] 数据库连接失败！密码可能不正确，请重新输入。
    goto INPUT_PASS
)
echo [OK] 数据库连接成功

:: 确保数据库存在
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" >nul 2>&1
echo [OK] 数据库 %DB_NAME% 已就绪
echo.

:: ---- [6] 检查 / 构建 JAR ----
echo [6/6] 检查应用程序包...
if exist "%JAR_PATH%" (
    echo 发现已有 JAR 文件。
    echo   Y = 重新构建（代码有更新时需要）
    echo   N = 直接启动（使用现有版本）
    set "REBUILD="
    set /p "REBUILD=是否重新构建？(Y/N) [N]: "
    if /i "!REBUILD!"=="Y" goto DO_BUILD
    echo [OK] 使用现有 JAR
    goto START_APP
)

:DO_BUILD
echo 正在构建，请稍候（约 2-3 分钟）...
cd /d "%SERVER_DIR%"
call gradlew.bat :api:bootJar -Pdev --no-daemon
if errorlevel 1 (
    echo [错误] 构建失败！请查看上方错误信息。
    pause
    exit /b 1
)
echo [OK] 构建成功

:START_APP
echo.
echo ========================================
echo   SurveyKing 正在启动...
echo ========================================
echo   访问地址：http://localhost:%PORT%
echo   管理后台：http://localhost:%PORT%/admin
echo   默认账号：admin / 123456
echo   按 Ctrl+C 停止服务
echo ========================================
echo.

cd /d "%SCRIPT_DIR%"
"%JAVA_EXE%" ^
    -Xmx768m -Xms384m -XX:+UseG1GC ^
    -Dfile.encoding=UTF-8 ^
    -jar "%JAR_PATH%" ^
    --spring.profiles.active=dev ^
    --server.port=%PORT% ^
    --spring.datasource.username=%DB_USER% ^
    --spring.datasource.password=%DB_PASS% ^
    --spring.datasource.url=jdbc:mysql://%DB_HOST%:%DB_PORT%/%DB_NAME%?useUnicode=true^&characterEncoding=utf-8^&serverTimezone=Asia/Shanghai^&useSSL=false^&allowPublicKeyRetrieval=true

echo.
echo SurveyKing 已停止。
pause
exit /b 0
