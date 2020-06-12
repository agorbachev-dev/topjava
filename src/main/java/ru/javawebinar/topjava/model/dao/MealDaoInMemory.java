package ru.javawebinar.topjava.model.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class MealDaoInMemory implements Dao<Meal> {
    private static volatile MealDaoInMemory instance;
    private final AtomicLong id;
    private final Map<Long, Meal> mealsInMemory;

    public static MealDaoInMemory getInstance() {
        MealDaoInMemory localInstance = instance;
        if (localInstance == null) {
            synchronized (MealDaoInMemory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MealDaoInMemory();
                }
            }
        }
        return localInstance;
    }

    private MealDaoInMemory() {
        this.id = new AtomicLong(0L);
        this.mealsInMemory = new ConcurrentHashMap<>();
        Stream.of(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ).forEach(this::create);
    }

    public Long generateId() {
        return id.getAndIncrement();
    }

    @Override
    public Meal create(Meal meal) {
        return mealsInMemory.computeIfAbsent(generateId(), id -> new Meal(id, meal));
    }

    @Override
    public Meal get(long id) {
        return mealsInMemory.getOrDefault(id, null);
    }

    @Override
    public Collection<Meal> getAll() {
        return mealsInMemory.values();
    }

    @Override
    public Meal update(Meal meal) {
        return mealsInMemory.computeIfPresent(meal.getId(), (i, m) -> new Meal(meal.getId(), meal));
    }

    @Override
    public void delete(long id) {
        mealsInMemory.remove(id);
    }
}