package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.DAO.MealDAO.MealDaoInMemoryImplementation;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private final MealDaoInMemoryImplementation dao = new MealDaoInMemoryImplementation();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String actionObj = req.getParameter("action");
        String idObj = req.getParameter("id");

        String action = (actionObj == null) ? "" : actionObj.trim();
        Long id = (idObj == null || "".equals(idObj)) ? null : Long.valueOf(idObj.trim());
        req.setAttribute("display","none");
        switch (action.toLowerCase()) {
            case "edit":
                edit(id,req);
                break;
            case "delete":
                delete(id);
                break;
            case "create":
                create(req);
                break;
            case "showcreateeditform":
                String actionType = req.getParameter("actionType").trim();
                req.setAttribute("actionType",actionType);
                if("edit".equals(actionType)){
                    req.setAttribute("mealToEdit",dao.get(id));
                }
                req.setAttribute("display","inline");
                break;
            case "list":
                break;
            default:
                break;
        }
        listAll(req, resp);
    }

    private void create(HttpServletRequest req) {
        dao.create(new Meal(TimeUtil.parseStrToLocalDateTime(req.getParameter("dateTime").replace('T',' ')),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories"))));
    }

    private void edit(Long id, HttpServletRequest req) {
        if (id != null)
            dao.update(id, new String[]{req.getParameter("dateTime").trim().replace('T', ' '),req.getParameter("description").trim(),req.getParameter("calories").trim()});

    }

    private void listAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("mealToList", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, dao.getExcess()));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    private void delete(Long id) {
        if (id != null)
            dao.delete(id);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
