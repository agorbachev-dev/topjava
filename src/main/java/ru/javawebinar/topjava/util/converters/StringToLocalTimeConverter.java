package ru.javawebinar.topjava.util.converters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime convert(String source) {
        return LocalTime.parse(source, TIME_FORMATTER);
    }
}
