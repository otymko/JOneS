# Java OneScript (Jones)

Jones - реализация [OneScript](https://github.com/EvilBeaver/OneScript) на Java.

## Зачем?

Расширение программ на Java-подобных языках через скрипты OneScript.

## Как использовать?

### Запуск скрипта из консоли

```sh
java -jar /path/to/jonec-exec.jar /path/to/script.os
```

где:

* `/path/to/jonec-exec.jar` - путь к исполняемому файлу. Где найти? Самому собрать из исходников или посмотреть 
в релизах (если они есть).
* `/path/to/script.os` - путь к скрипту OneScript.

Например:

```sh
java -jar jonec-exec.jar my-script.os
```

### Подключение к приложению

#### Начальный пример

Скрипт `my-script.os`:

```bsl
Перем ЧислоИзМодуля

Процедура ВывестиЧисло()
    Сообщить(ЧислоИзМодуля);
КонецПроцедуры

ЧислоИзМодуля = 42;
```

Подключаем зависимость (собирается на JitPack)

`develop-SNAPSHOT` - тег, по которому берется версия с [JitPack](https://jitpack.io/#otymko/JOneS/). Тег может быть как
номер версии (если есть релизы), так и хеш коммита на GitHub.

Gradle:

```groovy
implementation group: 'com.github.otymko', name: 'JOneS', version: 'develop-SNAPSHOT'
```

Gradle (Kotlin):

```kotlin
implementation("com.github.otymko", "JOneS", "develop-SNAPSHOT")
```

```xml

<dependency>
    <groupId>com.github.otymko</groupId>
    <artifactId>JOneS</artifactId>
    <version>develop-SNAPSHOT</version>
</dependency>
```

Добавляем в приложение на Java:

```java
Path pathToScript=Path.of("/path/to/my-script.os");
  ScriptEngine engine=new ScriptEngine();
  ScriptCompiler compiler=new ScriptCompiler(engine);
  ModuleImage moduleImage=compiler.compile(pathToScript,UserScriptContext.class);

  int methodId=sdo.getScriptMethod("ВывестиЧисло");
  sdo.callScriptMethod(engine,methodId,new IValue[0]);
```

После выполнения кода, будет выведено сообщение:

```
42
```

## Дорожная карта

Для начала, реализовать задачи на [Доска MVP](https://github.com/otymko/JOneS/projects/1) и выпустить релиз.

## Лицензия

Используется лицензия [Mozilla Public License Version 2.0](LICENSE)