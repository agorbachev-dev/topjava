//package ru.javawebinar.topjava.web.meal;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import ru.javawebinar.topjava.model.Meal;
//import ru.javawebinar.topjava.util.MealsUtil;
//import ru.javawebinar.topjava.web.SecurityUtil;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Map;
//
//import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
//import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
//
//
//public class JspMealController extends AbstractMealController {
//
//    @GetMapping("/meals")
//    public String doGet(Model model, @RequestParam(required = false) Map<String, String> attrs) {
//        String action = attrs.getOrDefault("action", "all");
//
//        switch (action) {
//            case "delete" -> {
//                int id = getId(attrs);
//                super.service.delete(id, SecurityUtil.authUserId());
//                return "redirect:meals";
//            }
//            case "create", "update" -> {
//                final Meal meal = "create".equals(action) ?
//                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
//                        super.service.get(getId(attrs), SecurityUtil.authUserId());
//                model.addAttribute("meal", meal);
//                return "mealForm";
//            }
//            case "filter" -> {
//                LocalDate startDate = parseLocalDate(attrs.get("startDate"));
//                LocalDate endDate = parseLocalDate(attrs.get("endDate"));
//                LocalTime startTime = parseLocalTime(attrs.get("startTime"));
//                LocalTime endTime = parseLocalTime(attrs.get("endTime"));
//                model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
//                return "meals";
//            }
//            default -> {
//                model.addAttribute("meals", MealsUtil.getTos(super.service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
//                return "meals";
//            }
//        }
//    }
//
//    private int getId(Map<String, String> attrs) {
//        return Integer.parseInt(attrs.get("id"));
//    }
//
//    @PostMapping("/meals")
//    public String doPost(@RequestParam Map<String, String> attrs) {
//        String strId = attrs.getOrDefault("id", "");
//        Meal meal = new Meal(LocalDateTime.parse(attrs.get("dateTime")), attrs.get("description"), Integer.parseInt(attrs.get("calories")));
//        if ("".equals(strId)) {
//            super.service.create(meal, SecurityUtil.authUserId());
//        } else {
//            meal.setId(Integer.parseInt(strId));
//            super.service.update(meal, SecurityUtil.authUserId());
//        }
//        return "redirect:meals";
//    }
//}