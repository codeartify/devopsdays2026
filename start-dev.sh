#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOG_DIR="$ROOT_DIR/.dev-logs"

IDENTITY_DIR="$ROOT_DIR/identity"
FITNESS_MANAGEMENT_DIR="$ROOT_DIR/fitness_management_system"

mkdir -p "$LOG_DIR"

identity_pid=""
fitness_management_pid=""

cleanup() {
  echo
  echo "Stopping Spring Boot apps..."

  if [[ -n "$identity_pid" ]] && kill -0 "$identity_pid" 2>/dev/null; then
    kill "$identity_pid" 2>/dev/null || true
  fi

  if [[ -n "$fitness_management_pid" ]] && kill -0 "$fitness_management_pid" 2>/dev/null; then
    kill "$fitness_management_pid" 2>/dev/null || true
  fi

  wait "$identity_pid" "$fitness_management_pid" 2>/dev/null || true
}

trap cleanup EXIT INT TERM

echo "Starting Docker Compose..."
docker compose -f "$ROOT_DIR/docker-compose.yml" up -d

echo "Starting identity on http://localhost:8082..."
(
  cd "$IDENTITY_DIR"
  ./mvnw spring-boot:run
) >"$LOG_DIR/identity.log" 2>&1 &
identity_pid=$!

echo "Starting fitness-management-system on http://localhost:8081..."
(
  cd "$FITNESS_MANAGEMENT_DIR"
  ./mvnw spring-boot:run
) >"$LOG_DIR/fitness-management-system.log" 2>&1 &
fitness_management_pid=$!

echo
echo "Apps are starting. Logs:"
echo "  identity: $LOG_DIR/identity.log"
echo "  fitness-management-system: $LOG_DIR/fitness-management-system.log"
echo
echo "Press Ctrl-C to stop the Spring Boot apps. Docker Compose will stay up."

wait "$identity_pid" "$fitness_management_pid"
