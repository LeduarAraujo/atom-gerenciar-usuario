package com.leduar.atomgerenciarusuario.utils;

import java.time.LocalDate;

public class StringToDate {
    public static LocalDate getStringToLocalDate(String data) {
        return LocalDate.parse(data);
    }
}
