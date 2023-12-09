# VakSmsAPI
### Способы создания клиента для работы с API:
- 1 вариант (Через статический метод)
```java
VakSmsApi api = VakSmsApi.createWithApiKey("your-api-key");
```
- 2 вариант (Конструктор)
```java
VakSmsApi api = new VakSmsApi(new OkHttpClient(), "your-api-key-here");
```
