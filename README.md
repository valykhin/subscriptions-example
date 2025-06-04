# Subscriptions application

## Run application

### Local environment
- Выбрать свободный порт для базы данных: ```127.0.0.1:15436```
  - Указать в local/docker-compose.yml:
    ```- "15436:5432"```
  - Указать в переменных окружения конфигурации запуска Spring application:
    ```spring.profiles.active=local;SPRING_DATASOURCE_PASSWORD=12345;SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:15436/subscriptions;SPRING_DATASOURCE_USERNAME=postgres```
- Запустить конфигурацию запуска Spring приложения

## Build and Test:

- Выполнить в корне проекта команду: ```./gradlew :clean build test jacocoTestReport```
- Покрытие тестов можно узнать в jacoco coverage report: [build/reports/coverage/index.html](build/reports/coverage/index.html)

## Build docker image
- ```./gradlew dockerBuild```


## Push docker image
- ```./gradlew dockerBuild```

## Run docker-compose
- Создать файл .env в корне проекта
- Добавить в него переменные с актуальными значениями
  - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/subscriptions
  - SPRING_DATASOURCE_USERNAME=<username>
  - SPRING_DATASOURCE_PASSWORD=<password>
- Выполнить команду ```docker-compose up -d```

## Swagger
/swagger-ui/index.html

## Prometheus metrics
http://<host>:8080/actuator/prometheus