package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public Collection<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId(), null), SecurityUtil.authUserCaloriesPerDay());
    }

    public Collection<MealTo> getAll(Map<String, Object> filterValues) {
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId(), filterValues), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id){
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        return service.update(meal, SecurityUtil.authUserId());
    }
}