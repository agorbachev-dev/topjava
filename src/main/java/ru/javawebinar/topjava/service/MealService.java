package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        return repository.save(meal);
    }

    public boolean delete(int id, int userId) {
        return get(id, userId) != null && repository.delete(id);
    }

    public Meal get(int id, int userId) {
        Meal meal = checkNotFoundWithId(repository.get(id), id);
        return meal.getUserId() == userId ? meal : null;
    }

    public Collection<Meal> getAll(Integer userId) {
        return repository.getAll(userId);
    }

    public Meal update(Meal meal) {
        return get(meal.getId(), meal.getUserId()) != null ? repository.save(meal) : null;
    }

}