@echo off
echo Stopping all servers...

taskkill /FI "WINDOWTITLE eq Eureka Server*" /F
taskkill /FI "WINDOWTITLE eq Demo1 Service*" /F
taskkill /FI "WINDOWTITLE eq Demo2 Service*" /F

echo All servers have been stopped. 