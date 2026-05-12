## Ad board application

## Технологии

- Java 17
- Spring Boot 4.0.5
- PostgreSQL 16.8
- Liquibase
- Maven
- Docker / Docker Compose
- Swagger

## Гайд по запуску

- скачать репозиторий с github,
- распаковать его в любом удобном месте
- в cmd перейти в папку docker скачанного репозитория -> cd путь_к_проекту\docker
- прописать docker compose build и дождаться окончания загрузки
- прописать docker compose up

## Гайд по тесту

можно тестировать через swagger в браузере:
http://localhost:8080/swagger-ui/index.html
или через постман по адресу:
http://localhost:8080/

## Тестовые Пользователи

Admin:
 - mail: super_admin@mail.com 
 - password: super_admin

users:
 - mail: test_one@mail.com
 - password: test_one

 - mail: test_two@mail.com
 - password: test_two

 - mail: test_three@mail.com
 - password: test_three