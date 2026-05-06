Ad board application

## Технологии

- Java 17
- Spring Boot 4.0.5
- PostgreSQL 16.8
- Liquibase
- Maven
- Docker / Docker Compose

## Гайд по запуску

1.скачать репозиторий с github,
2.распаковать его в любом удобном месте
3.в cmd перейти в папку ad_board скачанного репозитория -> cd путь_к_проекту\ad_board
4.прописать docker compose build
5.прописать docker compose up

всё!

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