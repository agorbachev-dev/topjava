package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userRepo = getOrCreate(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userRepo.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return userRepo.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return getOrCreate(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return getOrCreate(userId).get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId, Map<String, Object> filterValues) {
        Stream<Meal> rawStream = getOrCreate(userId).values().stream();
        if (filterValues != null) {
            LocalDate startDate = (LocalDate) filterValues.get("dateStart");
            LocalDate endDate = (LocalDate) filterValues.get("dateEnd");
            LocalTime startTime = (LocalTime) filterValues.get("timeStart");
            LocalTime endTime = (LocalTime) filterValues.get("timeEnd");
            rawStream = rawStream
                    .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate.plusDays(1L))
                            && DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
        }
        return rawStream
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getOrCreate(int userId) {
        Map<Integer, Meal> userRepo;
        return (userRepo = repository.putIfAbsent(userId, new HashMap<>())) == null ? repository.get(userId) : userRepo;
    }
}

