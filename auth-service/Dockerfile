# authentication-service/Dockerfile
FROM gradle:8.10-jdk21 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем необходимые файлы для сборки
COPY settings.gradle build.gradle ./
COPY common-module/build.gradle ./common-module/
COPY auth-service/build.gradle ./auth-service/
COPY common-module/src ./common-module/src/
COPY auth-service/src ./auth-service/src/

# Выполняем сборку
RUN gradle build -x test --no-daemon --info

# Этап запуска
FROM openjdk:21

# Копируем собранный JAR-файл из этапа сборки
COPY --from=build /app/auth-service/build/libs/*.jar /app/auth-service.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]
