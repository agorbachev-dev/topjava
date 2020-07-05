package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal getByIdAndUserId(Integer id, Integer user_id);

    @Transactional
    @Modifying
    int deleteByIdAndUserId(Integer id, Integer user_id);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(Integer user_id);

    List<Meal> findAllByUserIdAndDateTimeAfterAndDateTimeBeforeOrderByDateTimeDesc(Integer user_id, LocalDateTime startDate, LocalDateTime endDate);
}
