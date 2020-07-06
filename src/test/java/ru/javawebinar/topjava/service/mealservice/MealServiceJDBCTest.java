package ru.javawebinar.topjava.service.mealservice;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.JDBC})
public class MealServiceJDBCTest extends MealServiceTestCommon {
}
