package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal expected = new Meal(MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        Meal actual = service.get(MEAL_ID, USER_ID);
        assertMatch(actual, expected);
    }

    @Test
    public void getByNotOwner() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteByNotOwner() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusiveNoDate() {
        assertTrue(service.getBetweenInclusive(null, null, USER_ID).containsAll(getAllMeal()));
    }

    @Test
    public void getBetweenInclusive30Jan2020() {
        assertTrue(service.getBetweenInclusive(LocalDate.of(2020, 1, 30), LocalDate.of(2020, 1, 30), USER_ID)
                .containsAll(
                        getAllMeal()
                                .stream()
                                .filter(meal -> meal.getDate().compareTo(LocalDate.of(2020, 1, 30)) == 0)
                                .collect(Collectors.toList())
                )
        );
    }

    @Test
    public void getBetweenInclusive1900() {
        assertEquals(service.getBetweenInclusive(LocalDate.of(1900, 1, 01), LocalDate.of(1900, 12, 31), USER_ID).size(), 0);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        assertMatch(actual, getAllMeal());
    }

    @Test
    public void getAllByNotFoundUser() {
        List<Meal> actual = service.getAll(NOT_FOUND);
        assertEquals(actual.size(), 0);
    }

    @Test
    public void update() {
        Meal expected = new Meal(MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 10000);
        Meal updated = service.get(MEAL_ID, USER_ID);
        updated.setCalories(10000);
        service.update(updated, USER_ID);
        Meal actual = service.get(MEAL_ID, USER_ID);
        assertMatch(actual, expected);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        int createdId = created.getId();
        newMeal.setId(createdId);
        assertMatch(created, newMeal);
        assertMatch(service.get(createdId, USER_ID), newMeal);
    }
}