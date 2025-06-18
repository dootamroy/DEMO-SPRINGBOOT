@echo off
echo Building Demo1 Service...
cd demo1
call mvn clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo Failed to build Demo1 Service
    exit /b %ERRORLEVEL%
)
cd ..

echo Building Demo2 Service...
cd demo2
call mvn clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo Failed to build Demo2 Service
    exit /b %ERRORLEVEL%
)
cd ..

echo.
echo Build completed successfully!
echo.
echo JAR files are located at:
echo - demo1/target/demo1-0.0.1-SNAPSHOT.jar
echo - demo2/target/demo2-0.0.1-SNAPSHOT.jar
echo.
echo To run the services, use:
echo java -jar demo1/target/demo1-0.0.1-SNAPSHOT.jar
echo java -jar demo2/target/demo2-0.0.1-SNAPSHOT.jar 