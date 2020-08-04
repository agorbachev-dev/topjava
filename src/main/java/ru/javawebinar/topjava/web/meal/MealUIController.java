package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping(value = "/meals")
public class MealUIController extends AbstractMealController {

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }


    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestParam(defaultValue = "") String id,
                               @RequestParam LocalDateTime dateTime,
                               @RequestParam String description,
                               @RequestParam int calories) {
        Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id), dateTime, description, calories);
        if (meal.isNew()) {
            super.create(meal);
        }
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> filter(@RequestParam @Nullable LocalDate startDate,
                               @RequestParam @Nullable LocalTime startTime,
                               @RequestParam @Nullable LocalDate endDate,
                               @RequestParam @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
