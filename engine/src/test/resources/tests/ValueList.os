Перем юТест;

Функция ПолучитьСписокТестов(ЮнитТестирование) Экспорт

	юТест = ЮнитТестирование;

	ВсеТесты = Новый Массив;
	ВсеТесты.Добавить("ТестДолжен_ПроверитьСозданиеСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьДобавлениеЭлементов");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьДоступКСвойствамЭлементаСписка");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьЗаписьСвойствЭлементаСписка");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьИтерациюПоСписку");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьДоступПоИндексу");

	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодИндексСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодВставитьСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодСдвинутьСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодНайтиСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодУдалитьСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодСортироватьПоЗначениюСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодСортироватьПоПредставлениюСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодВыгрузитьЗначенияСпискаЗначений");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМетодЗагрузитьЗначенияСпискаЗначений");
	Возврат ВсеТесты;

КонецФункции

Процедура ТестДолжен_ПроверитьСозданиеСпискаЗначений() Экспорт
	СЗ = Новый СписокЗначений;
	юТест.ПроверитьРавенство(Тип("СписокЗначений"), ТипЗнч(СЗ));
КонецПроцедуры

Процедура ТестДолжен_ПроверитьДобавлениеЭлементов() Экспорт
	СЗ = Новый СписокЗначений;
	СЗ.Добавить("Один");
	СЗ.Добавить("Два");
	юТест.ПроверитьРавенство(2, СЗ.Количество());
	юТест.ПроверитьРавенство(Тип("ЭлементСпискаЗначений"), ТипЗнч(СЗ.Получить(0)));
КонецПроцедуры

Процедура ТестДолжен_ПроверитьДоступКСвойствамЭлементаСписка() Экспорт

	СЗ = Новый СписокЗначений;
	СЗ.Добавить(1,"Представление");
	СЗ.Добавить(2,"Представление2", Истина);
	СЗ.Добавить(3,"Представление3", Истина, "Тут должна быть картинка, но сейчас может быть любое значение");
	СЗ.Добавить(1);

	Элемент = СЗ[0];
	юТест.ПроверитьРавенство(1, Элемент.Значение, "Значение элемента 0 должно быть установлено");
	юТест.ПроверитьРавенство("Представление", Элемент.Представление, "Представление элемента 0 должно быть установлено");
	юТест.ПроверитьРавенство(Ложь, Элемент.Пометка, "Пометка элемента 0 не должна быть установлена");
	юТест.ПроверитьРавенство(Неопределено, Элемент.Картинка, "Картинка элемента 0 не должна быть установлена");

	Элемент = СЗ[1];
	юТест.ПроверитьРавенство(2, Элемент.Значение, "Значение элемента 1 должно быть установлено");
	юТест.ПроверитьРавенство("Представление2", Элемент.Представление, "Представление элемента 1 должно быть установлено");
	юТест.ПроверитьРавенство(Истина, Элемент.Пометка, "Пометка элемента 1 должна быть установлена");

	Элемент = СЗ[2];
	юТест.ПроверитьРавенство(3, Элемент.Значение, "Значение элемента 2 должно быть установлено");
	юТест.ПроверитьРавенство("Представление3", Элемент.Представление, "Представление элемента 2 должно быть установлено");
	юТест.ПроверитьРавенство(Истина, Элемент.Пометка, "Пометка элемента 2 должна быть установлена");
	юТест.ПроверитьРавенство("Тут должна быть картинка, но сейчас может быть любое значение", Элемент.Картинка, "Картинка элемента 2 должна быть установлена");

	юТест.ПроверитьРавенство("", СЗ[3].Представление, "Должно быть пустое представление по умолчанию");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьЗаписьСвойствЭлементаСписка() Экспорт
	СЗ = Новый СписокЗначений;
	Элемент = СЗ.Добавить(1);

	Элемент.Значение = 2;
	Элемент.Представление = "Привет";
	Элемент.Пометка = Истина;
	Элемент.Картинка = "---";

	юТест.ПроверитьРавенство(2, Элемент.Значение, "Значение элемента должно быть установлено");
	юТест.ПроверитьРавенство("Привет", Элемент.Представление, "Представление элемента должно быть установлено");
	юТест.ПроверитьРавенство(Истина, Элемент.Пометка, "Пометка элемента должна быть установлена");
	юТест.ПроверитьРавенство("---", Элемент.Картинка, "Картинка элемента должна быть установлена");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьИтерациюПоСписку() Экспорт
	СЗ = Новый СписокЗначений;
	СЗ.Добавить(1);
	СЗ.Добавить(2);
	СЗ.Добавить(3);
	СЗ.Добавить(4);
	Сч = 0;
	Для Каждого Элемент Из СЗ Цикл
		Сч = Сч + 1;
	КонецЦикла;

	юТест.ПроверитьРавенство(4, Сч);

КонецПроцедуры

Процедура ТестДолжен_ПроверитьДоступПоИндексу() Экспорт
	СЗ = Новый СписокЗначений;
	СЗ.Добавить(1);

	юТест.ПроверитьРавенство(1, СЗ[0].Значение);
	Попытка
		СЗ[0] = СЗ[0];
	Исключение
		юТест.ПроверитьРавенство("Индексированное значение доступно только для чтения", ИнформацияОбОшибке().Описание);
		Возврат;
	КонецПопытки;

	ВызватьИсключение "Должно было быть выброшено исключение, но это не произошло";

КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодИндексСпискаЗначений() Экспорт

	СЗ = Новый СписокЗначений;

	СЗ.Добавить(1);
	СЗ.Добавить(2);
	СЗ.Добавить(3);
	СЗ.Добавить(4);

	юТест.ПроверитьРавенство(СЗ.Индекс(СЗ[0]), 0, "Метод Индекс() списка значений");
	юТест.ПроверитьРавенство(СЗ.Индекс(СЗ[1]), 1, "Метод Индекс() списка значений");
	юТест.ПроверитьРавенство(СЗ.Индекс(СЗ[2]), 2, "Метод Индекс() списка значений");
	юТест.ПроверитьРавенство(СЗ.Индекс(СЗ[3]), 3, "Метод Индекс() списка значений");

	ДругойСЗ = Новый СписокЗначений;
	ДругойСЗ.Добавить(1);

	юТест.ПроверитьРавенство(СЗ.Индекс(ДругойСЗ[0]), -1, "Метод Индекс() списка значений: несуществующий элемент");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодВставитьСпискаЗначений() Экспорт

	СЗ = Новый СписокЗначений;

	Э3 = СЗ.Вставить(0, 3); // Вставка в пустой список
	Э1 = СЗ.Вставить(0, 1); // Вставка в начало
	Э2 = СЗ.Вставить(1, 2); // Вставка в середину
	Э4 = СЗ.Вставить(3, 4); // Вставка в конец

	юТест.ПроверитьРавенство(СЗ.Индекс(Э1), 0, "Метод Вставить() списка значений");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э2), 1, "Метод Вставить() списка значений");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э3), 2, "Метод Вставить() списка значений");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э4), 3, "Метод Вставить() списка значений");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодУдалитьСпискаЗначений() Экспорт

	СЗ = Новый СписокЗначений;
	Для Инд = 1 По 5 Цикл
		СЗ.Добавить(Инд);
	КонецЦикла;
	Э1 = СЗ[0];
	Э2 = СЗ[1];
	Э3 = СЗ[2];
	Э4 = СЗ[3];
	Э5 = СЗ[4];

	СЗ.Удалить(1);
	юТест.ПроверитьРавенство(СЗ.Количество(),4, "Удаление по индексу");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э2),-1, "Удаление по индексу");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э4),2, "Удаление по индексу");

	СЗ.Удалить(Э1);
	юТест.ПроверитьРавенство(СЗ.Количество(),3, "Удаление элемента");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э1),-1, "Удаление элемента");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э4),1, "Удаление элемента");

	СЗ.Удалить("1");
	юТест.ПроверитьРавенство(СЗ.Количество(),2, "Удаление по индексу типа строка");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э4),-1, "Удаление по индексу типа строка");
	юТест.ПроверитьРавенство(СЗ.Индекс(Э5),1, "Удаление по индексу типа строка");

	// Проверим ошибки
	Ошибка = "Удаление с неверным индексом";
	Попытка
		СЗ.Удалить(10);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Неверное значение аргумента"); // В ОСкрипте: Значение индекса выходит за пределы диапазона

	Ошибка = "Удаление несуществующего элемента";
	ДругойСЗ = Новый СписокЗначений;
	ДругойСЗ.Добавить(1);
	Попытка
		СЗ.Удалить(ДругойСЗ[0]);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Неверное значение аргумента"); // В ОСкрипте: Элемент не принадлежит списку значений

	Ошибка = "Удаление с неверным типом параметра";
	Попытка
		СЗ.Удалить("ё");
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Преобразование к типу 'Число' не поддерживается"); // В ОСкрипте: Неверный тип аргумента

КонецПроцедуры


Функция ПроверитьПорядок(Знач СЗ, Знач П1,
	Знач П2 = Неопределено,
	Знач П3 = Неопределено,
	Знач П4 = Неопределено,
	Знач П5 = Неопределено)

	Массив = Новый Массив;
	Массив.Добавить(П1);

	Если П2 <> Неопределено Тогда
		Массив.Добавить(П2);
	КонецЕсли;
	Если П3 <> Неопределено Тогда
		Массив.Добавить(П3);
	КонецЕсли;
	Если П4 <> Неопределено Тогда
		Массив.Добавить(П4);
	КонецЕсли;
	Если П5 <> Неопределено Тогда
		Массив.Добавить(П5);
	КонецЕсли;

	Для Инд = 0 По Массив.ВГраница() Цикл

		Если СЗ[Инд].Значение <> Массив[Инд] Тогда
			Возврат Ложь;
		КонецЕсли;

	КонецЦикла;

	Возврат Истина;

КонецФункции

Процедура ТестДолжен_ПроверитьМетодСдвинутьСпискаЗначений() Экспорт

	СЗ = Новый СписокЗначений;

	Для Инд = 1 По 5 Цикл
		СЗ.Добавить(Инд);
	КонецЦикла;

	С1 = СЗ[0];
	С2 = СЗ[1];
	С3 = СЗ[2];
	С4 = СЗ[3];
	С5 = СЗ[4];

	// Проверим обычное перемещение
	СЗ.Сдвинуть(С2, -1);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 2, 1, 3, 4, 5), "Сдвиг вверх");

	СЗ.Сдвинуть(С1, 2);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 2, 3, 4, 1, 5), "Сдвиг вниз");

	СЗ.Сдвинуть(3, -2);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 2, 1, 3, 4, 5), "Сдвиг по индексу вверх");

	СЗ.Сдвинуть(1, 2);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 2, 3, 4, 1, 5), "Сдвиг по индексу вниз");

	// Проверим приведение типа
	СЗ.Сдвинуть("2", 2);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 2, 3, 1, 5, 4), "Сдвиг, индекс - строка");

	СЗ.Сдвинуть("1", "2");
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 2, 1, 5, 3, 4), "Сдвиг, индекс и смещение - строки");

	// Проверим крайние случаи
	СЗ.Сдвинуть(0, 4);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 1, 5, 3, 4, 2), "Сдвиг по индексу вниз");

	СЗ.Сдвинуть(4, -4);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 2, 1, 5, 3, 4), "Сдвиг по индексу вниз");

	// Проверим ошибки
	Ошибка = "Сдвиг за пределы вниз";
	Попытка
		СЗ.Сдвинуть(0, 5);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Значение индекса выходит за пределы диапазона"); // В ОСкрипте: Неверное значение аргумента номер 2

	Ошибка = "Сдвиг за пределы вверх";
	Попытка
		СЗ.Сдвинуть(1, -3);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Значение индекса выходит за пределы диапазона"); // В ОСкрипте: Неверное значение аргумента номер 2

	Ошибка = "Сдвиг с неверным индексом";
	Попытка
		СЗ.Сдвинуть(10, 2);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Значение индекса выходит за пределы диапазона");

	Ошибка = "Сдвиг несуществующего элемента";
	ДругойСЗ = Новый СписокЗначений;
	ДругойСЗ.Добавить(1);
	Попытка
		СЗ.Сдвинуть(ДругойСЗ[0], 2);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Неверное значение аргумента"); // В ОСкрипте: Элемент не принадлежит списку значений

	Ошибка = "Сдвиг с неверным типом параметра";
	Попытка
		СЗ.Сдвинуть("ё", 2);
	Исключение
		Ошибка = ИнформацияОбОшибке().Описание;
	КонецПопытки;
	юТест.ПроверитьРавенство(Ошибка, "Преобразование к типу 'Число' не поддерживается"); // В ОСкрипте: Неверный тип аргумента

КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодСортироватьПоЗначениюСпискаЗначений() Экспорт

	СЗ = Новый СписокЗначений;
	СЗ.Добавить(4);
	СЗ.Добавить(3);
	СЗ.Добавить(2);
	СЗ.Добавить(1);

	СЗ.СортироватьПоЗначению();
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 1, 2, 3, 4), "Сортировка по-умолчанию");

	СЗ.СортироватьПоЗначению(НаправлениеСортировки.Убыв);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 4, 3, 2, 1), "Сортировка по убыванию");

	СЗ.СортироватьПоЗначению(НаправлениеСортировки.Возр);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 1, 2, 3, 4), "Сортировка по возрастанию");
	
	СЗ = Новый СписокЗначений;
	СЗ.Добавить("аааааб");
	СЗ.Добавить("аааааа");
	СЗ.Добавить("Бааааа");
	
	СЗ.СортироватьПоЗначению();
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, "аааааа", "аааааб", "Бааааа"), "Сортировка по регистру");

КонецПроцедуры


Процедура ТестДолжен_ПроверитьМетодСортироватьПоПредставлениюСпискаЗначений() Экспорт

	СЗ = Новый СписокЗначений;
	СЗ.Добавить(4,"14");
	СЗ.Добавить(3,"3");
	СЗ.Добавить(2,"2");
	СЗ.Добавить(1,"1");

	СЗ.СортироватьПоПредставлению();
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 1, 4, 2, 3), "Сортировка по-умолчанию");

	СЗ.СортироватьПоПредставлению(НаправлениеСортировки.Убыв);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 3, 2, 4, 1), "Сортировка по убыванию");

	СЗ.СортироватьПоПредставлению(НаправлениеСортировки.Возр);
	юТест.ПроверитьИстину(ПроверитьПорядок(СЗ, 1, 4, 2, 3), "Сортировка по возрастанию");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодНайтиСпискаЗначений() Экспорт

	СЗ = Новый СписокЗначений;
	Э0 = СЗ.Добавить(1.2);
	Э1 = СЗ.Добавить("абв");
	Э2 = СЗ.Добавить('20161009');
	Э3 = СЗ.Добавить(Истина);
	Э4 = СЗ.Добавить(Null);
	Э5 = СЗ.Добавить(Неопределено);

	юТест.ПроверитьРавенство(СЗ.НайтиПоЗначению(1.2)         , Э0, "Поиск числа");
	юТест.ПроверитьРавенство(СЗ.НайтиПоЗначению("абв")       , Э1, "Поиск строки");
	юТест.ПроверитьРавенство(СЗ.НайтиПоЗначению("АБВ")       , Неопределено, "Поиск строки в другом регистре");
	юТест.ПроверитьРавенство(СЗ.НайтиПоЗначению('20161009')  , Э2, "Поиск даты");
	юТест.ПроверитьРавенство(СЗ.НайтиПоЗначению(Истина)      , Э3, "Поиск Истины");
	юТест.ПроверитьРавенство(СЗ.НайтиПоЗначению(Null)        , Э4, "Поиск Null");
	юТест.ПроверитьРавенство(СЗ.НайтиПоЗначению(Неопределено), Э5, "Поиск Неопределено");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодВыгрузитьЗначенияСпискаЗначений() Экспорт

	СЗ = Новый СписокЗначений;
	Э0 = СЗ.Добавить(1.2);
	Э1 = СЗ.Добавить("абв");
	Э2 = СЗ.Добавить('20161009');
	Э3 = СЗ.Добавить(Истина);
	Э4 = СЗ.Добавить(Null);
	Э5 = СЗ.Добавить(Неопределено);

	Значения = СЗ.ВыгрузитьЗначения();

	юТест.ПроверитьРавенство(Значения.Количество() , 6, "Количество значений");
	юТест.ПроверитьРавенство(Значения[0]           , 1.2, "Проверка значения");
	юТест.ПроверитьРавенство(Значения[1]           , "абв", "Проверка значения");
	юТест.ПроверитьРавенство(Значения[2]           , '20161009', "Проверка значения");
	юТест.ПроверитьРавенство(Значения[3]           , Истина, "Проверка значения");
	юТест.ПроверитьРавенство(Значения[4]           , Null, "Проверка значения");
	юТест.ПроверитьРавенство(Значения[5]           , Неопределено, "Проверка значения");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьМетодЗагрузитьЗначенияСпискаЗначений() Экспорт
	
	МассивЗначений = Новый Массив;
	МассивЗначений.Добавить(1.2);
	МассивЗначений.Добавить("абв");
	МассивЗначений.Добавить('20161009');
	МассивЗначений.Добавить(Истина);
	МассивЗначений.Добавить(Null);
	МассивЗначений.Добавить(Неопределено);

	СЗ = Новый СписокЗначений;
	СЗ.ЗагрузитьЗначения(МассивЗначений);

	юТест.ПроверитьРавенство(СЗ.Количество() , 6, "Количество значений");

КонецПроцедуры