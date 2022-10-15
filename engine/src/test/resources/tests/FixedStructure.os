///////////////////////////////////////////////////////////////////////
// Тест класса ФиксированнаяСтруктура
///////////////////////////////////////////////////////////////////////

Перем юТест;

////////////////////////////////////////////////////////////////////
// Программный интерфейс

Функция ПолучитьСписокТестов(ЮнитТестирование) Экспорт

	юТест = ЮнитТестирование;

	ВсеТесты = Новый Массив;

    ВсеТесты.Добавить("ТестДолжен_ПроверитьПустуюФиксированнуюСтруктуру")
	ВсеТесты.Добавить("ТестДолжен_СоздатьФиксированнуюСтруктуруПоСтруктуре");
	ВсеТесты.Добавить("ТестДолжен_СоздатьФиксированнуюСтруктуруПоКлючамЗначениям");
	ВсеТесты.Добавить("ТестДолжен_СоздатьФиксированнуюСтруктуруПоКлючамЗначениямСоЗначениямиПоУмолчанию");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьТипПараметровКонструктора");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьНеизменностьФиксированнойСтруктуры");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьЧтоНельзяПоменятьЗначениеПриОбращенииЧерезКлюч");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодСвойство");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьОтсутствиеМетодаВставить");
	ВсеТесты.Добавить("ТестДолжен_СоздатьСтруктуруПоФиксированнойСтруктуре");

	Возврат ВсеТесты;
КонецФункции

Процедура ТестДолжен_ПроверитьПустуюФиксированнуюСтруктуру() Экспорт
    ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура();
    юТест.ПроверитьРавенство(0, ФиксированнаяСтруктура.Количество());
КонецПроцедуры

Процедура ТестДолжен_СоздатьФиксированнуюСтруктуруПоСтруктуре() Экспорт
	Структура = Новый Структура("Ключ1", "Значение1");

	ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура(Структура);

	юТест.ПроверитьТип(ФиксированнаяСтруктура, Тип("ФиксированнаяСтруктура"));
	юТест.ПроверитьРавенство(
		Структура.Количество(), 
		ФиксированнаяСтруктура.Количество(), 
		"количество элементов исходной и полученной структуры совпадут")
КонецПроцедуры

Процедура ТестДолжен_СоздатьФиксированнуюСтруктуруПоКлючамЗначениям() Экспорт
	ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура("Ключ1", "Значение1");

	юТест.ПроверитьТип(ФиксированнаяСтруктура, Тип("ФиксированнаяСтруктура"));
	юТест.ПроверитьРавенство(1, ФиксированнаяСтруктура.Количество(),
		"количество элементов исходной и полученной структуры совпадут");
КонецПроцедуры

Процедура ТестДолжен_СоздатьФиксированнуюСтруктуруПоКлючамЗначениямСоЗначениямиПоУмолчанию() Экспорт
	ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура("Ключ1,Ключ2");

	юТест.ПроверитьРавенство(2, ФиксированнаяСтруктура.Количество());
	юТест.ПроверитьРавенство(Неопределено, ФиксированнаяСтруктура.Ключ1);
	юТест.ПроверитьРавенство(Неопределено, ФиксированнаяСтруктура.Ключ2);
КонецПроцедуры

Процедура ТестДолжен_ПроверитьТипПараметровКонструктора() Экспорт
	ФиксированнаяСтруктура1 = Новый ФиксированнаяСтруктура("Ключ1,Ключ2", "Значение1","Значение2");

	БезОшибки = Истина;
	Попытка
		ФиксированнаяСтруктура2 = Новый ФиксированнаяСтруктура( ФиксированнаяСтруктура1, "Значение1","Значение2");
	Исключение
		БезОшибки = Ложь;
	КонецПопытки;

	Если БезОшибки Тогда
		ВызватьИсключение "Недопустимый тип параметра конструктора Структуры: ФиксированнаяСтруктура";
	КонецЕсли;

	БезОшибки = Истина;
	Попытка
		ФиксированнаяСтруктура2 = Новый ФиксированнаяСтруктура( 1000, "Значение1","Значение2");
	Исключение
		БезОшибки = Ложь;
	КонецПопытки;

	Если БезОшибки Тогда
		ВызватьИсключение "Недопустимый тип параметра конструктора Структуры: Число";
	КонецЕсли;

	БезОшибки = Истина;
	Попытка
		ФиксированнаяСтруктура2 = Новый ФиксированнаяСтруктура( Неопределено, "Значение1","Значение2");
	Исключение
		БезОшибки = Ложь;
	КонецПопытки;

	Если БезОшибки Тогда
		ВызватьИсключение "Недопустимый тип параметра конструктора ФиксированнойСтруктуры: Неопределено";
	КонецЕсли;
КонецПроцедуры

Процедура ТестДолжен_ПроверитьНеизменностьФиксированнойСтруктуры() Экспорт
	Значение = "Значение1";
	Структура = Новый Структура("Ключ1", Значение);

	ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура(Структура);

	юТест.ПроверитьРавенство(Структура.Ключ1, ФиксированнаяСтруктура.Ключ1, 
		"ФиксированнаяСтруктура.Ключ1 равно Структура.Ключ1");

	Структура.Ключ1 = Значение + 1;
	юТест.ПроверитьРавенство(Значение, ФиксированнаяСтруктура.Ключ1, 
		"ФиксированнаяСтруктура.Ключ1/Значение1 не меняет своего значения после изменения Структура.Ключ1/Значение");

	Значение = "Значение10";
	юТест.ПроверитьРавенство("Значение1", ФиксированнаяСтруктура.Ключ1,
		"ФиксированнаяСтруктура.Ключ1/Значение1 не меняет своего значения после изменения исходной переменной");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодСвойство() Экспорт
	Структура = Новый Структура("Ключ1", "Значение1");

	ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура(Структура);

	юТест.ПроверитьИстину(ФиксированнаяСтруктура.Свойство("Ключ1"), 
		"проверяем ФиксированнаяСтруктура.Свойство(""Ключ1"")");
	юТест.ПроверитьЛожь(ФиксированнаяСтруктура.Свойство("НесуществующийКлюч"), 
		"проверяем ФиксированнаяСтруктура.Свойство(""Ключ1"")");

	Значение = Неопределено;
	ФиксированнаяСтруктура.Свойство("Ключ1", Значение);
	юТест.ПроверитьРавенство("Значение1", Значение, 
		"проверяем значение из ФиксированнаяСтруктура.Свойство(""Ключ1"", Значение)");

	Значение = Неопределено;
	ФиксированнаяСтруктура.Свойство("НесуществующийКлюч", Значение);
	юТест.ПроверитьРавенство(Неопределено, Значение, 
	"проверяем значение из ФиксированнаяСтруктура.Свойство(""Ключ1"", Значение)");
КонецПроцедуры

Процедура ТестДолжен_ПроверитьОтсутствиеМетодаВставить() Экспорт
	Структура = Новый Структура("Ключ1", "Значение1");

	ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура(Структура);

	Попытка
		ФиксированнаяСтруктура.Вставить("Ключ2", "Значение2");
	Исключение
		юТест.ПроверитьРавенство(1, ФиксированнаяСтруктура.Количество(), 
			"количество элементов фиксированной структуры не меняется");
		Возврат;
	КонецПопытки;

	ВызватьИсключение "У класса ""ФиксированнаяСтруктура"" не должно быть метода ""Вставить"", а он есть";
КонецПроцедуры


Процедура ТестДолжен_ПроверитьЧтоНельзяПоменятьЗначениеПриОбращенииЧерезКлюч() Экспорт
	Значение = "Значение1";
	Структура = Новый Структура("Ключ1", Значение);

	ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура(Структура);

	Попытка
		ФиксированнаяСтруктура.Ключ1 = Значение+1;
	Исключение
		юТест.ПроверитьРавенство(Значение, ФиксированнаяСтруктура.Ключ1, 
			"ФиксированнаяСтруктура.Ключ1/Значение1 не меняет своего значения после изменения Структура.Ключ1/Значение");
		Возврат;
	КонецПопытки;

	ВызватьИсключение "Ожидали, что у класса ""ФиксированнаяСтруктура"" нельзя менять значение через обращение по ключу, а получили, что меняется";

КонецПроцедуры

Процедура ТестДолжен_СоздатьСтруктуруПоФиксированнойСтруктуре() Экспорт

	ФиксированнаяСтруктура = Новый ФиксированнаяСтруктура("Ключ1, Ключ2", "Значение1", "Значение2");
	Структура = Новый Структура(ФиксированнаяСтруктура);

	юТест.ПроверитьТип(Структура, Тип("Структура"));
	юТест.ПроверитьРавенство(ФиксированнаяСтруктура.Количество(), Структура.Количество(), "количество элементов исходной и полученной структуры совпадут");
	юТест.ПроверитьРавенство(ФиксированнаяСтруктура.Ключ1, Структура.Ключ1, "значения элементов совпадут");
	юТест.ПроверитьРавенство(ФиксированнаяСтруктура.Ключ2, Структура.Ключ2, "значения элементов совпадут");
КонецПроцедуры