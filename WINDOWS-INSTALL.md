# Lab Gamblers - Instalación Windows

## ⚡ Instalación Express (2 minutos)

### 1. Verificar Windows
Presiona `Win + R`, escribe `winver`, Enter.
- ✅ Windows 10/11 **Pro/Enterprise/Education**
- ❌ Windows 10/11 Home (no compatible)

### 2. Instalar Docker Desktop
1. Descargar: https://docs.docker.com/desktop/setup/install/windows-install/
2. **IMPORTANTE**: Durante instalación → **DESMARCAR** "Use WSL 2 instead of Hyper-V"
3. Reiniciar PC si se solicita

### 3. Ejecutar aplicación
```cmd
git clone https://github.com/TheEgea/Lab_Gamblers.git
cd Lab_Gamblers
start.bat
```

### 4. Acceder
- **Frontend**: http://localhost:5173
- **Backend**: http://localhost:8080

---

## 🛠️ Si ya tienes Docker instalado

1. Abrir Docker Desktop
2. Settings → General
3. **DESMARCAR** "Use the WSL 2 based engine"
4. Restart Docker

---

## 🚨 Problemas comunes

| Error | Solución |
|-------|----------|
| "Docker no está instalado" | Instalar Docker Desktop, reiniciar PC |
| "Docker no está corriendo" | Abrir Docker Desktop desde menú inicio |
| "Error Hyper-V" | Windows debe ser Pro/Enterprise/Education |
| "Puerto ocupado" | Cerrar otras apps, o reiniciar PC |
| "WSL error" | Seguir pasos de "Si ya tienes Docker instalado" |

---

## 🎯 Solo eso

El script `start.bat` hace todo automáticamente:
- Verifica requisitos
- Configura variables
- Levanta servicios
- Te da las URLs

**No necesitas WSL, ni configuraciones complejas, ni nada más.**