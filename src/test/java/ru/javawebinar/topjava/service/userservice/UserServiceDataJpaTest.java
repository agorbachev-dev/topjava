package ru.javawebinar.topjava.service.userservice;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles({Profiles.DATAJPA})
public class UserServiceDataJpaTest extends UserServiceTestCommon{
}
