﻿///////////////////////////////////////////////////////////////////////
//
// Тест проверки работы таблицы значений
//
//
///////////////////////////////////////////////////////////////////////

Перем юТест;

////////////////////////////////////////////////////////////////////
// Программный интерфейс

Функция Версия() Экспорт
	Возврат "0.2";
КонецФункции

Функция ПолучитьСписокТестов(ЮнитТестирование) Экспорт

	юТест = ЮнитТестирование;
	ВсеТесты = Новый Массив;

	ВсеТесты.Добавить("ТестДолжен_СоздатьТаблицуЗначений");
	ВсеТесты.Добавить("ТестДолжен_СоздатьКолонкуТаблицыЗначений");
	ВсеТесты.Добавить("ТестДолжен_СоздатьУдалитьКолонки");
	ВсеТесты.Добавить("ТестДолжен_СоздатьУдалитьСтроки");
	ВсеТесты.Добавить("ТестДолжен_ПереместитьСтроки");
	ВсеТесты.Добавить("ТестДолжен_ОтработатьСДанными");
	ВсеТесты.Добавить("ТестДолжен_ЗаполнитьЗначенияБезУказанияКолонок");
	ВсеТесты.Добавить("ТестДолжен_ЗаполнитьЗначенияБезУказанияКолонокСТипизацией");
	ВсеТесты.Добавить("ТестДолжен_ЗаполнитьЗначенияСУказаниемКолонок1");
	ВсеТесты.Добавить("ТестДолжен_ЗаполнитьЗначенияСУказаниемКолонок2");

	ВсеТесты.Добавить("ТестДолжен_ВыгрузитьКолонкуВМассив");
	ВсеТесты.Добавить("ТестДолжен_НайтиСтрокуВТаблице");
	ВсеТесты.Добавить("ТестДолжен_НайтиНесколькоСтрокВТаблице");
	ВсеТесты.Добавить("ТестДолжен_СкопироватьТаблицуПолностью");
	ВсеТесты.Добавить("ТестДолжен_СкопироватьТаблицуПоМассивуСтрок");
	ВсеТесты.Добавить("ТестДолжен_СкопироватьТаблицуНесколькоКолонок");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСверткуБезУказанияКолонок");

	ВсеТесты.Добавить("ТестДолжен_ЗагрузитьКолонку");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьТипизациюКолонки");

	ВсеТесты.Добавить("ТестДолжен_ПроверитьСверткуБольшойТаблицы");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСверткуБольшойТаблицы2");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСверткуПоДвумКолонкам");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСверткуПоДвумКолонкамСРазнымиТипами");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСверткуПоКолонкамСуммированияСРазнымиТипами");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСверткуПоОднойКолонкеСРазнымиТипами");

	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодОчистить");

	ВсеТесты.Добавить("ТестДолжен_ПроверитьСортировкуБезУказанияСравненияЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСортировкуСУказаниемСравненияЗначений");

	Возврат ВсеТесты;

КонецФункции

Процедура ТестДолжен_СоздатьКолонкуТаблицыЗначений() Экспорт
	Т = Новый ТаблицаЗначений;
	ОписаниеТипаКолонки = Новый ОписаниеТипов("Число");
	К = Т.Колонки.Добавить("Колонка1", ОписаниеТипаКолонки, "Колонка 1", 4);
	юТест.ПроверитьРавенство(К.Имя, "Колонка1");
	юТест.ПроверитьРавенство(К.Заголовок, "Колонка 1");
	юТест.ПроверитьРавенство(ТипЗнч(К.ТипЗначения), Тип("ОписаниеТипов"));
	юТест.ПроверитьРавенство(К.Ширина, 4);
КонецПроцедуры

Процедура ТестДолжен_СоздатьТаблицуЗначений() Экспорт

	Перем Т;

	Т = Новый ТаблицаЗначений;
	КоличествоДобавляемыхКолонокСтрок = 5;

	Для Инд = 1 По КоличествоДобавляемыхКолонокСтрок Цикл

		Т.Колонки.Добавить("К" + Инд);
		Т.Добавить();

	КонецЦикла;

	юТест.ПроверитьРавенство(Т.Колонки.Количество(), КоличествоДобавляемыхКолонокСтрок);
	юТест.ПроверитьРавенство(Т.Количество(), КоличествоДобавляемыхКолонокСтрок);

КонецПроцедуры

Процедура ТестДолжен_СоздатьУдалитьКолонки() Экспорт

	Перем Т;

	Т = Новый ТаблицаЗначений;

	К1 = Т.Колонки.Добавить("К1");
	К2 = Т.Колонки.Добавить("К2");
	К3 = Т.Колонки.Добавить("К3");
	К4 = Т.Колонки.Добавить("К4");
	К5 = Т.Колонки.Добавить("К5");

	юТест.ПроверитьРавенство(Т.Колонки.Количество(), 5);

	// Удаление колонки по ссылке
	Т.Колонки.Удалить(К3);

	юТест.ПроверитьРавенство(Т.Колонки.Количество(), 4);
	юТест.ПроверитьРавенство(Т.Колонки.Найти("К3"), Неопределено);
	юТест.ПроверитьРавенство(Т.Колонки.Найти("К2"), К2);

	Строка1 = Т.Добавить();
	Строка2 = Т.Добавить();

	К6 = Т.Колонки.Добавить("К6");

	// Доступность К6 после добавления строк

	Попытка

		Строка1["К6"] = 123;
		юТест.ПроверитьРавенство(Строка1["К6"], 123);

	Исключение

		юТест.ПроверитьИстину(Ложь, "Колонка К6 не доступна!");

	КонецПопытки;

	// Удаление колонки по индексу
	Т.Колонки.Удалить(0);

	юТест.ПроверитьРавенство(Т.Колонки.Количество(), 4);
	юТест.ПроверитьРавенство(Т.Колонки.Найти("К1"), Неопределено);
	юТест.ПроверитьРавенство(Т.Колонки.Найти("К2"), К2);

	// Удаление колонки по имени
	Т.Колонки.Удалить("К4");

	юТест.ПроверитьРавенство(Т.Колонки.Количество(), 3);
	юТест.ПроверитьРавенство(Т.Колонки.Найти("К4"), Неопределено);
	юТест.ПроверитьРавенство(Т.Колонки.Найти("К2"), К2);

	// Недоступность удалённой колонки

	Попытка

		Значение = Строка1["К1"];
		юТест.ПроверитьИстину(Ложь, "Доступна удалённая колонка!");

	Исключение

	КонецПопытки;

КонецПроцедуры

Процедура ТестДолжен_СоздатьУдалитьСтроки() Экспорт

	Перем Т;

	Т = Новый ТаблицаЗначений;

	К1 = Т.Колонки.Добавить("К1");
	К2 = Т.Колонки.Добавить("К2");
	К3 = Т.Колонки.Добавить("К3");
	К4 = Т.Колонки.Добавить("К4");
	К5 = Т.Колонки.Добавить("К5");

	С1 = Т.Добавить();
	С2 = Т.Добавить();
	С2_5 = Т.Добавить();
	С2_6 = Т.Добавить();
	С3 = Т.Добавить();
	С4 = Т.Добавить();

	Т.Удалить(С2_5);
	Т.Удалить(2);

	юТест.ПроверитьРавенство(Т.Количество(), 4);

	юТест.ПроверитьРавенство(Т.Получить(0), С1);
	юТест.ПроверитьРавенство(Т.Получить(1), С2);
	юТест.ПроверитьРавенство(Т.Получить(2), С3);
	юТест.ПроверитьРавенство(Т.Получить(3), С4);

	юТест.ПроверитьРавенство(Т[0], С1);
	юТест.ПроверитьРавенство(Т[1], С2);
	юТест.ПроверитьРавенство(Т[2], С3);
	юТест.ПроверитьРавенство(Т[3], С4);

	юТест.ПроверитьРавенство(Т.Индекс(С1), 0);
	юТест.ПроверитьРавенство(Т.Индекс(С2), 1);
	юТест.ПроверитьРавенство(Т.Индекс(С3), 2);
	юТест.ПроверитьРавенство(Т.Индекс(С4), 3);

	Обошли = Новый Соответствие;
	Для Каждого мСтрокаТаблицы Из Т Цикл

		Обошли.Вставить(мСтрокаТаблицы, Истина);

	КонецЦикла;

	юТест.ПроверитьИстину(Обошли.Получить(С1), "Обход бегунком");
	юТест.ПроверитьИстину(Обошли.Получить(С2), "Обход бегунком");
	юТест.ПроверитьИстину(Обошли.Получить(С3), "Обход бегунком");
	юТест.ПроверитьИстину(Обошли.Получить(С4), "Обход бегунком");

	БылоИсключение = Ложь;
	Попытка
		ИндексНеСтроки = Т.Индекс(Т);
	Исключение
		БылоИсключение = Истина;
	КонецПопытки;

	юТест.ПроверитьИстину(БылоИсключение, "Индекс не строки таблицы значений дает ошибку");

	СтрокаДляУдаления = Т[0];
	Т.Удалить(СтрокаДляУдаления);
	БылоИсключение = Ложь;
	Попытка
		Т.Удалить(СтрокаДляУдаления);
	Исключение
		БылоИсключение = Истина;
	КонецПопытки;
	юТест.ПроверитьИстину(БылоИсключение, "Удаление несуществующей строки должно вызывать исключение");

	БылоИсключение = Ложь;
	Попытка
		Т.Удалить(Неопределено);
	Исключение
		БылоИсключение = Истина;
	КонецПопытки;
	юТест.ПроверитьИстину(БылоИсключение, "Удаление не строки должно вызывать исключение");

	БылоИсключение = Ложь;
	Попытка
		Т.Удалить(Т.Количество());
	Исключение
		БылоИсключение = Истина;
	КонецПопытки;
	юТест.ПроверитьИстину(БылоИсключение, "Удаление строки по индексу за пределами границ должно вызывать исключение");

КонецПроцедуры

Функция ПроверитьПорядок(Т, П1, П2, П3, П4, П5)

	Массив = Новый Массив;
	Массив.Добавить(П1);
	Массив.Добавить(П2);
	Массив.Добавить(П3);
	Массив.Добавить(П4);
	Массив.Добавить(П5);

	Для Инд = 0 По 4 Цикл

		Если Т[Инд].Индекс <> Массив[Инд] Тогда
			Возврат Ложь;
		КонецЕсли;

	КонецЦикла;

	Возврат Истина;

КонецФункции

Процедура ТестДолжен_ПереместитьСтроки() Экспорт

	Перем Т;

	Т = Новый ТаблицаЗначений;
	Т.Колонки.Добавить("Индекс");

	Для Инд = 1 По 5 Цикл
		Т.Добавить().Индекс = Инд;
	КонецЦикла;

	С1 = Т[0];
	С2 = Т[1];
	С3 = Т[2];
	С4 = Т[3];
	С5 = Т[4];

	// Проверим обычное перемещение

	Т.Сдвинуть(С2, -1);
	ПроверитьПорядок(Т, 2, 1, 3, 4, 5);

	Т.Сдвинуть(С1, 2);
	ПроверитьПорядок(Т, 2, 3, 4, 1, 5);

	Т.Сдвинуть(3, -2);
	ПроверитьПорядок(Т, 2, 1, 3, 4, 5);

	Т.Сдвинуть(1, 2);
	ПроверитьПорядок(Т, 2, 3, 4, 1, 5);

	// Проверим приведение типа
	Т.Сдвинуть("2", 2);
	ПроверитьПорядок(Т, 2, 3, 1, 5, 4);

	Т.Сдвинуть("1", "2");
	ПроверитьПорядок(Т, 2, 1, 5, 3, 4);

	// Проверим крайние случаи
	Т.Сдвинуть(0, 4);
	ПроверитьПорядок(Т, 1, 5, 3, 4, 2);

	Т.Сдвинуть(4, -4);
	ПроверитьПорядок(Т, 2, 1, 5, 3, 4);

	// Проверим ошибки
	Ошибка = "Сдвиг за пределы вниз";
	Попытка
		Т.Сдвинуть(0, 5);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Значение индекса выходит за пределы диапазона"); // В ОСкрипте: Неверное значение аргумента номер 2

	Ошибка = "Сдвиг за пределы вверх";
	Попытка
		Т.Сдвинуть(1, -3);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Значение индекса выходит за пределы диапазона"); // В ОСкрипте: Неверное значение аргумента номер 2

	Ошибка = "Сдвиг с неверным индексом";
	Попытка
		Т.Сдвинуть(10, 2);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Значение индекса выходит за пределы диапазона");

	Ошибка = "Сдвиг с неверным индексом";
	Попытка
		Т.Сдвинуть(-10, 2);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Значение индекса выходит за пределы диапазона");

	Ошибка = "Сдвиг несуществующего элемента";
	ДругаяТЗ = Новый ТаблицаЗначений;
	ДругаяТЗ.Колонки.Добавить("Индекс");
	ДругаяТЗ.Добавить().Индекс = 1;
	Попытка
		Т.Сдвинуть(ДругаяТЗ[0], 2);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Неверное значение аргумента"); // В ОСкрипте: Строка не принадлежит таблице значений

	Ошибка = "Сдвиг с неверным типом параметра";
	Попытка
		Т.Сдвинуть("ё", 2);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Преобразование к типу 'Число' не поддерживается"); // В ОСкрипте: Неверный тип аргумента
КонецПроцедуры

Процедура ТестДолжен_ОтработатьСДанными() Экспорт

	Перем Т;

	Т = Новый ТаблицаЗначений;

	Т.Колонки.Добавить("Количество");
	Т.Колонки.Добавить("Цена");
	Т.Колонки.Добавить("Сумма");

	Для Инд = 1 По 5 Цикл

		НоваяСтрока = Т.Добавить();
		НоваяСтрока.Количество = Инд;

	КонецЦикла;

	Цены = Новый Массив;
	Цены.Добавить(100);
	Цены.Добавить(50);
	Цены.Добавить(30);
	Цены.Добавить(32.3);
	Цены.Добавить(16);

	Т.ЗагрузитьКолонку(Цены, "Цена");

	мСумма = 0;
	Для Каждого мСтрока Из Т Цикл

		мСтрока.Сумма = мСтрока.Количество * мСтрока.Цена;
		мСумма = мСумма + мСтрока.Сумма;

	КонецЦикла;

	КолонкаСумма = Т.Колонки.Сумма;

	юТест.ПроверитьРавенство(Т.Итог("Сумма"), мСумма);
	юТест.ПроверитьРавенство(Т.Итог(2), мСумма);
	юТест.ПроверитьРавенство(Т.Итог(КолонкаСумма), мСумма);


	Т.Свернуть("", "Сумма");

	юТест.ПроверитьРавенство(Т[0].Сумма, мСумма);

	Т[0].Установить(0, 123);
	юТест.ПроверитьРавенство(Т[0].Сумма, 123, "Метод Установить() строки таблицы по индексу");

	Т[0].Установить("Сумма", 456);
	юТест.ПроверитьРавенство(Т[0].Сумма, 456, "Метод Установить() строки таблицы по имени");

	Для Каждого мЗначение Из Т[0] Цикл
		юТест.ПроверитьРавенство(мЗначение, 456, "Обход строки таблицы итератором");
	КонецЦикла;

	ОписаниеТипаЧисло = Новый ОписаниеТипов("Число");
	Т.Колонки.Добавить("НоваяКолонка", ОписаниеТипаЧисло);
	юТест.ПроверитьРавенство(Т[0].НоваяКолонка, 0, "Новая типизированная колонка имеет значение по-умолчанию");

КонецПроцедуры

Процедура ТестДолжен_ВыгрузитьКолонкуВМассив() Экспорт

	Т = Новый ТаблицаЗначений;
	Т.Колонки.Добавить("Ключ");
	Т.Колонки.Добавить("Значение");

	ЭталонКлючей = Новый Массив;
	ЭталонЗначений = Новый Массив;

	Для Сч = 1 По 5 Цикл
		С = Т.Добавить();
		С.Ключ = "Ключ" + Строка(Сч);
		С.Значение = Сч;

		ЭталонКлючей.Добавить(С.Ключ);
		ЭталонЗначений.Добавить(С.Значение);

	КонецЦикла;

	// Передаем имя колонки
	юТест.ПроверитьИстину(МассивыИдентичны(Т.ВыгрузитьКолонку("Ключ"), ЭталонКлючей), "Массивы ключей должны совпадать");
	юТест.ПроверитьИстину(МассивыИдентичны(Т.ВыгрузитьКолонку("Значение"), ЭталонЗначений), "Массивы значений должны совпадать");

	// Передаем индекс колонки
	юТест.ПроверитьИстину(МассивыИдентичны(Т.ВыгрузитьКолонку(0), ЭталонКлючей), "Массивы ключей должны совпадать");
	юТест.ПроверитьИстину(МассивыИдентичны(Т.ВыгрузитьКолонку(1), ЭталонЗначений), "Массивы значений должны совпадать");

	// Передаем колонку
	юТест.ПроверитьИстину(МассивыИдентичны(Т.ВыгрузитьКолонку(Т.Колонки.Ключ), ЭталонКлючей), "Массивы ключей должны совпадать");
	юТест.ПроверитьИстину(МассивыИдентичны(Т.ВыгрузитьКолонку(Т.Колонки.Значение), ЭталонЗначений), "Массивы значений должны совпадать");

КонецПроцедуры

Функция МассивыИдентичны(Знач Проверяемый, Знач Эталон)
	Если Проверяемый.Количество() <> Эталон.Количество() Тогда
		Возврат Ложь;
	КонецЕсли;

	Для Сч = 0 По Проверяемый.Количество()-1 Цикл
		Если Проверяемый[Сч] <> Эталон[Сч] Тогда
			Возврат Ложь;
		КонецЕсли;
	КонецЦикла;

	Возврат Истина;

КонецФункции

Процедура ТестДолжен_НайтиСтрокуВТаблице() Экспорт
	Т = Новый ТаблицаЗначений;
	Т.Колонки.Добавить("Ключ");
	Т.Колонки.Добавить("Значение");

	Для Сч = 1 По 5 Цикл
		С = Т.Добавить();
		С.Ключ = "Ключ" + Строка(Сч);
		С.Значение = Сч;
	КонецЦикла;

	ИскомаяСтрока = Т.Найти("Ключ2", "Ключ");
	юТест.ПроверитьЛожь(ИскомаяСтрока = Неопределено, "Строка должна быть найдена");
	юТест.ПроверитьРавенство(ИскомаяСтрока.Значение, 2);

	ИскомаяСтрока = Т.Найти("Ключ2, Которого нет нигде", "Ключ");
	юТест.ПроверитьРавенство(ИскомаяСтрока, Неопределено, "Строка не должна быть найдена");

	БылоИсключение = Ложь;
	Попытка
		ИскомаяСтрока = Т.Найти("Ключ2", "НесуществующийКлюч");
	Исключение
		БылоИсключение = Истина;
	КонецПопытки;
	юТест.ПроверитьИстину(БылоИсключение, "Поиск значения по не существующей колонке должен вызвать исключение");

КонецПроцедуры

Процедура ТестДолжен_НайтиНесколькоСтрокВТаблице() Экспорт
	Т = Новый ТаблицаЗначений;
	Т.Колонки.Добавить("Ключ");
	Т.Колонки.Добавить("Значение");

	Для Сч = 1 По 8 Цикл
		С = Т.Добавить();

		Если Сч % 2 Тогда
			С.Ключ = "Истина";
			С.Значение = Истина;
		Иначе
			С.Ключ = "Ложь";
			С.Значение = Ложь;
		КонецЕсли;

	КонецЦикла;

	БылоИсключение = Ложь;
	Попытка
		НайденныеСтроки = Т.НайтиСтроки("Строка");
	Исключение
		БылоИсключение = Истина;
	КонецПопытки;
	юТест.ПроверитьИстину(БылоИсключение, "Вызов НайтиСтроки с неверным типом бросает исключение");

	БылоИсключение = Ложь;
	Попытка
		КлючиПоиска = Новый Структура("НесуществующийКлюч", "Истина");
		НайденныеСтроки = Т.НайтиСтроки(КлючиПоиска);
	Исключение
		БылоИсключение = Истина;
	КонецПопытки;
	юТест.ПроверитьИстину(БылоИсключение, "Вызов НайтиСтроки с несуществующими колонками бросает исключение");

	КлючиПоиска = Новый Структура("Ключ,Значение", "Истина", Истина);
	НайденныеСтроки = Т.НайтиСтроки(КлючиПоиска);

	юТест.ПроверитьРавенство(НайденныеСтроки.Количество(), 4, "Количество строк должно совпадать с эталоном");

	Для Каждого Стр Из НайденныеСтроки Цикл
		юТест.ПроверитьРавенство(Стр.Ключ, "Истина");
		юТест.ПроверитьРавенство(Стр.Значение, Истина);
	КонецЦикла;

КонецПроцедуры

Функция СоздатьТаблицуСДанными()

	Т = Новый ТаблицаЗначений;
	Т.Колонки.Добавить("Ключ");
	Т.Колонки.Добавить("Значение");
	Т.Колонки.Добавить("К3");

	Для Сч = 1 По 5 Цикл
		С = Т.Добавить();
		С.Ключ = "Ключ" + Строка(Сч);
		С.Значение = Сч;
		С.К3 = "К3-" + Сч
	КонецЦикла;

	Возврат Т;

КонецФункции

Процедура ТестДолжен_СкопироватьТаблицуПолностью() Экспорт

	Т = СоздатьТаблицуСДанными();
	Т2 = Т.Скопировать();

	юТест.ПроверитьНеравенство(Т, Т2);
	юТест.ПроверитьРавенство(Т.Количество(), Т2.Количество());
	Для Сч = 0 По Т.Количество() - 1 Цикл
		С1 = Т[сч];
		С2 = Т2[сч];
		юТест.ПроверитьРавенство(С1.Ключ, С2.Ключ, "Равенство Ключей в строке " + Сч);
		юТест.ПроверитьРавенство(С1.Значение, С2.Значение, "Равенство Значений в строке " + Сч);
		юТест.ПроверитьРавенство(С1.К3, С2.К3, "Равенство КЗ в строке " + Сч);
		юТест.ПроверитьНеравенство(С1, С2);
	КонецЦикла;

КонецПроцедуры

Процедура ТестДолжен_СкопироватьТаблицуПоМассивуСтрок() Экспорт

	Т = СоздатьТаблицуСДанными();

	МассивСтрок = Новый Массив;
	МассивСтрок.Добавить(Т[0]);
	МассивСтрок.Добавить(Т[2]);
	Т2 = Т.Скопировать(МассивСтрок);

	юТест.ПроверитьНеравенство(Т, Т2);
	юТест.ПроверитьРавенство(2, Т2.Количество());
	Для Сч = 0 По Т2.Количество() - 1 Цикл
		С1 = МассивСтрок[сч];
		С2 = Т2[сч];
		юТест.ПроверитьРавенство(С1.Ключ, С2.Ключ, "Равенство Ключей в строке " + Сч);
		юТест.ПроверитьРавенство(С1.Значение, С2.Значение, "Равенство Значений в строке " + Сч);
		юТест.ПроверитьРавенство(С1.К3, С2.К3, "Равенство КЗ в строке " + Сч);
		юТест.ПроверитьНеравенство(С1, С2);
	КонецЦикла;

КонецПроцедуры

Процедура ТестДолжен_СкопироватьТаблицуНесколькоКолонок() Экспорт

	Т = СоздатьТаблицуСДанными();

	МассивСтрок = Новый Массив;
	МассивСтрок.Добавить(Т[0]);
	МассивСтрок.Добавить(Т[2]);
	Т2 = Т.Скопировать(МассивСтрок, "Ключ, Значение");

	юТест.ПроверитьНеравенство(Т, Т2);
	юТест.ПроверитьРавенство(2, Т2.Количество());
	юТест.ПроверитьРавенство(2, Т2.Колонки.Количество());
	Для Сч = 0 По Т2.Количество() - 1 Цикл
		С1 = МассивСтрок[сч];
		С2 = Т2[сч];
		юТест.ПроверитьРавенство(С1.Ключ, С2.Ключ, "Равенство Ключей в строке " + Сч);
		юТест.ПроверитьРавенство(С1.Значение, С2.Значение, "Равенство Значений в строке " + Сч);
		юТест.ПроверитьНеравенство(С1, С2);
	КонецЦикла;

КонецПроцедуры

Процедура ТестДолжен_ПроверитьСверткуБезУказанияКолонок() Экспорт

	Перем Т;

	Т = Новый ТаблицаЗначений;
	Т.Колонки.Добавить("Колонка1");

	Т.Добавить().Колонка1 = "Значение1";
	Т.Добавить().Колонка1 = "Значение2";

	Т.Добавить().Колонка1 = "Значение1";
	Т.Добавить().Колонка1 = "Значение2";

	// Вызываем свёртку БЕЗ указания второго параметра
	Т.Свернуть("Колонка1");

	Т.Сортировать("Колонка1 УБЫВ");

	юТест.ПроверитьРавенство(Т.Количество(), 2, "После свёртки должно остаться 2 строки");
	юТест.ПроверитьРавенство(Т.Получить(1).Колонка1, "Значение1", "Свёртка по значениям");
	юТест.ПроверитьРавенство(Т.Получить(0).Колонка1, "Значение2", "Свёртка по значениям");

КонецПроцедуры

Процедура ТестДолжен_ЗагрузитьКолонку() Экспорт

	Т = Новый ТаблицаЗначений;

	Т.Колонки.Добавить("Количество1");
	Т.Колонки.Добавить("Количество2");
	Т.Колонки.Добавить("Количество3");

	Для Инд = 1 По 5 Цикл

		НоваяСтрока = Т.Добавить();

	КонецЦикла;

	Количество = Новый Массив;
	Количество.Добавить(1);
	Количество.Добавить(2);
	Количество.Добавить(3);
	Количество.Добавить(4);
	Количество.Добавить(5);

	Т.ЗагрузитьКолонку(Количество, "Количество1");
	Т.ЗагрузитьКолонку(Количество, 1);
	Т.ЗагрузитьКолонку(Количество, Т.Колонки.Количество3);

	юТест.ПроверитьРавенство(Т.Итог("Количество1"), 15);
	юТест.ПроверитьРавенство(Т.Итог("Количество2"), 15);
	юТест.ПроверитьРавенство(Т.Итог("Количество3"), 15);

КонецПроцедуры

Процедура ТестДолжен_ПроверитьТипизациюКолонки() Экспорт

	Таблица = Новый ТаблицаЗначений;
	Таблица.Колонки.Добавить("Колонка1", Новый ОписаниеТипов("Строка", , Новый КвалификаторыСтроки(10)));

	СтрокаТаблицы = Таблица.Добавить();
	СтрокаТаблицы.Колонка1 = 1;

	юТест.ПроверитьРавенство(СтрокаТаблицы.Колонка1, "1");

	СтрокаТаблицы.Колонка1 = Неопределено;
	юТест.ПроверитьРавенство(СтрокаТаблицы.Колонка1, "", "Неопределено - Пустая строка");

	Таблица.Колонки.Добавить("Колонка2", Новый ОписаниеТипов("Строка,Число"));
	СтрокаТаблицы.Колонка2 = 1;
	юТест.ПроверитьРавенство(СтрокаТаблицы.Колонка2, 1);

	СтрокаТаблицы.Колонка2 = "ъ";
	юТест.ПроверитьРавенство(СтрокаТаблицы.Колонка2, "ъ");

	СтрокаТаблицы.Колонка2 = Неопределено;
	юТест.ПроверитьРавенство(СтрокаТаблицы.Колонка2, Неопределено);

КонецПроцедуры

// -- issue #1010

Функция ТестовыеДанные()
	Возврат  "135,29,91,60,30,120,61,31,92,121,130,131,133,8,9,10,11,134,13,14,15,16,5," +
	"62,32,132,7,8,63,10,11,64,33,93,122,5,6,7,8,12,65,34,94,123,5,6,7,8,13,66,35,95,4,5," +
	"6,7,8,14,96,67,36,4,5,15,68,37,97,4,5,6,9,17,124,69,38,98,4,5,6,19,70,39,99,4,5,6,7," +
	"20,71,40,100,4,5,6,21,72,41,22,23,73,42,24,74,43,101,25,75,102,44,4,5,6,26,114,128," +
	"-1,45,103,76,46,4,77,47,104,4,5,6,7,8,9,10,11,12,13,14,15,16,5,78,48,6,7,8,79,10,11," +
	"80,49,105,4,5,6,7,8,12,81,50,106,4,5,6,7,8,13,82,51,107,4,5,6,7,8,14,108,83,52,4,5," +
	"15,84,53,109,4,5,6,9,17,125,85,54,110,4,5,6,19,86,55,111,4,5,6,7,20,87,56,112,4,5,6," +
	"21,88,57,22,23,89,58,24,90,59,113,25,1,3,2,4,5,6,26,115,129";
КонецФункции

Функция ТестоваяТаблица()
	ТЗ = Новый ТаблицаЗначений;
	ТЗ.Колонки.Добавить("Значение1");
	ТЗ.Колонки.Добавить("Значение2");
	ТЗ.Колонки.Добавить("Количество1");
	ТЗ.Колонки.Добавить("Количество2");
	Возврат ТЗ;
КонецФункции

Функция ТаблицаСДанными()

	Строка = ТестовыеДанные();
	ТЗ = ТестоваяТаблица();

	Для каждого Элемент Из СтрРазделить(Строка, ",") Цикл
		СтрТЗ = ТЗ.Добавить();
		СтрТЗ.Значение1 = СокрЛП(Элемент);
		СтрТЗ.Количество1 = 1;
	КонецЦикла;

	Возврат ТЗ;

КонецФункции

Процедура ТестДолжен_ПроверитьСверткуБольшойТаблицы() Экспорт

	ТЗ = ТаблицаСДанными();
	ТЗ.Свернуть("Значение1", "Количество1");

	юТест.ПроверитьРавенство(ТЗ.Количество(), 127, "Количество строк после свёртки");
	юТест.ПроверитьРавенство(ТЗ.Колонки.Количество(), 2, "Количество колонок после свёртки");

	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "53", 1);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "12", 3);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "4", 18);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "8", 10);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "6", 18);
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСверткуБольшойТаблицы2() Экспорт

	Строка = ТестовыеДанные();
	ТЗ = ТестоваяТаблица();

	Для каждого Элемент Из СтрРазделить(Строка, ",") Цикл
		СтрТЗ = ТЗ.Добавить();
		СтрТЗ.Значение1 = СокрЛП(Элемент);
		СтрТЗ.Значение2 = СокрЛП(Элемент);
		СтрТЗ.Количество1 = 1;
	КонецЦикла;
	ТЗ2 = ТЗ.Скопировать(, "Значение1, Количество1");

	ТЗ2.Свернуть("Значение1", "Количество1");
	ТЗ.Свернуть("Значение1, Значение2", "Количество1");

	юТест.ПроверитьРавенство(ТЗ.Количество(), 127, "Количество строк после свёртки");
	юТест.ПроверитьРавенство(ТЗ.Колонки.Количество(), 3, "Количество колонок после свёртки");

	Для Каждого Стр Из ТЗ Цикл
		ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ2, Стр.Значение1, Стр.Количество1)
	КонецЦикла;
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСверткуПоДвумКолонкам() Экспорт

	Строка = ТестовыеДанные();
	ТЗ = ТестоваяТаблица();

	Сч=0;
	Для каждого Элемент Из СтрРазделить(Строка, ",") Цикл
		СтрТЗ = ТЗ.Добавить();
		СтрТЗ.Значение1 = СокрЛП(Элемент);
		СтрТЗ.Значение2 = Сч;
		СтрТЗ.Количество1 = 1;
		Сч=(Сч+1)%3;
	КонецЦикла;

	ТЗ.Свернуть("Значение1,Значение2", "Количество1");

	юТест.ПроверитьРавенство(ТЗ.Количество(), 159, "Количество строк после свёртки");
	юТест.ПроверитьРавенство(ТЗ.Колонки.Количество(), 3, "Количество колонок после свёртки");

	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "12", 2);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "4", 7);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "8", 2);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "6", 8);
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСверткуПоДвумКолонкамСРазнымиТипами() Экспорт

	Строка = ТестовыеДанные();
	ТЗ = ТестоваяТаблица();

	Сч=0;
	Для каждого Элемент Из СтрРазделить(Строка, ",") Цикл
		СтрТЗ = ТЗ.Добавить();
		СтрТЗ.Значение1 = СокрЛП(Элемент);
		СтрТЗ.Значение2 = ?(Сч=0,Элемент,'20200511');
		СтрТЗ.Количество1 = 1;
		Сч=(Сч+1)%2;
	КонецЦикла;

	ТЗ.Свернуть("Значение1,Значение2", "Количество1");

	юТест.ПроверитьРавенство(ТЗ.Количество(), 149, "Количество строк после свёртки");
	юТест.ПроверитьРавенство(ТЗ.Колонки.Количество(), 3, "Количество колонок после свёртки");

	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "12", 1);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "4", 10);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "8", 5);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "6", 10);
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСверткуПоКолонкамСуммированияСРазнымиТипами() Экспорт

	Строка = ТестовыеДанные();
	ТЗ = ТестоваяТаблица();

	Сч=0;
	Для каждого Элемент Из СтрРазделить(Строка, ",") Цикл
		СтрТЗ = ТЗ.Добавить();
		СтрТЗ.Значение1 = СокрЛП(Элемент);
		СтрТЗ.Количество1 = 1;
		СтрТЗ.Количество2 = ?(Сч=0,1,'20200511');
		Сч=(Сч+1)%2;
	КонецЦикла;

	ТЗ.Свернуть("Значение1", "Количество1,Количество2");

	юТест.ПроверитьРавенство(ТЗ.Количество(), 127, "Количество строк после свёртки");
	юТест.ПроверитьРавенство(ТЗ.Колонки.Количество(), 3, "Количество колонок после свёртки");

	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "12", 3, "Количество1");
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "12", 2, "Количество2");
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "4", 18, "Количество1");
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "4", 10, "Количество2");
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "8", 10, "Количество1");
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "8", 5, "Количество2");
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "6", 18, "Количество1");
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "6", 10, "Количество2");
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСверткуПоОднойКолонкеСРазнымиТипами() Экспорт

	Строка = ТестовыеДанные();
	ТЗ = ТестоваяТаблица();

	Сч=0;
	Для каждого Элемент Из СтрРазделить(Строка, ",") Цикл
		СтрТЗ = ТЗ.Добавить();
		СтрТЗ.Значение1 = ?(Сч=0,Число(Элемент),СокрЛП(Элемент));;
		СтрТЗ.Количество1 = 1;
		Сч=(Сч+1)%2;
	КонецЦикла;

	ТЗ.Свернуть("Значение1", "Количество1");

	юТест.ПроверитьРавенство(ТЗ.Количество(), 149, "Количество строк после свёртки");
	юТест.ПроверитьРавенство(ТЗ.Колонки.Количество(), 2, "Количество колонок после свёртки");

	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "12", 1);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, 12, 2);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "4", 8);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, 4, 10);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "8", 5);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, 8, 5);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, "6", 8);
	ПроверитьВТаблицеКоличествоДляЗначения1(ТЗ, 6, 10);
КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодОчистить() Экспорт

	Таблица = ТаблицаСДанными();
	юТест.ПроверитьНеравенство(Таблица.Количество(), 0, "Ожидали, что в таблице есть строки");
	Таблица.Очистить();
	юТест.ПроверитьРавенство(Таблица.Количество(), 0, "Ожидали, что в таблице нет строк");

КонецПроцедуры

Процедура ПроверитьВТаблицеКоличествоДляЗначения1(Таблица, Значение, Количество, КолонкаСуммы="Количество1")
	СтрТЗ2 = Таблица.Найти(Значение, "Значение1" );
	юТест.ПроверитьРавенство( СтрТЗ2[КолонкаСуммы], Количество,
	     СтрШаблон("Сумма колонки %1 для значения %2 типа %3", КолонкаСуммы, Значение, ТипЗнч(Значение)) );
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСортировкуБезУказанияСравненияЗначений() Экспорт
	Таблица = ТестоваяТаблицаДляСортировки();
	Таблица.Сортировать("Колонка1 УБЫВ");
	ПроверитьСортировку(Таблица, "ПорядокУбыв", "Ошибка сортировки");
	Таблица.Сортировать("Колонка1");
	ПроверитьСортировку(Таблица, "Порядок", "Ошибка сортировки");
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСортировкуСУказаниемСравненияЗначений() Экспорт
	Сравнение = Новый СравнениеЗначений;
	Таблица = ТестоваяТаблицаДляСортировки();
	Таблица.Сортировать("Колонка1 Убыв", Сравнение);
	ПроверитьСортировку(Таблица, "ПорядокУбыв", "Ошибка сортировки с указанием сравнения значений");
	Таблица.Сортировать("Колонка1", Сравнение);
	ПроверитьСортировку(Таблица, "Порядок", "Ошибка сортировки с указанием сравнения значений");
КонецПроцедуры

Процедура ТестДолжен_ЗаполнитьЗначенияБезУказанияКолонок() Экспорт

	ТЗ = Новый ТаблицаЗначений;
	ТЗ.Колонки.Добавить("К1");
	ТЗ.Колонки.Добавить("К2");

	ТЗ.Добавить();
	ТЗ.Добавить();

	ЗначениеЗаполнения = 2;
	ТЗ.ЗаполнитьЗначения(ЗначениеЗаполнения);
	Для Каждого мСтрока Из ТЗ Цикл
		юТест.ПроверитьРавенство(мСтрока.К1, ЗначениеЗаполнения);
		юТест.ПроверитьРавенство(мСтрока.К2, ЗначениеЗаполнения);
	КонецЦикла;

КонецПроцедуры

Процедура ТестДолжен_ЗаполнитьЗначенияБезУказанияКолонокСТипизацией() Экспорт

	ТипСтрока = Новый ОписаниеТипов("Строка");
	ТипЧисло = Новый ОписаниеТипов("Число");

	ТЗ = Новый ТаблицаЗначений;
	ТЗ.Колонки.Добавить("К1", ТипСтрока);
	ТЗ.Колонки.Добавить("К2", ТипЧисло);

	ТЗ.Добавить();
	ТЗ.Добавить();

	ЗначениеЗаполнения = 2;
	ТЗ.ЗаполнитьЗначения(ЗначениеЗаполнения);
	Для Каждого мСтрока Из ТЗ Цикл
		юТест.ПроверитьРавенство(мСтрока.К1, Строка(ЗначениеЗаполнения));
		юТест.ПроверитьРавенство(мСтрока.К2, ЗначениеЗаполнения);
	КонецЦикла;

КонецПроцедуры

Процедура ТестДолжен_ЗаполнитьЗначенияСУказаниемКолонок1() Экспорт

	ТипСтрока = Новый ОписаниеТипов("Строка");
	ТипЧисло = Новый ОписаниеТипов("Число");

	ТЗ = Новый ТаблицаЗначений;
	ТЗ.Колонки.Добавить("К1", ТипСтрока);
	ТЗ.Колонки.Добавить("К2", ТипЧисло);

	ТЗ.Добавить();
	ТЗ.Добавить();

	ЗначениеЗаполнения = 2;
	ТЗ.ЗаполнитьЗначения(ЗначениеЗаполнения, "К1,К2");
	Для Каждого мСтрока Из ТЗ Цикл
		юТест.ПроверитьРавенство(мСтрока.К1, Строка(ЗначениеЗаполнения));
		юТест.ПроверитьРавенство(мСтрока.К2, ЗначениеЗаполнения);
	КонецЦикла;

КонецПроцедуры

Процедура ТестДолжен_ЗаполнитьЗначенияСУказаниемКолонок2() Экспорт

	ТипСтрока = Новый ОписаниеТипов("Строка");
	ТипЧисло = Новый ОписаниеТипов("Число");

	ТЗ = Новый ТаблицаЗначений;
	ТЗ.Колонки.Добавить("К1", ТипСтрока);
	ТЗ.Колонки.Добавить("К2", ТипЧисло);

	ТЗ.Добавить();
	ТЗ.Добавить();

	ЗначениеЗаполнения = 2;
	ТЗ.ЗаполнитьЗначения(ЗначениеЗаполнения, "К1");
	Для Каждого мСтрока Из ТЗ Цикл
		юТест.ПроверитьРавенство(мСтрока.К1, Строка(ЗначениеЗаполнения));
		юТест.ПроверитьРавенство(мСтрока.К2, 0);
	КонецЦикла;

КонецПроцедуры

Функция ТестоваяТаблицаДляСортировки()

	Таблица = Новый ТаблицаЗначений;
	Таблица.Колонки.Добавить("Колонка1");
	Таблица.Колонки.Добавить("Порядок");
	Таблица.Колонки.Добавить("ПорядокУбыв");
	Таблица.Колонки.Добавить("ПорядокСлучайный");

	Г = Новый ГенераторСлучайныхЧисел;
	Порядок = 0;
	Для Каждого мЗначение Из ТестовыеЗначенияВПравильномПорядке() Цикл

		Порядок = Порядок + 1;

		НоваяСтрока = Таблица.Добавить();
		НоваяСтрока.Колонка1 = мЗначение;
		НоваяСтрока.Порядок = Порядок;
		НоваяСтрока.ПорядокУбыв = -Порядок;
		НоваяСтрока.ПорядокСлучайный = Г.СлучайноеЧисло(1, 1000);

	КонецЦикла;

	Таблица.Сортировать("ПорядокСлучайный");
	Таблица.Колонки.Удалить("ПорядокСлучайный");

	Возврат Таблица;

КонецФункции

Функция ТестовыеЗначенияВПравильномПорядке()
	Результат = Новый Массив;
	Результат.Добавить(Неопределено);
	Результат.Добавить("100");
	Результат.Добавить("11");
	Результат.Добавить(-1);
	Результат.Добавить(0);
	Результат.Добавить(1);
	Результат.Добавить('20221017');
	Результат.Добавить('20221017010000');
	Результат.Добавить('20221017010001');
	Результат.Добавить(Ложь);
	Результат.Добавить(Истина);
	Результат.Добавить(Тип("Число")); // между собой порядок не гарантирован
	Результат.Добавить(Новый Массив); // между собой порядок не гарантирован
	Результат.Добавить(Null);
	Возврат Результат;
КонецФункции

Процедура ПроверитьСортировку(Знач Таблица, Знач КолонкаПроверки, Знач ТекстСообщения)

	ПредыдущееЗначение = Неопределено;
	Для Каждого мСтрока Из Таблица Цикл

		ТекущееЗначение = мСтрока[КолонкаПроверки];

		Если ПредыдущееЗначение <> Неопределено Тогда
			юТест.ПроверитьМеньшеИлиРавно(ПредыдущееЗначение, ТекущееЗначение, ТекстСообщения);
		КонецЕсли;

		ПредыдущееЗначение = ТекущееЗначение;

	КонецЦикла;

КонецПроцедуры

Функция СтрШаблон(Знач СтрокаШаблон, Знач П1, Знач П2, Знач П3)

	СтрокаРезультат = СтрокаШаблон;
	СтрокаРезультат = СтрЗаменить(СтрокаРезультат, "%1", П1);
	СтрокаРезультат = СтрЗаменить(СтрокаРезультат, "%2", П2);
	СтрокаРезультат = СтрЗаменить(СтрокаРезультат, "%3", П3);

	Возврат СтрокаРезультат;

КонецФункции
