package ru.javawebinar.topjava.model.DAO.MealDAO;

import ru.javawebinar.topjava.model.DAO.Dao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoInMemoryImplementation implements Dao<Meal> {

    private static final AtomicLong id = new AtomicLong(0L);
    private static final ConcurrentHashMap<Long, Meal> mealInMemory = new ConcurrentHashMap<>();

    @Override
    public void create(Meal meal) {
        meal.setId(id.getAndIncrement());
        // meal.getId() like primary key in DB
        mealInMemory.put(meal.getId(), meal);
    }

    @Override
    public Meal get(long id) {
        return mealInMemory.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealInMemory.values());
    }

    @Override
    public void update(long id, String[] params) {
        Meal updatedMeal = new Meal(TimeUtil.parseStrToLocalDateTime(params[0]),
                params[1],
                Integer.parseInt(params[2]));
        updatedMeal.setId(id);
        mealInMemory.put(id, updatedMeal);
    }

    @Override
    public void delete(long id) {
        mealInMemory.remove(id);
    }
}
