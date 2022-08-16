package support;

public class ErrorMessage {
    public static final String INCORRECT_DATABASE_CONNECTION = "Некорректные параметры соединения с базой данных(расположение файла свойств)." +
            "Проверьте правильность параметров файла кофигурации.";
    public static final String INCORRECT_INPUT_FILES = "Входной файл содержит некорректные данные(пуст)";
    public static final String INCORRECT_INPUT_PARAMS = "Проверьте корректность входных параметров:" +
            "названия файлов и тип операции";
    public static final String INCORRECT_DATE = "Ошибка в значении даты во входящем файле(неверный формат)";
    public static final String BAD_QUERY = "Ошибка в при выполнении запроса к базе данных";
    public static final String BAD_CRITERIA = "Неверно указан один из критериев поиска";
    public static final String FATAL_ERROR = "Произошла ошибка.Проверьте параметры соединения с БД,входные критерии и пути к файлам";
}
