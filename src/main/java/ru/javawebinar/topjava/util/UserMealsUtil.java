package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStream(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDate = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesPerDate.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> mealTo = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDateTime mealDateTime = meal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                mealTo.add(new UserMealWithExcess(mealDateTime, meal.getDescription(), meal.getCalories(), caloriesPerDate.get(mealDateTime.toLocalDate()) > caloriesPerDay));
            }
        }
        return mealTo;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDate = meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(meal ->
                        TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal ->
                        new UserMealWithExcess(meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                caloriesPerDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(new CustomCollector(startTime, endTime, caloriesPerDay));
    }

    public static class CustomCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>> {
        private final Map<LocalDate, Integer> caloriesPerDateMap;
        private final int caloriesPerDateLimit;
        private final LocalTime startTime;
        private final LocalTime endTime;

        public CustomCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDateLimit) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.caloriesPerDateLimit = caloriesPerDateLimit;
            caloriesPerDateMap = new HashMap<>();
        }

        @Override
        public Supplier<List<UserMeal>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
            return (list, meal) -> {
                caloriesPerDateMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
                if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                    list.add(meal);
            };
        }

        @Override
        public BinaryOperator<List<UserMeal>> combiner() {
            return (l, r) -> {
                l.addAll(r);
                return l;
            };
        }

        @Override
        public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
            return filteredUserMealList -> filteredUserMealList
                    .stream()
                    .map(meal -> new UserMealWithExcess(meal.getDateTime(),
                            meal.getDescription(),
                            meal.getCalories(),
                            caloriesPerDateMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDateLimit))
                    .collect(Collectors.toList());
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.CONCURRENT);
        }
    }
}
