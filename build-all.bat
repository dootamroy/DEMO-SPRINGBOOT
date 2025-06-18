@echo off
echo Building Eureka Server...
cd eureka-server
call mvn clean package -DskipTests
cd ..

echo Building Demo1 Service...
cd demo1
call mvn clean package -DskipTests
cd ..

echo Building Demo2 Service...
cd demo2
call mvn clean package -DskipTests
cd ..

echo All services built successfully! 