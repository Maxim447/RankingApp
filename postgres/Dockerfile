FROM postgres:17

# Установка зависимостей для PostGIS
RUN apt-get update && apt-get install -y \
    postgresql-17-postgis-3 \
    postgresql-17-postgis-3-scripts \
    && rm -rf /var/lib/apt/lists/*

# Копируем скрипт инициализации
COPY init-scripts /docker-entrypoint-initdb.d