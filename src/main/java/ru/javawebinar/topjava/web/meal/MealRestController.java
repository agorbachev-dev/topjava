package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Collection<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        return checkNotFoundWithId(service.get(id, SecurityUtil.authUserId()), id);
    }

    public Meal create(Meal meal) {
        meal.setUserId(SecurityUtil.authUserId());
        return service.create(meal);
    }

    public void delete(int id) {
        checkNotFoundWithId(service.delete(id, SecurityUtil.authUserId()), id);
    }

    public Meal update(Meal meal, int id) {
        meal.setUserId(SecurityUtil.authUserId());
        assureIdConsistent(meal, id);
        return checkNotFoundWithId(service.update(meal), id);
    }
}