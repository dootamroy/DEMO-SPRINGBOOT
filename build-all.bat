@echo off
echo Building all Spring Boot microservices...
echo.

echo Starting multi-module build...
call mvn clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo All services built successfully!
    echo ========================================
    echo.
    echo Built modules:
    echo - eureka-server
    echo - demo1
    echo - demo2
    echo.
    echo JAR files are ready in target/ directories
) else (
    echo.
    echo ========================================
    echo Build failed! Check the error messages above.
    echo ========================================
    pause
) 