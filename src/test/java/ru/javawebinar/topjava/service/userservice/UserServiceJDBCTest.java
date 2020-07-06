package ru.javawebinar.topjava.service.userservice;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.JDBC})
public class UserServiceJDBCTest extends UserServiceTestCommon{
}
