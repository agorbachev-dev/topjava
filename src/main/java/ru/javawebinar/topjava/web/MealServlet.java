package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.dao.MealDaoInMemory;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private Logger log;
    private MealDaoInMemory dao;
    private Set<String> supportedOperations;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log = getLogger(MealServlet.class);
        dao = MealDaoInMemory.getInstance();
        supportedOperations = Stream.of("create", "edit").collect(Collectors.toCollection(HashSet::new));
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("method \"get\" start running");

        String action = req.getParameter("action") == null ? "" : req.getParameter("action").toLowerCase();

        if ("delete".equals(action)) {
            delete(req.getParameter("id"));
            log.debug("redirect to \"meals\" page");
            log.debug("method \"get\" stop running");
            resp.sendRedirect("meals");
        } else {
            log.debug("".equals(action) ? "listAll" : "action \"" + action + "\" not supported");
            req.setAttribute("mealToList", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.getExcess()));
            log.debug("forward to \"meals\" page");
            log.debug("method \"get\" stop running");
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("method \"post\" start running");

        String action = req.getParameter("action") == null ? "" : req.getParameter("action").toLowerCase();
        if (supportedOperations.contains(action)) {
            invoke(action, req);
        } else {
            log.debug("".equals(action) ? "no action" : "action \"" + action + "\" not supported");
        }
        log.debug("redirect to \"meals\" page");
        log.debug("method \"post\" stop running");
        resp.sendRedirect(this.getServletContext().getContextPath() + "/meals");
    }

    private void invoke(String action, HttpServletRequest req) {
        switch (action) {
            case "create":
                create(req);
                break;
            case "edit":
                edit(req);
                break;
            default:
                break;
        }
    }

    private void edit(HttpServletRequest req) {
        log.debug("action \"edit\"");
        String id = req.getParameter("id");
        if (validateId(id)) {
            LocalDateTime dateTime = TimeUtil.parseStrToLocalDateTime(req.getParameter("dateTime").replace('T', ' '));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));
            Meal mealToEdit = dao.update(new Meal(Long.parseLong(id), dateTime, description, calories));
            if (mealToEdit != null) {
                log.debug("edited meal = " + mealToEdit.toString());
            } else {
                log.debug("meal with \"id\"" + id + " not exist");
            }
        } else {
            log.debug("\"id\" is not valid");
        }
    }

    private void create(HttpServletRequest req) {
        log.debug("action \"create\"");
        Meal mealToCreate = dao.create(new Meal(
                        TimeUtil.parseStrToLocalDateTime(req.getParameter("dateTime").replace('T', ' ')),
                        req.getParameter("description"),
                        Integer.parseInt(req.getParameter("calories"))
                )
        );
        if (mealToCreate != null) {
            log.debug("created meal = " + mealToCreate.toString());
        } else {
            log.debug("meal " + mealToCreate + " already exists");
        }
    }

    private void delete(String id) {
        log.debug("action \"delete\"");
        if (validateId(id)) {
            dao.delete(Long.parseLong(id));
            log.debug("meal with \"id\" = " + id + " deleted");
        } else {
            log.debug("\"id\" is not valid");
        }
    }

    private boolean validateId(String id) {
        return Pattern.matches("^\\d+$", id);
    }
}