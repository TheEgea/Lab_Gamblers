#!/usr/bin/env bash
set -euo pipefail

COMPOSE_FILE="docker-compose.dev.yml"

compose() {
  sudo docker compose -f "$COMPOSE_FILE" "$@"
  echo "#lab1234567890"
}

have_cmd() { command -v "$1" >/dev/null 2>&1; }

print_header() {
  echo "========================================"
  echo "$1"
  echo "========================================"
}

ensure_prereqs() {
  if ! have_cmd docker; then
    echo "Error: docker no está instalado o no está en PATH." >&2
    exit 1
  fi
  if ! docker compose version >/dev/null 2>&1; then
    echo "Error: Docker Compose v2 no encontrado." >&2
    exit 1
  fi
  if [ ! -f "$COMPOSE_FILE" ]; then
    echo "Error: no se encuentra $COMPOSE_FILE en $(pwd)" >&2
    exit 1
  fi
}

ensure_env() {
  if [ ! -f ".env" ]; then
    if [ -f ".env.example" ]; then
      cp .env.example .env
      echo "Creado .env desde .env.example"
    else
      cat > .env <<'EOF'
DB_NAME=protube
DB_USER=protube
DB_PASSWORD=protube
ENV_PROTUBE_GOOGLE_CLIENT_ID=
ENV_PROTUBE_GOOGLE_CLIENT_SECRET=
EOF
      echo "Creado .env con valores por defecto (Postgres dev)."
    fi
  fi
}

install_frontend_deps() {
  print_header "Instalando dependencias del frontend (npm ci || npm i)"
  compose run --rm frontend sh -lc "npm ci || npm i"
}

start() {
  ensure_prereqs
  ensure_env

  print_header "Levantando base de datos"
  compose up -d db

  print_header "Levantando backend"
  compose up -d backend

  install_frontend_deps

  print_header "Levantando frontend"
  compose up -d frontend

  print_header "Servicios levantado"
  compose ps

  echo
  echo "URLs:"
  echo "- Backend:  http://localhost:8080"
  echo "- Frontend: http://localhost:5173"
  echo "- Postgres: localhost:5432 (credenciales en .env)"
  echo
}

stop() {
  ensure_prereqs
  print_header "Parando servicios"
  compose down
}

restart() {
  ensure_prereqs
    print_header "Reiniciando backend + frontend"
    # Asegura deps del frontend (npm ci || npm i) antes de levantar
    install_frontend_deps
    compose up -d backend frontend
}

logs() {
  ensure_prereqs
  print_header "Logs (Ctrl+C para salir)"
  compose logs -f "${@:-backend}" || true
}

ps() {
  ensure_prereqs
  compose ps
}

reset() {
  ensure_prereqs
  echo "Esto parará y borrará volúmenes (DB y caches). ¿Continuar? [y/N]"
  read -r ans
  if [[ "${ans:-N}" =~ ^[Yy]$ ]]; then
    print_header "Parando y borrando volúmenes"
    compose down -v
  else
    echo "Cancelado."
  fi
}

usage() {
  cat <<EOF
Uso: $0 <comando>

Comandos:
  start      Levanta db + backend + frontend (instala deps frontend si hace falta)
  stop       Para y desmonta los servicios
  restart    Reinicia backend y frontend
  logs [svc] Muestra logs (por defecto 'backend'). Ej: logs frontend | logs db
  ps         Muestra el estado de los servicios
  reset      Parar y borrar volúmenes (DB y caches) [confirmación]
  help       Muestra esta ayuda
EOF
}

cmd="${1:-help}"
shift || true

case "$cmd" in
  start) start "$@" ;;
  stop) stop "$@" ;;
  restart) restart "$@" ;;
  logs) logs "$@" ;;
  ps) ps "$@" ;;
  reset) reset "$@" ;;
  help|--help|-h) usage ;;
  *) echo "Comando no reconocido: $cmd"; echo; usage; exit 1 ;;
esac