## 📁 Estructura del Proyecto
/home/lab/protube/Lab_Gamblers/
├── backend/ # Spring Boot backend
├── frontend/ # React/Vite frontend
├── tooling/ # Herramientas (videoGrabber)
├── docs/ # Documentación
├── resources/ # Recursos del proyecto
└── README.md # Este archivo

## 🔐 Autenticación JWT

El backend implementa autenticación JWT. Ver [docs/JWT_LOGIN.md](docs/JWT_LOGIN.md) para detalles completos.

**Usuarios de prueba (dev):**
- `admin` / `admin123` (roles: ADMIN, USER)
- `user` / `user123` (role: USER)

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**Usar el token:**
```bash
curl -H "Authorization: Bearer <token>" \
  http://localhost:8080/api/endpoint
```

## ⚙️ Configuración

```bash
# Configurar SIEMPRE antes de ejecutar el back
export ENV_PROTUBE_STORE_DIR="/home/lab/protube_store"

# Para añadirlo permanentemente al usuario actual
echo 'export ENV_PROTUBE_STORE_DIR="/home/lab/protube_store"' >> ~/.bashrc
source ~/.bashrc
```

## Acceso por red, puertos y CORS (dev)

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
    - Backend: arráncalo desde IntelliJ (ProtubeBackApplication) con ENV_PROTUBE_STORE_DIR configurado.
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

Comandos útiles
```bash
# Frontend (en el servidor)
cd frontend
npm install
npm run dev -- --host

# Backend (en el servidor)
# Ejecutar ProtubeBackApplication en IntelliJ con ENV_PROTUBE_STORE_DIR configurado
```
## Git: cómo commitear y pushear rápido

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
- Primera vez en rama nueva (sin upstream):
```bash
# Crea/activa rama si hace falta
git switch -c feat/jwt-security-13   # o: git checkout -b feat/jwt-security-13

# Añade todo (tracked + untracked), commitea y configura upstream en el primer push
git add -A
git commit -m "feat(security): configurar JWT y SecurityFilterChain (#13)"
git push -u origin $(git rev-parse --abbrev-ref HEAD)
```

- Siguientes commits en la misma rama:
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