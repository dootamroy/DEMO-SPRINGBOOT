@echo off
echo Docker Volume Management Script

:menu
echo.
echo 1. List all volumes
echo 2. Remove all volumes
echo 3. Exit
echo.
set /p choice="Enter your choice (1-3): "

if "%choice%"=="1" (
    echo.
    echo Listing all volumes...
    docker volume ls
    goto menu
)

if "%choice%"=="2" (
    echo.
    echo Removing all volumes...
    docker volume rm demo1_logs demo2_logs
    echo Volumes removed successfully!
    goto menu
)

if "%choice%"=="3" (
    exit /b 0
)

echo Invalid choice!
goto menu 