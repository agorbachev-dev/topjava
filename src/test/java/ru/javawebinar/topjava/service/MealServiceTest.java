package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThrows;
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

    @Autowired
    private MealService service;

    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);

    private static final Map<String, Map<String, Object>> testInfo = new HashMap<>();

    @Rule
    public final TestRule watchman = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            testInfo.get(description.getMethodName()).put("testPassing", "succeeded");
        }

        @Override
        protected void failed(Throwable e, Description description) {
            testInfo.get(description.getMethodName()).put("testPassing", "failed");
        }

        @Override
        protected void skipped(AssumptionViolatedException e, Description description) {
            testInfo.get(description.getMethodName()).put("testPassing", "skipped");
        }

        @Override
        protected void starting(Description description) {
            testInfo.put(description.getMethodName(), new HashMap<>());
            testInfo.get(description.getMethodName()).put("startTime", LocalTime.now());
            super.starting(description);
        }

        @Override
        protected void finished(Description description) {
            testInfo.get(description.getMethodName()).put("endTime", LocalTime.now());
            super.finished(description);
        }
    };

    @AfterClass
    public static void afterClass() {
        String skipped = "";
        String succeeded = "";
        String failed = "";

        for (Map.Entry<String, Map<String, Object>> pair : testInfo.entrySet()) {

            String testPassing = (String) pair.getValue().get("testPassing");

            LocalTime startTime = (LocalTime) pair.getValue().get("startTime");
            LocalTime endTime = (LocalTime) pair.getValue().get("endTime");

            String execTime = String.valueOf(Duration.between(startTime, endTime).toMillis() / 1000D);

            String a = pair.getKey() + " - exec time: " + execTime + " sec";

            switch (testPassing) {
                case "succeeded":
                    succeeded += a + "\n";
                    break;
                case "failed":
                    failed += a + "\n";
                    break;
                default:
                    skipped += a + "\n";
                    break;
            }
        }
        log.info("\n" +
                "\n================================TEST PASSED===============================\n" +
                "\n" +
                succeeded +
                "\n" +
                "\n================================TEST FAILED===============================\n" +
                "\n" +
                failed +
                "\n" +
                "\n================================TEST SKIPPED===============================\nх" +
                "\n" +
                skipped
        );
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() throws Exception {
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.update(MEAL1, ADMIN_ID));
    }

    @Test
    public void getAll() throws Exception {
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
    }
}