package ru.javawebinar.topjava.model.dao;

import java.util.Collection;

public interface Dao<T> {
    T create(T t);

    T get(long id);

    Collection<T> getAll();

    T update(T t);

    void delete(long id);
}
