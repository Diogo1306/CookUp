@echo off
REM ============================================================
REM  CookUp - arranque do ambiente local de desenvolvimento
REM  Antes de correr: abre o XAMPP e liga Apache + MySQL.
REM  A API e' servida pelo Apache a partir do DocumentRoot.
REM ============================================================

echo A arrancar o CookUp...
echo.

start "CookUp Dashboard" cmd /k "cd /d %~dp0dashboard && npm run dev -- --port 5180 --strictPort"
start "CookUp Admin API" cmd /k "cd /d %~dp0firebase-admin && node index.js"

echo.
echo  Dashboard ....... http://localhost:5180/
echo  Admin API ....... http://localhost:4000/
echo.
echo  Lembra-te de ter o XAMPP (Apache + MySQL) ligado.
echo  Entra com a conta admin (admin@gmail.com).
echo.
pause
