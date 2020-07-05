package ru.javawebinar.topjava;

import org.springframework.test.context.ActiveProfilesResolver;

public class CommonTestResolver implements ActiveProfilesResolver {
    @Override
    public String[] resolve(Class<?> testClass) {
        String[] arr = new String[2];
        arr[0] = Profiles.getActiveDbProfile();
        arr[1] = Profiles.getActiveRepositoryProfile();
        return arr;
    }
}
