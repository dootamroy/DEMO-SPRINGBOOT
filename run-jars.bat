@echo off
echo Creating logs directories...
if not exist "demo1\logs" (
    mkdir "demo1\logs"
    echo Created demo1\logs directory
)
if not exist "demo2\logs" (
    mkdir "demo2\logs"
    echo Created demo2\logs directory
)

echo Starting Eureka Server...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"

echo Waiting for Eureka Server to start...
timeout /t 10

echo Starting Demo1 Service...
start "Demo1 Service" cmd /k "cd demo1 && java -jar target/demo1-0.0.1-SNAPSHOT.jar"

echo Starting Demo2 Service...
start "Demo2 Service" cmd /k "cd demo2 && java -jar target/demo2-0.0.1-SNAPSHOT.jar"

echo All servers are starting...
echo Eureka Server will be available at: http://localhost:8761
echo Demo1 Service will be available at: http://localhost:8081/hello
echo Demo2 Service will be available at: http://localhost:8082/hello
echo Logs will be available at:
echo - demo1/logs/demo1.log
echo - demo2/logs/demo2.log

echo.
echo To monitor logs in real-time, use:
echo Get-Content -Path "demo1/logs/demo1.log" -Wait
echo Get-Content -Path "demo2/logs/demo2.log" -Wait 