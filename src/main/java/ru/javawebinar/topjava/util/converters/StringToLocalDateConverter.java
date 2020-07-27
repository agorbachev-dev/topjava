package ru.javawebinar.topjava.util.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, TIME_FORMATTER);
    }
}
