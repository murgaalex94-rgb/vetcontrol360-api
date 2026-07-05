@echo off
echo ==============================================
echo    INICIANDO BACKEND DE VETCARE
echo ==============================================
echo.

cd /d "%~dp0"
node mock-server.js

pause