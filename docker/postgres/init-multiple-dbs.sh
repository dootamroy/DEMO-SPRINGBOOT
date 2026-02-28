#!/bin/sh
set -eu

create_db_if_needed() {
  db_name="$1"

  if [ -z "$db_name" ] || [ "$db_name" = "postgres" ]; then
    return
  fi

  if psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname postgres -tAc "SELECT 1 FROM pg_database WHERE datname='${db_name}'" | grep -q 1; then
    echo "Database '${db_name}' already exists, skipping"
    return
  fi

  echo "Creating database '${db_name}'"
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname postgres -c "CREATE DATABASE \"${db_name}\""
}

create_db_if_needed "${DEMO1_DB_NAME:-demo1_db}"
create_db_if_needed "${DEMO2_DB_NAME:-demo2_db}"
