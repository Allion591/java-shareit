ShareIt
Платформа для аренды вещей
ShareIt — это backend-сервис для шеринга вещей, позволяющий пользователям предлагать свои предметы в аренду, искать нужные вещи и бронировать их на определённые даты.

🚀 Технологии
Java 17+

Spring Boot 2.7

Spring Data JPA

PostgreSQL / H2 (для тестов)

Maven

Mockito / JUnit 5 (тестирование)

Docker 

📁 Структура проекта
Проект построен по модульной архитектуре:

text
shareit/
├── shareit-server/          # Основная бизнес-логика
│   ├── controllers/         # REST endpoints
│   ├── services/           # Бизнес-логика
│   ├── repositories/       # Работа с БД
│   ├── models/            # Сущности БД
│   └── dto/               # Data Transfer Objects
├── shareit-gateway/        # Входная точка, валидация
│   ├── controllers/        # Gateway endpoints
│   └── clients/           # Feign-клиенты к server
└── pom.xml                # Мультимодульный Maven проект

🎯 Функциональности
Управление пользователями (регистрация, обновление, просмотр)

Управление вещами (добавление, редактирование, поиск)

Система бронирований (создание, подтверждение, просмотр)

Запросы на вещи (если нужной вещи нет в каталоге)

Комментарии и отзывы к арендованным вещам

Поиск по каталогу с фильтрацией

🚦 Запуск проекта
Требования:
Java 17+

Maven 3.8+

PostgreSQL 12+

Шаги для запуска:
Клонировать репозиторий:

bash
git clone <repository-url>
cd shareit
Настроить базу данных:

sql
Обновить настройки в application.properties: (учётные данные БД)

Собрать проект:

bash
mvn clean package
Запустить модули:

bash
# В отдельном терминале - server модуль
java -jar shareit-server/target/shareit-server.jar

# В другом терминале - gateway модуль  
java -jar shareit-gateway/target/shareit-gateway.jar
Docker Compose:
bash
docker-compose up -d
📚 API Endpoints
Основные endpoints:

Пользователи
POST /users - создать пользователя

GET /users/{id} - получить пользователя

GET /users - все пользователи

PATCH /users/{id} - обновить

DELETE /users/{id} - удалить

Вещи
POST /items - добавить вещь

PATCH /items/{id} - обновить

GET /items/{id} - получить

GET /items?from={from}&size={size} - вещи пользователя

GET /items/search?text={text} - поиск

POST /items/{id}/comment - добавить отзыв

Бронирования
POST /bookings - создать бронь

PATCH /bookings/{id}?approved={true|false} - подтвердить/отклонить

GET /bookings/{id} - получить бронь

GET /bookings?state={state} - брони пользователя

GET /bookings/owner?state={state} - брони владельца

Запросы
POST /requests - создать запрос

GET /requests/{id} - получить запрос

GET /requests - мои запросы

GET /requests/all?from={from}&size={size} - все запросы

🧪 Тестирование
bash
# Запуск всех тестов
mvn test

# Запуск тестов с отчётом о покрытии
mvn test jacoco:report
Проект покрыт:

Юнит-тестами (Mockito)

Интеграционными тестами

Тестами репозиториев

📊 База данных
Схема БД включает сущности:

users - пользователи

items - вещи

bookings - бронирования

requests - запросы на вещи

comments - отзывы

Миграции управляются через Liquibase.

🔗 Взаимодействие модулей
text
Клиент → Gateway (валидация) → Feign Client → Server (бизнес-логика) → БД
📄 Документация API
После запуска доступна через Swagger:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI спецификация: http://localhost:8080/v3/api-docs

📝 Лицензия
Это учебный проект.

Разработано в рамках учебного проекта по микросервисной архитектуре
