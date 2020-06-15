package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(null, "admin1", "admin1@example.com", "admin1", Role.ADMIN),
            new User(null, "user1", "user1@example.com", "user1", Role.USER),
            new User(null, "user2", "user2@example.com", "user2", Role.USER)
    );
}
