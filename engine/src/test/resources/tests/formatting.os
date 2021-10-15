Перем юТест;

Функция ПолучитьСписокТестов(ЮнитТестирование) Экспорт

	юТест = ЮнитТестирование;

	ВсеТесты = Новый Массив;

	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеБулево");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеЛокалиЧисла");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеРазмерностиЧисло");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеРазделительДробей");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеГруппировкаРазрядов");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеЛидирующиеНули");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеПредставлениеНуля");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеПредставлениеОтрицательных");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеБольшойРазрядности");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеПустаяДата");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеЛокальнаяДата");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеЛокальнаяДатаВремя");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеДлинныйФорматЛокальнойДаты");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеЛокальноеВремя");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьПроизвольноеФорматированиеДат");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьФорматированиеДатДоДробей");

	Возврат ВсеТесты;
КонецФункции

Процедура ТестДолжен_ПроверитьФорматированиеБулево() Экспорт

	Да = Истина;
	Нет = Ложь;

	юТест.ПроверитьРавенство("Ага", Формат(Да, "БИ='Ага'; БЛ = 'Ненене'"));
	юТест.ПроверитьРавенство("Ага", Формат(Истина, "БИ='Ага'; БЛ = 'Ненене'"));
	юТест.ПроверитьРавенство("Ненене", Формат(Нет, "БИ='Ага'; БЛ = 'Ненене'"));
	юТест.ПроверитьРавенство("Ненене", Формат(Ложь, "БИ='Ага'; БЛ = 'Ненене'"));

	юТест.ПроверитьРавенство(Строка(Да), Формат(Да, "ЧГ=0"));
	юТест.ПроверитьРавенство(Строка(Нет), Формат(Нет, "ЧГ=0"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеЛокалиЧисла() Экспорт
	Число=12345.678;
	юТест.ПроверитьРавенство("12,345.678", Формат(Число, "L=en_US"));
	юТест.ПроверитьРавенство("12 345,678", Формат(Число, "Л=ru_RU"));
КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеРазмерностиЧисло() Экспорт

	Целое = 123;
	юТест.ПроверитьРавенство("123.00", Формат(Целое, "ЧЦ=5; ЧДЦ=2; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Целое, "ЧЦ=5; ЧДЦ=0; L=en_US"));
	юТест.ПроверитьРавенство("99.9", Формат(Целое, "ЧЦ=3; ЧДЦ=1; L=en_US"));
	юТест.ПроверитьРавенство("99", Формат(Целое, "ЧЦ=2; ЧДЦ=0; L=en_US"));
	юТест.ПроверитьРавенство("123.000", Формат(Целое, "ЧЦ=8; ЧДЦ=3; L=en_US"));
	юТест.ПроверитьРавенство("123.00000", Формат(Целое, "ЧЦ=-8; ЧДЦ=5; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Целое, "ЧЦ=-8; ЧДЦ=-4; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Целое, "ЧЦ=8; ЧДЦ=-4; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Целое, "ЧЦ=0; L=en_US"));
	юТест.ПроверитьРавенство("123.000", Формат(Целое, "ЧЦ=0; ЧДЦ=3; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Целое, "ЧЦ=; L=en_US"));
	юТест.ПроверитьРавенство("123.00", Формат(Целое, "ЧЦ=; ЧДЦ=2; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Целое, "ЧЦ=; ЧДЦ=; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Целое, "ЧЦ=3; ЧДЦ=-2; L=en_US"));

	Дробь = 123.345;
	юТест.ПроверитьРавенство("123.35", Формат(Дробь, "ЧЦ=5; ЧДЦ=2; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Дробь, "ЧЦ=5; ЧДЦ=0; L=en_US"));
	юТест.ПроверитьРавенство("99.9", Формат(Дробь, "ЧЦ=3; ЧДЦ=1; L=en_US"));
	юТест.ПроверитьРавенство("99", Формат(Дробь, "ЧЦ=2; ЧДЦ=0; L=en_US"));
	юТест.ПроверитьРавенство("123.3450", Формат(Дробь, "ЧЦ=8; ЧДЦ=4; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Дробь, "ЧЦ=0; L=en_US"));
	юТест.ПроверитьРавенство("123.345", Формат(Дробь, "ЧЦ=0; ЧДЦ=3; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Дробь, "ЧЦ=; L=en_US"));
	юТест.ПроверитьРавенство("123.35", Формат(Дробь, "ЧЦ=; ЧДЦ=2; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Дробь, "ЧЦ=; ЧДЦ=; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Дробь, "ЧЦ=3; ЧДЦ=-2; L=en_US"));

	Дробь = 0.123456789;
	юТест.ПроверитьРавенство("0.123457", Формат(Дробь, "ЧДЦ=6; L=en_US"));
	юТест.ПроверитьРавенство("0.12345678900", Формат(Дробь, "ЧДЦ=11; L=en_US"));
	юТест.ПроверитьРавенство("0.123457", Формат(Дробь, "ЧЦ=9; ЧДЦ=6; L=en_US"));
	юТест.ПроверитьРавенство(".123456789", Формат(Дробь, "ЧЦ=9; ЧДЦ=9; L=en_US"));
	юТест.ПроверитьРавенство("0.123456789", Формат(Дробь, "ЧЦ=10; ЧДЦ=9; L=en_US"));
	юТест.ПроверитьРавенство(".123456789", Формат(Дробь, "ЧЦ=9; ЧДЦ=10; L=en_US"));
	юТест.ПроверитьРавенство(".12346", Формат(Дробь, "ЧЦ=5; ЧДЦ=8; L=en_US"));
	юТест.ПроверитьРавенство("0.12346", Формат(Дробь, "ЧЦ=0; ЧДЦ=5; L=en_US"));
	юТест.ПроверитьРавенство("", Формат(Дробь, "ЧЦ=5; ЧДЦ=0; L=en_US"));
	юТест.ПроверитьРавенство("Z", Формат(Дробь, "ЧЦ=5; ЧДЦ=0; ЧН=Z; L=en_US"));

	Дробь = 3.123456789;
	юТест.ПроверитьРавенство("3.123457", Формат(Дробь, "ЧДЦ=6; L=en_US"));
	юТест.ПроверитьРавенство("3.12345678900", Формат(Дробь, "ЧДЦ=11; L=en_US"));
	юТест.ПроверитьРавенство("3.123457", Формат(Дробь, "ЧЦ=9; ЧДЦ=6; L=en_US"));
	юТест.ПроверитьРавенство(".999999999", Формат(Дробь, "ЧЦ=9; ЧДЦ=9; L=en_US"));
	юТест.ПроверитьРавенство("3.123456789", Формат(Дробь, "ЧЦ=10; ЧДЦ=9; L=en_US"));
	юТест.ПроверитьРавенство(".999999999", Формат(Дробь, "ЧЦ=9; ЧДЦ=10; L=en_US"));
	юТест.ПроверитьРавенство(".99999", Формат(Дробь, "ЧЦ=5; ЧДЦ=8; L=en_US"));
	юТест.ПроверитьРавенство("3.12346", Формат(Дробь, "ЧЦ=0; ЧДЦ=5; L=en_US"));
	юТест.ПроверитьРавенство("3", Формат(Дробь, "ЧЦ=5; ЧДЦ=0; L=en_US"));
	юТест.ПроверитьРавенство("3", Формат(Дробь, "ЧЦ=5; ЧДЦ=0; ЧН=Z; L=en_US"));

	юТест.ПроверитьРавенство("3.12346", Формат(Дробь, "ЧДЦ=4;ЧДЦ=6;ЧДЦ=5; L=en_US"));

	Большое = 123456789.12345678;
	юТест.ПроверитьРавенство("123,456,789.12", Формат(Большое, "ЧЦ=12; ЧДЦ=2; L=en_US"));
	юТест.ПроверитьРавенство("99,999,999.99", Формат(Большое, "ЧЦ=10; ЧДЦ=2; L=en_US"));
	юТест.ПроверитьРавенство("99,999,999.9999", Формат(Большое, "ЧЦ=12; ЧДЦ=4; L=en_US"));
	юТест.ПроверитьРавенство("123,456,789.1235", Формат(Большое, "ЧЦ=13; ЧДЦ=4; L=en_US"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеРазделительДробей() Экспорт

	Дробь = 123.1234;
	юТест.ПроверитьРавенство("123/12", Формат(Дробь, "ЧЦ=5; ЧДЦ=2; ЧРД='/'"));
	юТест.ПроверитьРавенство("123:12", Формат(Дробь, "ЧЦ=5; ЧДЦ=2; ЧРД=':?'"));
	Дробь = 123456789.123;
	юТест.ПроверитьРавенство("123,456,7890123", Формат(Дробь, "ЧРД=0; L=en_US"));
	юТест.ПроверитьРавенство("123,456,789.123", Формат(Дробь, "ЧРД=; L=en_US"));
	юТест.ПроверитьРавенство("123,456,789.123", Формат(Дробь, "ЧРД=''; L=en_US"));
	юТест.ПроверитьРавенство("123,456,789,123", Формат(Дробь, "ЧРД=,; L=en_US"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеГруппировкаРазрядов() Экспорт

	Число=123456789;
	//юТест.ПроверитьРавенство("123,456,789", Формат(Число, "ЧГ; L=en_US"));
	юТест.ПроверитьРавенство("123456789", Формат(Число, "ЧГ=; L=en_US"));
	юТест.ПроверитьРавенство("123456789", Формат(Число, "ЧГ=0; L=en_US"));
	юТест.ПроверитьРавенство("12345,6789", Формат(Число, "ЧГ=4; L=en_US"));
	юТест.ПроверитьРавенство("1,2345,6789", Формат(Число, "ЧГ=4,4; L=en_US"));
	юТест.ПроверитьРавенство("1,23,45,6789", Формат(Число, "ЧГ=4,2; L=en_US"));
	юТест.ПроверитьРавенство("123456789", Формат(Число, "ЧГ=0,4; L=en_US"));
	юТест.ПроверитьРавенство("12345,6789", Формат(Число, "ЧГ=,4; L=en_US"));
	юТест.ПроверитьРавенство("1,2,3,4,5,6789", Формат(Число, "ЧГ=4,1,2; L=en_US"));
	юТест.ПроверитьРавенство("1,2,3,4,5,6789", Формат(Число, "ЧГ=4.1,2; L=en_US"));
	юТест.ПроверитьРавенство("1,2,3,4,5,6789", Формат(Число, "ЧГ=-4.1+2; L=en_US"));

	Число=123456789.1234;
	юТест.ПроверитьРавенство("1,23,45,6789.1234", Формат(Число, "ЧГ=4,2;ЧРГ=; L=en_US"));
	юТест.ПроверитьРавенство("102304506789.1234", Формат(Число, "ЧГ=4,2;ЧРГ=0; L=en_US"));
	юТест.ПроверитьРавенство("1.23.45.6789.1234", Формат(Число, "ЧГ=4,2;ЧРГ='.'; L=en_US"));
	юТест.ПроверитьРавенство("1,23,45,6789.1234", Формат(Число, "ЧГ=4,2;ЧРГ=','; L=en_US"));
	юТест.ПроверитьРавенство("1:23:45:6789.1234", Формат(Число, "ЧГ=4,2;ЧРГ=:; L=en_US"));
	юТест.ПроверитьРавенство("1;23;45;6789.1234", Формат(Число, "ЧГ=4,2;ЧРГ=';,'; L=en_US"));
	юТест.ПроверитьРавенство("1,23,45,6789,1234", Формат(Число, "ЧГ=4,2;ЧРГ=',';ЧРД=','; L=en_US"));

	Число=123456789123456789.12;
	юТест.ПроверитьРавенство("123456,789123456789.12", Формат(Число, "ЧГ=12; L=en_US"));
	юТест.ПроверитьРавенство("12,3456,789123456789.12", Формат(Число, "ЧГ=12,4; L=en_US"));
	юТест.ПроверитьРавенство("123456,789123456789.12", Формат(Число, "ЧГ=12,10; L=en_US"));
	юТест.ПроверитьРавенство("123456,789123456789.12", Формат(Число, "ЧГ=12,0,3; L=en_US"));
	юТест.ПроверитьРавенство("12.3456.789123456789.12", Формат(Число, "ЧГ=12,4;ЧРГ='.'; L=en_US"));
	юТест.ПроверитьРавенство("12,3456,789123456789.12", Формат(Число, "ЧГ=12,4;ЧРГ=','; L=en_US"));
	юТест.ПроверитьРавенство("12,3456,789123456789,12", Формат(Число, "ЧГ=12,4;ЧРГ=',';ЧРД=','; L=en_US"));

	юТест.ПроверитьРавенство("99999999,999999", Формат(Число, "ЧГ=6;ЧЦ=14; L=en_US"));
	юТест.ПроверитьРавенство("99,999999999999", Формат(Число, "ЧГ=12;ЧЦ=14; L=en_US"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеЛидирующиеНули() Экспорт

	Число=123;
	юТест.ПроверитьРавенство("0,000,123", Формат(Число, "ЧЦ=7; ЧВН=1; L=en_US"));
	юТест.ПроверитьРавенство("0,000,123", Формат(Число, "ЧЦ=7; ЧВН=0; L=en_US"));
	юТест.ПроверитьРавенство("0,000,123", Формат(Число, "ЧЦ=7; ЧВН=; L=en_US"));
	юТест.ПроверитьРавенство("0,000,123", Формат(Число, "ЧЦ=7; ЧВН=9; L=en_US"));
	юТест.ПроверитьРавенство("123", Формат(Число, "ЧЦ=7; ЧВН; L=en_US"));
	юТест.ПроверитьРавенство("000,000,000,123", Формат(Число, "ЧЦ=12; ЧВН=1; L=en_US"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеПредставлениеНуля() Экспорт

	юТест.ПроверитьРавенство("", Формат(0, "ЧЦ=7"));
	юТест.ПроверитьРавенство("0", Формат(0, "ЧН=0;"));
	юТест.ПроверитьРавенство("НОЛЬ", Формат(0, "ЧН='НОЛЬ';"));
	юТест.ПроверитьРавенство("0", Формат(0, "ЧН='';"));
	юТест.ПроверитьРавенство(" ", Формат(0, "ЧН=' ';"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеПредставлениеОтрицательных() Экспорт

	Число = -123234.811;

	юТест.ПроверитьРавенство("(123234.811)", Формат(Число, "ЧО=0; ЧГ=0; L=en_US"));
	юТест.ПроверитьРавенство("-123,234.811", Формат(Число, "ЧО=1; L=en_US"));
	юТест.ПроверитьРавенство("- 123,234.811", Формат(Число, "ЧО=2; L=en_US"));
	юТест.ПроверитьРавенство("123,234.811-", Формат(Число, "ЧО=3; L=en_US"));
	юТест.ПроверитьРавенство("123,234.811 -", Формат(Число, "ЧО=4; L=en_US"));
	юТест.ПроверитьРавенство("-123,234.811", Формат(Число, "ЧО=7; L=en_US"));
	юТест.ПроверитьРавенство("-123,234.811", Формат(Число, "ЧО=-2; L=en_US"));

	юТест.ПроверитьРавенство("(99999)", Формат(Число, "ЧО=0;ЧЦ=5; ЧГ=0; L=en_US"));
	юТест.ПроверитьРавенство("-99,999", Формат(Число, "ЧО=1;ЧЦ=5; L=en_US"));
	юТест.ПроверитьРавенство("- 99,999", Формат(Число, "ЧО=2;ЧЦ=5; L=en_US"));
	юТест.ПроверитьРавенство("99,999-", Формат(Число, "ЧО=3;ЧЦ=5; L=en_US"));
	юТест.ПроверитьРавенство("99,999 -", Формат(Число, "ЧО=4;ЧЦ=5; L=en_US"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеБольшойРазрядности() Экспорт

	Число = 54321234567890.0123456789;

	юТест.ПроверитьРавенство(".9999999999999999999999", Формат(Число, "ЧЦ=22;ЧВН=1;ЧГ=10,10;ЧДЦ=40; L=en_US"));
	юТест.ПроверитьРавенство(".99999999999999999999999999999999", Формат(Число, "ЧЦ=32;ЧВН=1;ЧГ=10,10;ЧДЦ=40; L=en_US"));
	юТест.ПроверитьРавенство("99.9999999999999999999999999999999999999999", Формат(Число, "ЧЦ=42;ЧВН=1;ЧГ=10,10;ЧДЦ=40; L=en_US"));
	юТест.ПроверитьРавенство("99,9999999999.9999999999999999999999999999999999999999", Формат(Число, "ЧЦ=52;ЧВН=1;ЧГ=10,10;ЧДЦ=40; L=en_US"));
	юТест.ПроверитьРавенство("00,0000000000,0000000000,0000000000,0000000000,0000000000,0000005432,1234567890.0123456789000000000000000000000000000000",
	  Формат(Число, "ЧЦ=112;ЧВН=1;ЧГ=10,10;ЧДЦ=40; L=en_US"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеПустаяДата() Экспорт

	ПустаяДата = '00010101';

	юТест.ПроверитьРавенство("", Формат(ПустаяДата, "ЧГ=0"));
	юТест.ПроверитьРавенство("--", Формат(ПустаяДата, "ДП=--"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеЛокальнаяДата() Экспорт

	Эталон = '20140207122517';
	Эталон2 = '20141217122517';

	юТест.ПроверитьРавенство("07.02.2014", Формат(Эталон, "ДЛФ=Д; Л=ru_RU"));
	юТест.ПроверитьРавенство("17.12.2014", Формат(Эталон2, "ДЛФ=Д; Л=ru_RU"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеЛокальнаяДатаВремя() Экспорт

	Эталон = '20140207022517';
	Эталон2 = '20141217022517';

	юТест.ПроверитьРавенство("07.02.2014 2:25:17", Формат(Эталон, "ДЛФ=ДВ; Л=ru_RU"));
	юТест.ПроверитьРавенство("17.12.2014 2:25:17", Формат(Эталон2, "ДЛФ=ДВ; Л=ru_RU"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеЛокальноеВремя() Экспорт

	Эталон = '20140207022517';

	юТест.ПроверитьРавенство("2:25:17", Формат(Эталон, "ДЛФ=В; Л=ru_RU"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеДлинныйФорматЛокальнойДаты() Экспорт

	Эталон = '20140207022517';

	// внимание, дата и время разделены неразрывным пробелом
	юТест.ПроверитьРавенство("7 февраля 2014 г.", Формат(Эталон, "ДЛФ=ДД; Л=ru_RU"), "Только дата");
	юТест.ПроверитьРавенство("7 февраля 2014 г. 2:25:17", Формат(Эталон, "ДЛФ=ДДВ; Л=ru_RU"), "Дата время");

КонецПроцедуры

Процедура ТестДолжен_ПроверитьПроизвольноеФорматированиеДат() Экспорт

	Эталон = '20140207020805';
	Эталон2 = '20141217020805';

	юТест.ПроверитьРавенство("02:08:05", Формат(Эталон, "ДФ=чч:мм:сс"));
	юТест.ПроверитьРавенство("02:08:05", Формат(Эталон, "ДФ='чч:мм:сс'"));
	юТест.ПроверитьРавенство("2:8:5", Формат(Эталон, "ДФ=ч:м:с"));
	юТест.ПроверитьРавенство("*2-8-5*", Формат(Эталон, "ДФ=*ч-м-с*"));

	юТест.ПроверитьРавенство("07-02-14/02 08 05", Формат(Эталон, "ДФ='дд-ММ-гг/чч мм сс'"));

	юТест.ПроверитьРавенство("7-2-14", Формат(Эталон, "ДФ='д-М-гг'"));
	юТест.ПроверитьРавенство("17-12-14", Формат(Эталон2, "ДФ='д-М-гг'"));

	юТест.ПроверитьРавенство("7-Feb-2014", Формат(Эталон, "ДФ='д-МММ-гггг'; Л=en_US"));
	юТест.ПроверитьРавенство("17-Dec-2014", Формат(Эталон2, "ДФ='д-МММ-гггг'; Л=en_US"));

	юТест.ПроверитьРавенство("07-February-2014", Формат(Эталон, "ДФ='дд-ММММ-гггг'; Л=en_US"));
	юТест.ПроверитьРавенство("07 February 2014", Формат(Эталон, "ДФ='дд ММММ гггг'; Л=en_US"));
	юТест.ПроверитьРавенство("17-December-2014", Формат(Эталон2, "ДФ='дд-ММММ-гггг'; Л=en_US"));
	юТест.ПроверитьРавенство("17 December 2014", Формат(Эталон2, "ДФ='дд ММММ гггг'; Л=en_US"));

КонецПроцедуры

Процедура ТестДолжен_ПроверитьФорматированиеДатДоДробей() Экспорт

	ДатаПроверяемая = ТекущаяДата();
	СтрокаЭталон = Формат(ДатаПроверяемая, "ДФ=сс") + ".";
	ЧислоЭталон = Число(СтрокаЭталон);

	ПредельноеКоличествоДопустимыхДробныхЗнаков = 6;

	СтрокаФормата = "ДФ=сс.";
	Для КоличествоЗнаков = 1 По ПредельноеКоличествоДопустимыхДробныхЗнаков Цикл

		СтрокаФормата = СтрокаФормата + "р";
		ФорматированнаяДата = Формат(ДатаПроверяемая, СтрокаФормата);
		юТест.ПроверитьИстину(СтрНачинаетсяС(ФорматированнаяДата, СтрокаЭталон));

		ПроверяемоеЧисло = Число(ФорматированнаяДата);

		юТест.ПроверитьИстину(ПроверяемоеЧисло >= ЧислоЭталон, "Больше знаков - больше число");

	КонецЦикла;

КонецПроцедуры
