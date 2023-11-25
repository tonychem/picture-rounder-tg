Телеграм-бот, который конвертирует входящие видеофайлы в кружки с вотермаркой. 
Входящие видео должны быть длительностью не более 60 секунд.

Требования:
JDK 17+, Maven, FFmpeg

Развертывание:
```shell
git clone https://github.com/tonychem/picture-rounder-tg.git
```

В файл application.yml внести токен и юзернейм бота, далее перейти в корневую папку с проектом

```shell
mvn clean package
java -jar target/watermark-bot-0.0.1-SNAPSHOT.jar
```