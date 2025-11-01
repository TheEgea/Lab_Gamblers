# Lab Gamblers

## 🚀 Quickstart

Prerequisitos
- Docker y Docker Compose instalados
- Node 20 (solo si ejecutas el frontend fuera de Docker)
- (Linux) Configurar el directorio de almacenamiento:
  ```bash
  export ENV_PROTUBE_STORE_DIR="/home/lab/protube_store"
  echo 'export ENV_PROTUBE_STORE_DIR="/home/lab/protube_store"' >> ~/.bashrc
  source ~/.bashrc
  ```

Arrancar todo en desarrollo (perfil dev)
```bash
# da permisos (una vez)
chmod +x scripts/run.sh

# inicia servicios
./scripts/run.sh start dev

# ver logs del backend
./scripts/run.sh logs backend
```

Probar login (elige tu entorno)
- Linux/macOS/Git Bash:
  ```bash
  curl -i -X POST http://localhost:8080/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin123"}'
  ```
- Windows CMD (ojo comillas):
  ```bat
  curl -i -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
  ```
- PowerShell:
  ```powershell
  $body = @{ username = "admin"; password = "admin123" } | ConvertTo-Json
  Invoke-WebRequest -Uri "http://localhost:8080/auth/login" -Method POST -ContentType "application/json" -Body $body
  ```

Esperado: 200 OK, con cabecera Authorization: Bearer <token> y body {"access_token":"<token>"}

---
## 🪟 Para Windows
**Requisitos**: Windows 10/11 Pro/Enterprise + Docker Desktop SIN WSL(no es necesario vaya)
### Instalación rápida:
git clone https://github.com/TheEgea/Lab_Gamblers.git
cd Lab_Gamblers
run-dev.bat

**URLs después del start:**
- Frontend: http://localhost:5173
- Backend: http://localhost:8080

**¿Problemas?** → [Guía Windows (2 min)](WINDOWS-INSTALL.md)


---

## 📁 Estructura del Proyecto
---
```
/home/lab/protube/Lab_Gamblers/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/com/tecnocampus/LS2/protube_back/
│   │   ├── api/             # Controladores REST (AuthController, VideoController, GlobalExceptionHandler)
│   │   ├── application/     # Casos de uso, DTOs y mappers
│   │   │   ├── auth/        # AuthenticationService
│   │   │   ├── user/        # UserService (WIP)
│   │   │   ├── video/       # VideoService (WIP)
│   │   │   └── dto/         # request/, response/, mapper/
│   │   ├── domain/          # Entidades/VOs y puertos (User, Username, Password, Role, TokenService, UserAuthPort, Video…)
│   │   ├── persistence/     # Adaptadores JPA (user/, video/)
│   │   ├── security/        # Configuración JWT y servicios de seguridad
│   │   ├── exception/       # Excepciones de negocio + ErrorResponse
│   │   ├── AppStartupRunner.java
│   │   └── ProtubeBackApplication.java
│   └── pom.xml
├── frontend/                # React/Vite frontend
├── tooling/                 # Herramientas (videoGrabber)
├── docs/                    # Documentación (si aplica)
├── resources/               # Recursos del proyecto
└── README.md

```
---


## 🔐 Autenticación JWT

El backend implementa autenticación JWT. Ver [docs/JWT_LOGIN.md](docs/JWT_LOGIN.md) para detalles completos.

**Usuarios de prueba (dev):**
- `admin` / `admin123` (roles: ADMIN, USER)
- `user` / `user123` (role: USER)

**Login (ejemplos):**
- Linux/macOS/Git Bash:
  ```bash
  curl -i -X POST http://localhost:8080/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin123"}'
  ```
- Windows CMD:
  ```bat
  curl -i -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
  ```
- PowerShell:
  ```powershell
  $body = @{ username = "admin"; password = "admin123" } | ConvertTo-Json
  Invoke-WebRequest -Uri "http://localhost:8080/auth/login" -Method POST -ContentType "application/json" -Body $body
  ```
- Con fichero JSON (evita problemas de comillas):
  ```bash
  echo '{"username":"admin","password":"admin123"}' > login.json
  curl -i -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" --data-binary "@login.json"
  ```

**Usar el token:**
```bash
curl -i -H "Authorization: Bearer <token>" http://localhost:8080/api/endpoint
```
Nota: el token expira; si empiezas a recibir 401 pasado un tiempo, vuelve a loguearte.

### Postman (opcional)
- Method: POST
- URL: http://localhost:8080/auth/login (o IP del servidor si accedes por red)
- Headers: Content-Type: application/json, Accept: application/json
- Body (raw → JSON):
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
- Test para guardar el token en variables:
  ```javascript
  pm.test("status is 200", function () { pm.response.to.have.status(200); });
  const json = pm.response.json();
  const tok = json?.access_token ?? json?.token;
  if (tok) {
    pm.environment.set("access_token", tok);
    pm.environment.set("authHeader", "Bearer " + tok);
  }
  ```
- Luego usa Authorization: {{authHeader}} en endpoints protegidos.

---

## ⚙️ Configuración

```bash
# Configurar SIEMPRE antes de ejecutar el back (Linux)
export ENV_PROTUBE_STORE_DIR="/home/lab/protube_store"

# Para añadirlo permanentemente al usuario actual
echo 'export ENV_PROTUBE_STORE_DIR="/home/lab/protube_store"' >> ~/.bashrc
source ~/.bashrc
```

---

## 🌐 Acceso por red, puertos y CORS (dev)

Resumen de puertos por defecto:
- Frontend (Vite): 5173
- Backend (Spring Boot): 8080
- Base de datos (Postgres opcional): 5432

Frontend: autodetección del host del backend
- El front construye las URLs del backend a partir del host por el que abras el front.
    - Si abres por IP: http://192.168.0.141:5173 → llamará a http://192.168.0.141:8080
    - Si abres por localhost: http://localhost:5173 → llamará a http://localhost:8080
- Overrides opcionales (por si quieres forzar):
    - VITE_API_DOMAIN y VITE_MEDIA_DOMAIN (por ejemplo, http://mi-host:8080)
    - VITE_API_PORT y VITE_MEDIA_PORT (por defecto 8080)
- Implementación: ver frontend/src/utils/Env.ts.

CORS en desarrollo
- El backend en desarrollo debe permitir CORS desde el origen donde sirves el front.
- Verificación rápida (deberías ver Access-Control-Allow-Origin reflejando el Origin pasado):
  ```bash
  curl -i -H "Origin: http://localhost:5173"     http://localhost:8080/api/videos
  curl -i -H "Origin: http://192.168.0.141:5173" http://localhost:8080/api/videos
  ```

Acceso desde otro equipo por IP (en la misma red)
1. En el servidor (192.168.0.141):
    - Backend: arráncalo con Docker (script) o desde IntelliJ (ProtubeBackApplication) con ENV_PROTUBE_STORE_DIR configurado.
    - Frontend: en la carpeta frontend
      ```bash
      npm install
      npm run dev -- --host
      ```
      Nota: --host expone Vite por la IP del servidor.
2. En tu PC (navegador):
    - Frontend: http://192.168.0.141:5173
    - Backend (prueba directa): http://192.168.0.141:8080/api/videos

Acceso por túnel SSH (sin exponer puertos en la LAN)
- Crea el túnel desde tu PC:
  ```bash
  ssh -L 5173:localhost:5173 -L 8080:localhost:8080 lab@192.168.0.141
  ```
- Luego abre en tu PC:
    - Frontend: http://localhost:5173
    - Backend: http://localhost:8080/api/videos

Resolución de problemas
- Brave/Ad-blockers:
    - Si en Chrome funciona y en Brave no, desactiva Shields para el sitio o ponlo en “Standard”.
    - Revisa DevTools > Console por “ERR_BLOCKED_BY_CLIENT” o mensajes CORS.
- Fuerza recarga sin caché (Ctrl+F5) si no ves cambios del front.
- Comprueba firewall/VPN si no puedes acceder a http://<IP>:5173 o :8080 desde tu PC.

---

## 🧰 Run modes: dev and prod with a single compose

Usamos un único Docker Compose y un script para ejecutar en modo desarrollo o producción.

Servicios y puertos
- Backend: http://localhost:8080
- Frontend: http://localhost:5173
- Postgres: localhost:5432 (credenciales en .env / docker-compose.dev.yml)

Uso del script
```bash
# Forma general
./scripts/run.sh {start|stop|restart|logs|ps} [dev|prod]
```

Dev mode (por defecto)
- Profile: `dev`
- JPA DDL: `update`
- Dev seeder: habilitado e idempotente (crea admin/admin123 si no existe)
- Comandos:
  ```bash
  ./scripts/run.sh start dev
  ./scripts/run.sh logs backend
  ./scripts/run.sh ps
  ./scripts/run.sh stop
  ```

Prod mode
- Profile: `prod`
- JPA DDL: `validate` (no cambia el esquema)
- Seeder: deshabilitado (no crea usuarios por defecto)
- Requiere variables:
    - `ENV_PROTUBE_JWT_SECRET` (secreto robusto)
    - `DB_NAME`, `DB_USER`, `DB_PASSWORD` (que correspondan a tu Postgres)
- Comandos:
  ```bash
  export ENV_PROTUBE_JWT_SECRET=change_me
  export DB_NAME=protube
  export DB_USER=protube
  export DB_PASSWORD=protube

  ./scripts/run.sh start prod
  ./scripts/run.sh logs backend
  ```

Prueba de login (dev/prod)
- POST /auth/login espera `{"username":"...","password":"..."}`
- Respuesta esperada:
    - 200 OK
    - Header: `Authorization: Bearer <token>`
    - Body: `{"access_token":"<token>"}`

Troubleshooting
- GET /auth/login:
    - 405 Method Not Allowed → El POST existe y está expuesto (OK)
    - 401 Unauthorized → Revisa la configuración de seguridad (permitAll en /auth/** para el login)
    - 404 Not Found → Path o mapping incorrecto
- “JSON parse error” en logs:
    - En Windows CMD, recuerda escapar comillas del JSON o usa PowerShell/archivo .json
- “Dev user already exists” al arrancar:
    - El `DevUserSeeder` en dev debe ser idempotente (si existe, que “skippee”)
- Forzar rebuild (si notas contenedores “stale”):
  ```bash
  docker compose -f docker-compose.dev.yml up -d --build --force-recreate backend
  ```

---

## 🛠️ Comandos útiles

```bash
# Frontend (en el servidor)
cd frontend
npm install
npm run dev -- --host

# Backend (en el servidor)
# Ejecutar ProtubeBackApplication en IntelliJ con ENV_PROTUBE_STORE_DIR configurado
```

---

## 🧑‍💻 Git: cómo commitear y pushear rápido

Pre-requisitos
- Estar dentro del repo (en la carpeta del proyecto).
- Tener configurado tu nombre y email (solo la primera vez):
  ```bash
  git config user.name "Tu Nombre"
  git config user.email "tuemail@example.com"
  ```

Ver estado y cambios pendientes
```bash
git status
```

Commit y push de TODOS los cambios en la rama actual

Primera vez en rama nueva (sin upstream):
```bash
# Crea/activa rama si hace falta
git switch -c feat/jwt-security-13   # o: git checkout -b feat/jwt-security-13

# Añade todo (tracked + untracked), commitea y configura upstream en el primer push
git add -A
git commit -m "feat(security): configurar JWT y SecurityFilterChain (#13)"
git push -u origin $(git rev-parse --abbrev-ref HEAD)
```

Siguientes commits en la misma rama:
```bash
git add -A
git commit -m "fix(security): ajustes de properties y UTF-8 (#13)"
git push
```

Consejos rápidos
- Ver qué se va a commitear: `git diff --cached`
- Cambiar el último commit (sin modificar mensaje): `git commit --amend --no-edit`
- Cambiar el último commit (editando mensaje): `git commit --amend`
- Si ya hiciste push del último commit y lo enmiendas: `git push --force-with-lease`
- Evita `#` en nombres de ramas (en bash `#` inicia comentarios). Usa `feat/jwt-security-13` en vez de `feat/jwt-security-#13`.