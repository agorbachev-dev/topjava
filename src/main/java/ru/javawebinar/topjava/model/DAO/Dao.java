package ru.javawebinar.topjava.model.DAO;

import java.util.List;

public interface Dao<T> {
    void create(T t);

    T get(long id);

    List<T> getAll();

    void update(long id, String[] params);

    void delete(long id);
}
