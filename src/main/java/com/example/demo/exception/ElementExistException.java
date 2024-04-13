package com.example.demo.exception;

/**
 * Исключение в случае отсутствия/наличия данных.
 */
public class ElementExistException extends RuntimeException {

    private static final String ERROR_EXIST_MESSAGE = "Данная запись уже существует в таблице!";
    private static final String ERROR_ABSENT_MESSAGE = "Данная запись отсутствует в таблице!";
    public ElementExistException(Boolean isExist) {
        super(isExist ? ERROR_EXIST_MESSAGE : ERROR_ABSENT_MESSAGE);
    }

}
