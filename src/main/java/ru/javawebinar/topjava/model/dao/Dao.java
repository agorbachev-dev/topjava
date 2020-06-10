package ru.javawebinar.topjava.model.dao;

import java.util.List;

public interface Dao<T> {
    T create(T t);

    T get(long id);

    List<T> getAll();

    T update(T t);

    void delete(long id);
}
