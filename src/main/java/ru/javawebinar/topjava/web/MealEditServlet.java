package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.dao.MealDaoInMemoryImplementation;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealEditServlet extends HttpServlet {
    private Logger log;
    private MealDaoInMemoryImplementation dao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log = getLogger(MealServlet.class);
        dao = MealDaoInMemoryImplementation.getInstance();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("method get start running");
        String action = req.getParameter("action");
        String idFromParameter = req.getParameter("id");
        Long id = null;
        if (idFromParameter != null && !"".equals(idFromParameter)) {
            id = Long.parseLong(idFromParameter);
        }
        if ("create".equals(action) || ("edit".equals(action) && id != null)) {
            if (action.equals("create")) {
                log.debug("action = create");
                req.setAttribute("action", "create");
            } else {
                log.debug("action = edit");
                req.setAttribute("action", "edit");
                req.setAttribute("mealToEdit", dao.get(id));
            }
            log.debug("forward to mealEditForm page");
            log.debug("method get stop running");
            req.getRequestDispatcher("/mealEditForm.jsp").forward(req, resp);
        } else {
            log.debug("parameters is empty or not valid");
            log.debug("redirect to meal page");
            log.debug("method get stop running");
            resp.sendRedirect("meals.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("method post start running");
        String action = req.getParameter("action");
        if (action != null && !"".equals(action)) {
            switch (action.toLowerCase()) {
                case "create":
                    create(req);
                    break;
                case "edit":
                    edit(req);
                    break;
                default:
                    log.debug("action = " + action + "is not valid ");
                    break;
            }
        } else {
            log.debug("parameter \"action\" is empty");
        }
        log.debug("forward to meals page");
        log.debug("method post stop running");
        resp.sendRedirect(this.getServletContext().getContextPath()+"/meals");
    }

    private void edit(HttpServletRequest req) {
        log.debug("action = edit");
        String idFromParameter = req.getParameter("id");
        if (idFromParameter != null && !"".equals(idFromParameter)) {
            long id = Long.parseLong(idFromParameter);
            Meal mealInDb = dao.get(id);

            if (mealInDb != null) {
                String dateTimeStr = req.getParameter("dateTime");
                LocalDateTime dateTime = dateTimeStr == null || "".equals(dateTimeStr) ? null : TimeUtil.parseStrToLocalDateTime(dateTimeStr.replace('T', ' '));

                String descriptionStr = req.getParameter("description");
                String description = descriptionStr == null || "".equals(descriptionStr) ? null : descriptionStr;

                String caloriesStr = req.getParameter("calories");
                Integer calories = caloriesStr == null || "".equals(caloriesStr) ? null : Integer.parseInt(caloriesStr);

                if (dateTime != null && description != null && calories != null) {
                    log.debug("updated meal = " +
                            dao.update(new Meal(id, dateTime, description, calories)).toString()
                    );
                } else {
                    log.debug("some params are not valid, update failed");
                }
            } else {
                log.debug("no meal in db with id = " + id);
            }
        } else {
            log.debug("parameter \"id\" is empty");
        }
    }

    private void create(HttpServletRequest req) {
        log.debug("action = create");
        Meal mealToAddToDb = new Meal(
                dao.generateId(),
                TimeUtil.parseStrToLocalDateTime(req.getParameter("dateTime").replace('T', ' ')),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories"))
        );
        Meal createdMeal = dao.create(mealToAddToDb);
        if (createdMeal != null) {
            log.debug("created meal = " + createdMeal.toString());
        } else {
            log.debug("meal with id = " + mealToAddToDb.getId() + " already exists");
        }
    }
}
