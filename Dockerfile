# Указываем базовый образ с нужной версией JDK (в данном случае Java 21)
FROM bellsoft/liberica-openjdk-alpine:21.0.1

# Создаем пользователя и группу для выполнения приложения
RUN addgroup --system spring-boot-group && adduser --system --ingroup spring-boot-group spring-boot
USER spring-boot

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл сборки JAR из локальной системы в контейнер и переименовывает его в app.jar
COPY target/transaction-api-1.0.0.jar app.jar

# Указываем команду запуска JAR файла
ENTRYPOINT ["java", "-jar", "app.jar"]