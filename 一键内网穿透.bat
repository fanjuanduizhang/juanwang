@echo off
chcp 65001 >nul
title SurveyKing 内网穿透
color 0A

echo ========================================
echo        SurveyKing 内网穿透启动器
echo ========================================
echo.

:: 检查 natapp.exe 是否存在
if not exist "natapp.exe" (
    echo [错误] 未找到 natapp.exe
    echo.
    echo 请先下载 natapp:
    echo   1. 访问 https://natapp.cn
    echo   2. 下载 Windows 客户端
    echo   3. 将 natapp.exe 放入当前目录
    echo.
    pause
    exit /b 1
)

:: 检查端口 7007 是否被占用
netstat -ano | findstr ":7007" >nul
if errorlevel 1 (
    echo [警告] 端口 7007 未被占用
    echo 请确保 SurveyKing 服务已启动
    echo.
)

echo [就绪] natapp 已准备完成
echo.
echo ========================================
echo  内网穿透配置
echo ========================================
echo  本地端口: 7007
echo  隧道类型: Web
echo ========================================
echo.
echo 启动后按 Ctrl+C 可停止穿透
echo.
echo 即将启动...
pause

:: 启动 NATAPP
natapp -authtoken=5af5e19a14fef4ef -log=stdout

pause
