# TicketsService
## Система заказов по покупке билетов

## Работу выполнил
__Мухин Дмитрий БПИ228__

## Запуск

1. Склонировать репозиторий
2. Создать .jar файлы, запустить docker-compose
Для этого запустить команду из корневой директории проекта:
```
cd auth
./gradlew bootJar
cd ../booking
./gradlew bootJar
cd ..
docker-compose down
docker-compose up -d

```

## Заполнение данными

База данных заполняется автоматически при запуске docker-compose файла.

Для удобной работы в базу сразу заносятся данные об остановках.

Остальные данные пользователю предлагается заполнить используя REST API.

## Коллекция Postman

Доступна по [ссылке](https://www.postman.com/navigation-observer-52281699/workspace/kpoticketingsystem/collection/15266125-7989f32c-e224-431e-ae62-03e1cc3e1eab?action=share&creator=15266125)

## Технологии

- Spring Boot
- Spring Security
- JWT
- Kotlin
- PostgreSQL
- Liquibase