# Chat

## Описание классов из пакета model

* **Client**: реализация основных операций клиента(connect, disconnect) 
* **Server**: реализация основных операций сервера(start, stop)
* **Message**: обертка для посылаемых сообщений, реализация методов write и read  
* **Settings**: синглтон, отвечает за хранение текущих настроек(имени пользователя, ip-адреса сервера и портов)
* **Controller**: реализует общение с собеседником(посылка и получение сообщений) и отвечает за взаимодействие UI и модели  
* **Main**: main-класс, для запуска чата 

## Описание классов из пакета ui

* **StartFrame**: стартовое окно, на нем можно выбрать тип соединения и изменить настройки
* **SettingsFrame**: окно с настройками
* **ChatFrame**: окно чата, можно отправлять сообщения и просматривать историю

## Преимущества данной реализации

  Лёгкость портирования на другие платформы; возможность общения с клиентом некоторых одногруппников при небольшом изменении Message.
