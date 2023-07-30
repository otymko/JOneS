Перем ЮнитТестирование;

Функция ПолучитьСписокТестов(Тестировщик) Экспорт
	ЮнитТестирование = Тестировщик;

	ВсеТесты = Новый Массив;
	ВсеТесты.Добавить("ТестДолжен_ПроверитьЦел");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьОкр");
	ВсеТесты.Добавить("ТестДолжен_ПроверитьМатематическиеФункции");

	Возврат ВсеТесты;
КонецФункции

&Тест
Процедура ТестДолжен_ПроверитьЦел() Экспорт

	ЮнитТестирование.ПроверитьРавенство( Цел(1234.567), 1234);
	ЮнитТестирование.ПроверитьРавенство( Цел(1234), 1234);
	ЮнитТестирование.ПроверитьРавенство( Цел(-1234.567), -1234);
	ЮнитТестирование.ПроверитьРавенство( Цел(-1234), -1234);
	ЮнитТестирование.ПроверитьРавенство( Цел(0), 0);

КонецПроцедуры

&Тест
Процедура ТестДолжен_ПроверитьОкр() Экспорт

	ЮнитТестирование.ПроверитьРавенство( Окр(1234.567), 1235);
	ЮнитТестирование.ПроверитьРавенство( Окр(2234.567,2), 2234.57);
	ЮнитТестирование.ПроверитьРавенство( Окр(3234,-2), 3200);
	ЮнитТестирование.ПроверитьРавенство( Окр(4234.567,-2), 4200);

	ЮнитТестирование.ПроверитьРавенство( Окр(5234.55,1), 5234.6);
	ЮнитТестирование.ПроверитьРавенство( Окр(6234.55,1,0), 6234.5);
	ЮнитТестирование.ПроверитьРавенство( Окр(7234.55,1,1), 7234.6);
	ЮнитТестирование.ПроверитьРавенство( Окр(8234.555,1,0), 8234.6);
	ЮнитТестирование.ПроверитьРавенство( Окр(9234.55,1,-4), 9234.6);

	ЮнитТестирование.ПроверитьРавенство( Окр(10250,-2,0), 10200);
	ЮнитТестирование.ПроверитьРавенство( Окр(11251,-2,0), 11300);
	ЮнитТестирование.ПроверитьРавенство( Окр(12251,-2,1), 12300);
	ЮнитТестирование.ПроверитьРавенство( Окр(-13250,-2,0), -13200);
	ЮнитТестирование.ПроверитьРавенство( Окр(-14251,-2,0), -14300);
	ЮнитТестирование.ПроверитьРавенство( Окр(-15250,-2,1), -15300);

	ЮнитТестирование.ПроверитьРавенство( Окр(-16234.55,1,0), -16234.5);
	ЮнитТестирование.ПроверитьРавенство( Окр(-17234.55,1,1), -17234.6);
	ЮнитТестирование.ПроверитьРавенство( Окр(-18234.551,1,0), -18234.6);
	ЮнитТестирование.ПроверитьРавенство( Окр(-19234.551,1,1), -19234.6);

	ЮнитТестирование.ПроверитьРавенство( Окр("20234.5","0",-1.9), 20235);

КонецПроцедуры

&Тест
Процедура ТестДолжен_ПроверитьМатематическиеФункции() Экспорт

	ЮнитТестирование.ПроверитьРавенство( 0, Log(1), "Log");
	ЮнитТестирование.ПроверитьРавенство( 5.545177444479562, Log(256), "Log");
	ЮнитТестирование.ПроверитьРавенство( 3, Log10(1000), "Log10");
	ЮнитТестирование.ПроверитьРавенство(-0.9880316240928617, Sin(30), "Sin");
	ЮнитТестирование.ПроверитьРавенство( 0.5253219888177297, Cos(45), "Cos");
	ЮнитТестирование.ПроверитьРавенство( 1.6197751905438615, Tan(45), "Tan");
	ЮнитТестирование.ПроверитьРавенство( 0.5943858000010622, ASin(0.56), "ASin");
	ЮнитТестирование.ПроверитьРавенство( 0.9764105267938343, ACos(0.56), "ACos");
	ЮнитТестирование.ПроверитьРавенство( 0.5104883219167758, ATan(0.56), "ATan");
	ЮнитТестирование.ПроверитьРавенство( 8103.083927575384, Exp(9), "Exp");
	ЮнитТестирование.ПроверитьРавенство( 2, Sqrt(4), "Sqrt");
	ЮнитТестирование.ПроверитьРавенство( 8, Pow(2, 3), "Pow");

КонецПроцедуры
