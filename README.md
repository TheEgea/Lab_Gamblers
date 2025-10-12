## 📁 Estructura del Proyecto
/home/lab/protube/Lab_Gamblers/
├── backend/ # Spring Boot backend
├── frontend/ # React/Vite frontend
├── tooling/ # Herramientas (videoGrabber)
├── docs/ # Documentación
├── resources/ # Recursos del proyecto
└── README.md # Este archivo

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