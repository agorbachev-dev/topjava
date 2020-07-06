package ru.javawebinar.topjava.service.mealservice;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.DATAJPA})
public class MealServiceDataJpaTest extends MealServiceTestCommon {
}
