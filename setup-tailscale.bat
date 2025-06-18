@echo off
echo Setting up Tailscale for Docker...

REM Create tailscale directory if it doesn't exist
if not exist tailscale mkdir tailscale

REM Check if .env file exists
if not exist .env (
    echo Creating .env file...
    echo TAILSCALE_AUTH_KEY=tskey-auth-xxxxxxxxxxxxxxxxxxxx > .env
    echo Please update the TAILSCALE_AUTH_KEY in .env with your actual Tailscale auth key
)

echo.
echo To get your Tailscale auth key:
echo 1. Go to https://login.tailscale.com/admin/authkeys
echo 2. Create a new auth key
echo 3. Copy the key and update it in the .env file
echo.
echo After updating the auth key, run:
echo docker-compose up
echo. 