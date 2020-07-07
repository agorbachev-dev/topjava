package ru.javawebinar.topjava.service.userservice.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.userservice.UserServiceTestCommon;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTestCommon {
    @Test
    public void getByIdWithMeals() throws Exception {
        User user = super.service.getByIdWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
        Assert.assertEquals(7L, user.getMeals().size());
    }
}
