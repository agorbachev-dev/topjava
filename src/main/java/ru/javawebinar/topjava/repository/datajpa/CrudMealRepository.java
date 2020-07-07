package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal getByIdAndUserId(int id, int user_id);

    @Transactional
    @Modifying
    int deleteByIdAndUserId(int id, int user_id);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(int user_id);

    List<Meal> findAllByUserIdAndDateTimeAfterAndDateTimeBeforeOrderByDateTimeDesc(int user_id, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:user_id")
    Meal getWithUser(@Param("id") int id, @Param("user_id") int userId);
}
