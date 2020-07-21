package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class MealController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealController(MealService service) {
        this.service = service;
    }

    @GetMapping(value = "/meals/edit")
    public String edit(@RequestParam(required = false) String id, Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal;
        if (id == null) {
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        } else {
            meal = service.get(Integer.parseInt(id), userId);
        }
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        log.info("getAll for user {}", userId);
        return "meals";
    }

    @GetMapping("/meals/delete")
    public String delete(@RequestParam String id) {
        int userId = SecurityUtil.authUserId();
        service.delete(Integer.parseInt(id), userId);
        log.info("delete meal {} for user {}", id, userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/filter")
    public String filter(@RequestParam Map<String, String> params, Model model) {
        int userId = SecurityUtil.authUserId();
        LocalDate startDate = parseLocalDate(params.get("startDate"));
        LocalDate endDate = parseLocalDate(params.get("endDate"));
        LocalTime startTime = parseLocalTime(params.get("startTime"));
        LocalTime endTime = parseLocalTime(params.get("endTime"));
        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, userId);
        model.addAttribute("meals", MealsUtil.getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        return "/meals";
    }

    @PostMapping("/meals/save")
    public String save(@RequestParam Map<String, String> params) {
        int userId = SecurityUtil.authUserId();
        LocalDateTime dateTime = LocalDateTime.parse(params.get("dateTime"));
        String description = params.get("description");
        int calories = Integer.parseInt(params.get("calories"));
        String id = params.getOrDefault("id", "");
        Meal meal = new Meal(dateTime, description, calories);
        if ("".equals(id)) {
            service.create(meal, userId);
            log.info("create {} for user {}", meal, userId);
        } else {
            meal.setId(Integer.parseInt(id));
            service.update(meal, userId);
            log.info("update {} for user {}", meal, userId);
        }
        return "redirect:/meals";
    }
}
