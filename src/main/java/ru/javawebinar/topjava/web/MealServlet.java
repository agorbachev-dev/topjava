package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private Map<Integer, Map<String, Object>> filters;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        filters = new HashMap<>();
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            mealRestController = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if ("filter".equals(action)) {
            filters.put(SecurityUtil.authUserId(), addFilterParametersToMap(request));
        } else {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories"))
            );
            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            Meal meal1 = meal.isNew() ? mealRestController.create(meal) : mealRestController.update(meal, Integer.parseInt(id));

        }
        response.sendRedirect("meals");
    }

    private Map<String, Object> addFilterParametersToMap(HttpServletRequest request) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dateStart", request.getParameter("dateStart").isEmpty() ? LocalDate.MIN : LocalDate.parse(request.getParameter("dateStart")));
        parameters.put("dateEnd", request.getParameter("dateEnd").isEmpty() ? LocalDate.MAX.minusDays(1L) : LocalDate.parse(request.getParameter("dateEnd")));
        parameters.put("timeStart", request.getParameter("timeStart").isEmpty() ? LocalTime.MIN : LocalTime.parse(request.getParameter("timeStart")));
        parameters.put("timeEnd", request.getParameter("timeEnd").isEmpty() ? LocalTime.MAX : LocalTime.parse(request.getParameter("timeEnd")));
        return parameters;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, null) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "nofilter":
                filters.remove(SecurityUtil.authUserId());
                response.sendRedirect("meals");
                break;
            case "all":
            default:
                log.info("getAll");
                if (filters.containsKey(SecurityUtil.authUserId())) {
                    request.setAttribute("meals", mealRestController.getAll(filters.get(SecurityUtil.authUserId())));
                    request.setAttribute("filter", filters.get(SecurityUtil.authUserId()));
                } else {
                    request.setAttribute("meals", mealRestController.getAll());
                }

                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
