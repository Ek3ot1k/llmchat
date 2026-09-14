# LLM Chat

Консольное Java-приложение для диалога с локальной LLM через [Ollama](https://ollama.com/). Приложение сохраняет историю текущего диалога, поэтому модель учитывает предыдущие сообщения.

## Возможности

- отправка сообщений модели `llama3.2:1b`;
- системная инструкция для модели;
- сохранение контекста в рамках запущенного приложения;
- очистка истории командой `/clear`;
- обработка ошибок подключения к Ollama и отсутствующей модели.

## Требования

- Java 21;
- Ollama;
- модель `llama3.2:1b`.


## Установка и запуск Ollama

1. Установите Ollama с [официального сайта](https://ollama.com/download).
2. Загрузите модель:

   ```bash
   ollama pull llama3.2:1b
   ```

3. В отдельном окне терминала запустите сервер Ollama:

   ```bash
   ollama serve
   ```

Обычно Ollama доступна по адресу `http://localhost:11434`.

## Запуск приложения

В корне проекта выполните:

```bash
./mvnw spring-boot:run
```

В Windows:

```bat
mvnw.cmd spring-boot:run
```

После запуска введите сообщение в консоль. Для завершения работы используйте `/exit`.

Пример:

```text
Вы: Меня зовут Амин.
Assistant: Приятно познакомиться, Амин.

Вы: Как меня зовут?
Assistant: Вас зовут Амин.
```

## Команды

| Команда | Описание |
| --- | --- |
| `/clear` | Очищает историю текущего диалога. |
| `/exit` | Завершает работу приложения. |

Пустые сообщения не отправляются модели.

## Настройка

По умолчанию используются следующие значения:

```text
LLM_BASE_URL=http://localhost:11434
LLM_MODEL=llama3.2:1b
```

Их можно переопределить переменными окружения. Например, для macOS/Linux:

```bash
export LLM_BASE_URL=http://localhost:11434
export LLM_MODEL=llama3.2:1b
./mvnw spring-boot:run
```

Для PowerShell:

```powershell
$env:LLM_BASE_URL = "http://localhost:11434"
$env:LLM_MODEL = "llama3.2:1b"
.\mvnw.cmd spring-boot:run
```

## Тесты

Для запуска unit-тестов:

```bash
./mvnw test
```

## Структура проекта

```text
ConsoleRunner -> ChatService -> OllamaClient -> Ollama API
```

- `ConsoleRunner` читает ввод пользователя и выводит ответы;
- `ChatService` хранит историю и добавляет системную инструкцию;
- `OllamaClient` отправляет HTTP-запросы в Ollama.
