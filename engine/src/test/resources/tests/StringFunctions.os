Перем ЮнитТестирование;

Функция ПолучитьСписокТестов(Тестировщик) Экспорт
	ЮнитТестирование = Тестировщик;

	ВсеТесты = Новый Массив;
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСокрП");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСокрЛ");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСокрЛП");

	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_НаКириллице");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_НаЛатинице");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_СПустойСтрокой");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_СПустойСтрокойРазделителя");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_РазделениеБезПустых");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_РазделениеСПустыми");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_СПустойСтрокойБезПустых");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_Авторазделение");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрРазделить_ПараметрыНеЗаданы");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрСравнить");
	ВсеТесты.Добавить("ТестДолжен_ВызватьМетод_СтрСоединить");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСтрЧислоСтрок");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСтрНайти");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСтрНачинаетсяС");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСтрЧислоВхожденийВхожденийНет");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСтрЧислоВхождений");

	Возврат ВсеТесты;
КонецФункции

&Тест
Процедура ТестДолжен_ПроверитьСокрП() Экспорт

	Значение = "Значение  ";
	НовоеЗначение = СокрП(Значение);
	ЮнитТестирование.ПроверитьРавенство(НовоеЗначение, "Значение");

	Значение = "  Значение";
	НовоеЗначение = СокрП(Значение);
	ЮнитТестирование.ПроверитьРавенство(НовоеЗначение, Значение);

КонецПроцедуры

&Тест
Процедура ТестДолжен_ПроверитьСокрЛ() Экспорт

	Значение = "  Значение";
	НовоеЗначение = СокрЛ(Значение);
	ЮнитТестирование.ПроверитьРавенство(НовоеЗначение, "Значение");

	Значение = "Значение  ";
	НовоеЗначение = СокрЛ(Значение);
	ЮнитТестирование.ПроверитьРавенство(НовоеЗначение, Значение);

КонецПроцедуры

&Тест
Процедура ТестДолжен_ПроверитьСокрЛП() Экспорт
	Значение = "  Значение  ";
	НовоеЗначение = СокрЛП(Значение);
	ЮнитТестирование.ПроверитьРавенство(НовоеЗначение, "Значение");

	Значение = "Значение  ";
	НовоеЗначение = СокрЛП(Значение);
	ЮнитТестирование.ПроверитьРавенство(НовоеЗначение, "Значение");

	Значение = "  Значение";
	НовоеЗначение = СокрЛП(Значение);
	ЮнитТестирование.ПроверитьРавенство(НовоеЗначение, "Значение");
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_НаКириллице() Экспорт
	мРезультат = СтрРазделить("0,1,2,3,4,5", ",");
	ЮнитТестирование.ПроверитьРавенство(6, мРезультат.Количество());
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_НаЛатинице() Экспорт
	мРезультат = StrSplit("0,1,2,3,4,5", ",");
	ЮнитТестирование.ПроверитьРавенство(6, мРезультат.Количество());
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_СПустойСтрокой() Экспорт
	ТекстОшибки = "";
	мРезультат = СтрРазделить("", ",");
	ЮнитТестирование.ПроверитьРавенство(1, мРезультат.Количество());
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_СПустойСтрокойБезПустых() Экспорт
	ТекстОшибки = "";
	мРезультат = СтрРазделить("", ",", ложь);
	ЮнитТестирование.ПроверитьРавенство(0, мРезультат.Количество());
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_СПустойСтрокойРазделителя() Экспорт
	ТекстОшибки = "";
	Попытка
		мРезультат = СтрРазделить("0,1,2,3,4,5", "");
	Исключение
		ТекстОшибки = ОписаниеОшибки();
	КонецПопытки;
	ЮнитТестирование.ПроверитьРавенство("0,1,2,3,4,5", мРезультат[0]);
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_РазделениеБезПустых() Экспорт
	мРезультат = СтрРазделить("0,1,2,,4,5", ",", Ложь);
	ЮнитТестирование.ПроверитьРавенство(5, мРезультат.Количество());
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_РазделениеСПустыми() Экспорт
	мРезультат = СтрРазделить("0,1,2,,4,5", ",", Истина);
	ЮнитТестирование.ПроверитьРавенство(6, мРезультат.Количество());
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_Авторазделение() Экспорт
	мРезультат = СтрРазделить("0,1,2,,4,5", ",");
	ЮнитТестирование.ПроверитьРавенство(6, мРезультат.Количество());
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрРазделить_ПараметрыНеЗаданы() Экспорт
	ТекстОшибки = "";
	Попытка
		мРезультат = СтрРазделить(Неопределено, Неопределено);
	Исключение
		ТекстОшибки = ОписаниеОшибки();
	КонецПопытки;
	ЮнитТестирование.ПроверитьРавенство(1, мРезультат.Количество());
КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрСравнить() Экспорт

    Левая = "Строка";
    Правая = "Строка1";
    Результат = СтрСравнить(Левая, Правая);
    ЮнитТестирование.ПроверитьРавенство(Результат, -1);

    Левая = "Строка1";
    Правая = "Строка";
    Результат = СтрСравнить(Левая, Правая);
    ЮнитТестирование.ПроверитьРавенство(Результат, 1);

    Левая = "Строка ";
    Правая = "Строка ";
    Результат = СтрСравнить(Левая, Правая);
    ЮнитТестирование.ПроверитьРавенство(Результат, 0);

    Левая = "Строка ";
    Правая = 1;
    Результат = СтрСравнить(Левая, Правая);
    ЮнитТестирование.ПроверитьРавенство(Результат, 1);

КонецПроцедуры

Процедура ТестДолжен_ВызватьМетод_СтрСоединить() Экспорт
    Массив = Новый Массив;
    Массив.Добавить("Строка1");
    Массив.Добавить("Строка2");

    Результат = СтрСоединить(Массив);
    ЮнитТестирование.ПроверитьРавенство(Результат, "Строка1Строка2");

    Разделитель = " ";
    Результат = СтрСоединить(Массив, Разделитель);
    ЮнитТестирование.ПроверитьРавенство(Результат, "Строка1" + Разделитель + "Строка2");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьСтрЧислоСтрок() Экспорт
    СтрокаДляТеста = "Это одна строка";

	ЮнитТестирование.ПроверитьРавенство(1, СтрЧислоСтрок(СтрокаДляТеста));

	СтрокаДляТеста = СтрокаДляТеста + "
	|Это две строки
	|И даже три строки";

	ЮнитТестирование.ПроверитьРавенство(3, СтрЧислоСтрок(СтрокаДляТеста));
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСтрНайти() Экспорт
    ГдеИскать = "Строка Строки Строчки Стр";
    ЧтоИскать = "Строки";
    Индекс = СтрНайти(ГдеИскать, ЧтоИскать);
    ЮнитТестирование.ПроверитьРавенство(8, Индекс);

    ЧтоИскать = "Стр";
    Индекс = СтрНайти(ГдеИскать, ЧтоИскать);
    ЮнитТестирование.ПроверитьРавенство(1, Индекс);
    Сообщить(Индекс);

    Индекс = СтрНайти(ГдеИскать, ЧтоИскать, НаправлениеПоиска.СКонца);
    ЮнитТестирование.ПроверитьРавенство(23, Индекс);

    // TODO: протестировать индекс начала поиска и порядок срабатывания
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСтрНачинаетсяС() Экспорт
    ГдеИскать = "МойСловарь";
    ЧтоИскать = "Мой";
    ЮнитТестирование.ПроверитьИстину(СтрНачинаетсяС(ГдеИскать, ЧтоИскать));

    ЧтоИскать = "НеМой";
    ЮнитТестирование.ПроверитьЛожь(СтрНачинаетсяС(ГдеИскать, ЧтоИскать));

    ЮнитТестирование.ПроверитьЛожь(СтрНачинаетсяС("", ЧтоИскать));

    // TODO: проверить СтрНачинаетсяС(ГдеИскать, "")
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСтрЧислоВхожденийВхожденийНет() Экспорт
	ГдеИскать = "Здесь был Ежик. Ежик любит грибы, Я Ежик!!!";
	ЧтоИскать = "колбаса";

	ЮнитТестирование.ПроверитьРавенство(0, СтрЧислоВхождений(ГдеИскать, ЧтоИскать));
КонецПроцедуры

Процедура ТестДолжен_ПроверитьСтрЧислоВхождений() Экспорт
	ГдеИскать = "Здесь был Ежик. Ежик любит грибы, Я Ежик!!!";
	ЧтоИскать = "Ежик";

	ЮнитТестирование.ПроверитьРавенство(3, СтрЧислоВхождений(ГдеИскать, ЧтоИскать));
КонецПроцедуры