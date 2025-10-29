@echo off
setlocal enabledelayedexpansion

set COMPOSE_FILE=docker-compose.dev.yml

echo ========================================
echo   Lab Gamblers - Startup Script
echo ========================================
echo.

REM Verificar Docker
echo [1/5] Verificando Docker...
where docker >nul 2>&1
if !errorlevel! neq 0 (
    echo ERROR: Docker no esta instalado o no esta en PATH
    echo        Instala Docker Desktop desde: https://docs.docker.com/desktop/
    pause
    exit /b 1
)

docker compose version >nul 2>&1
if !errorlevel! neq 0 (
    echo ERROR: Docker Compose v2 no encontrado
    echo        Actualiza Docker Desktop a la ultima version
    pause
    exit /b 1
)

REM Verificar que Docker este corriendo
echo [2/5] Verificando que Docker este corriendo...
docker ps >nul 2>&1
if !errorlevel! neq 0 (
    echo ERROR: Docker no esta corriendo
    echo        Inicia Docker Desktop desde el menu inicio
    pause
    exit /b 1
)

REM Crear .env si no existe
echo [3/5] Configurando variables de entorno...
if not exist ".env" (
    if exist ".env.example" (
        copy .env.example .env >nul
        echo ✓ Creado .env desde .env.example
    ) else (
        echo DB_NAME=protube> .env
        echo DB_USER=protube>> .env
        echo DB_PASSWORD=protube>> .env
        echo ENV_PROTUBE_GOOGLE_CLIENT_ID=>> .env
        echo ENV_PROTUBE_GOOGLE_CLIENT_SECRET=>> .env
        echo ✓ Creado .env con valores por defecto
    )
) else (
    echo ✓ Archivo .env ya existe
)

REM Limpiar contenedores previos
echo [4/5] Limpiando contenedores previos...
docker compose -f "%COMPOSE_FILE%" down >nul 2>&1

REM Instalar dependencias del frontend
echo [5/5] Preparando e iniciando servicios...
echo     - Levantando base de datos...
docker compose -f "%COMPOSE_FILE%" up -d db
if !errorlevel! neq 0 (
    echo ERROR: Fallo al iniciar la base de datos
    pause
    exit /b 1
)

echo     - Levantando backend...
docker compose -f "%COMPOSE_FILE%" up -d backend
if !errorlevel! neq 0 (
    echo ERROR: Fallo al iniciar el backend
    pause
    exit /b 1
)

echo     - Instalando dependencias del frontend...
docker compose -f "%COMPOSE_FILE%" run --rm frontend sh -c "npm ci || npm install" >nul 2>&1

echo     - Levantando frontend...
docker compose -f "%COMPOSE_FILE%" up -d frontend
if !errorlevel! neq 0 (
    echo ERROR: Fallo al iniciar el frontend
    pause
    exit /b 1
)

REM Esperar a que los servicios esten listos
echo.
echo Esperando que los servicios esten listos...
timeout /t 10 /nobreak >nul

echo.
echo ========================================
echo      ✓ Lab Gamblers RUNNING!
echo ========================================
echo.
echo URLs disponibles:
echo   Frontend:  http://localhost:5173
echo   Backend:   http://localhost:8080
echo   Database:  localhost:5432
echo.
echo Credenciales DB: usuario=protube, password=protube
echo.
echo Para parar: docker compose -f docker-compose.dev.yml down
echo Para logs:  docker compose -f docker-compose.dev.yml logs -f
echo.
pause