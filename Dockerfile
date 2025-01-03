# authentication-service/Dockerfile
FROM gradle:8.10-jdk21 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем необходимые файлы для сборки
COPY build.gradle .
COPY src ./src

# Выполняем сборку
RUN gradle build -x test --no-daemon --info

# Этап запуска
FROM openjdk:21

# Копируем собранный JAR-файл из этапа сборки
COPY --from=build /app/build/libs/*.jar /app/

# Указываем команду для запуска приложения
CMD ["java", "-jar", "/app/app-0.0.1-SNAPSHOT.jar"]
