package ru.javawebinar.topjava.service.userservice.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.userservice.UserServiceTestCommon;

@ActiveProfiles(Profiles.JDBC)
public class UserServiceJdbcTest extends UserServiceTestCommon {
}
