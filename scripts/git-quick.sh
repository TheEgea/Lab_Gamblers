#!/usr/bin/env bash
set -euo pipefail

# Uso:
#   scripts/git-quick.sh "mensaje de commit"
# Hace:
#   - Detecta la rama actual
#   - Añade todos los cambios (tracked + untracked)
#   - Si no hay cambios, sale informando
#   - Commit con el mensaje pasado
#   - Push (si no hay upstream configurado, hace push -u origin HEAD)

msg="${1:-}"
if [[ -z "${msg}" ]]; then
  echo "Error: debes pasar el mensaje de commit"
  echo "Ejemplo: scripts/git-quick.sh \"feat: añadir login (#14)\""
  exit 1
fi

# Asegúrate de estar en un repo git
git rev-parse --is-inside-work-tree >/dev/null 2>&1 || {
  echo "Error: aquí no hay un repositorio git"; exit 1;
}

branch="$(git rev-parse --abbrev-ref HEAD)"
echo "Rama actual: ${branch}"

# Añade todo
git add -A

# Si no hay nada staged, salir
if git diff --cached --quiet; then
  echo "No hay cambios para commitear."
  exit 0
fi

git commit -m "${msg}"

# Detecta si existe upstream
if git rev-parse --abbrev-ref --symbolic-full-name "@{u}" >/dev/null 2>&1; then
  echo "Upstream detectado. Haciendo push..."
  git push
else
  echo "No hay upstream. Haciendo push con -u origin ${branch}..."
  git push -u origin "${branch}"
fi