package ru.javawebinar.topjava.model.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class MealDaoInMemoryImplementation implements Dao<Meal> {
    private static volatile MealDaoInMemoryImplementation instance;
    private final AtomicLong id;
    private final Map<Long, Meal> mealsInMemory;

    public static MealDaoInMemoryImplementation getInstance() {
        MealDaoInMemoryImplementation localInstance = instance;
        if (localInstance == null) {
            synchronized (MealDaoInMemoryImplementation.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MealDaoInMemoryImplementation();
                }
            }
        }
        return localInstance;
    }


    private MealDaoInMemoryImplementation() {
        this.id = new AtomicLong(0L);
        this.mealsInMemory = new ConcurrentHashMap<>();
        Long size = Stream.of(
                new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(generateId(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ).map(this::create).count();
    }

    public Long generateId() {
        return id.getAndIncrement();
    }

    @Override
    public Meal create(Meal meal) {
        // meal.getId() like primary key in DB
        if (!mealsInMemory.containsKey(meal.getId())) {
            mealsInMemory.put(meal.getId(), meal);
        } else {
            meal = null;
        }

        return meal;
    }

    @Override
    public Meal get(long id) {
        return mealsInMemory.getOrDefault(id, null);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsInMemory.values());
    }

    @Override
    public Meal update(Meal meal) {
        return mealsInMemory.put(meal.getId(), meal);
    }

    @Override
    public void delete(long id) {
        mealsInMemory.remove(id);
    }
}
