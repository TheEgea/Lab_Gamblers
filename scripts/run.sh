#!/usr/bin/env bash
set -euo pipefail

CMD="${1:-help}"
MODE="${2:-dev}"

COMPOSE_FILE="docker-compose.dev.yml"

compose() {
  docker compose -f "$COMPOSE_FILE" "$@"
}

export_envs_for_mode() {
  case "$MODE" in
    prod)
      export SPRING_PROFILES_ACTIVE=prod
      export SPRING_JPA_HIBERNATE_DDL_AUTO=validate
      ;;
    dev|*)
      export SPRING_PROFILES_ACTIVE=dev
      export SPRING_JPA_HIBERNATE_DDL_AUTO=update
      ;;
  esac
}

case "$CMD" in
  start)
    export_envs_for_mode
    compose up -d db
    compose up -d backend
    compose up -d frontend
    ;;
  stop)
    compose down
    ;;
  restart)
    "$0" stop
    "$0" start "$MODE"
    ;;
  logs)
    SVC="${3:-backend}"
    compose logs -f "$SVC"
    ;;
  ps)
    compose ps
    ;;
  *)
    echo "Uso: $0 {start|stop|restart|logs|ps} [dev|prod]"
    echo "Ejemplos:"
    echo "  $0 start dev"
    echo "  $0 start prod"
    echo "  $0 logs backend"
    exit 1
    ;;
esac
