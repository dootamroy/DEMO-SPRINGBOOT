@echo off
echo Starting Eureka Server...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"

echo Waiting for Eureka Server to start...
timeout /t 10

echo Starting Demo1 Service...
start "Demo1 Service" cmd /k "cd demo1 && mvn spring-boot:run"

echo Starting Demo2 Service...
start "Demo2 Service" cmd /k "cd demo2 && mvn spring-boot:run"

echo All servers are starting...
echo Eureka Server will be available at: http://localhost:8761
echo Demo1 Service will be available at: http://localhost:8081/hello
echo Demo2 Service will be available at: http://localhost:8082/hello 