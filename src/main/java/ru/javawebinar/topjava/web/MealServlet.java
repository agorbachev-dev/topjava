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

        String action = (actionObj == null) ? "" : actionObj;
        Long id = (idObj == null) ? null : Long.valueOf(idObj);
        boolean error;
        switch (action) {
            case "edit":
                req.setAttribute("display","inline");
                error = edit(id,req);
                break;
            case "delete":
                req.setAttribute("display","none");
                error = delete(id);
                break;
            case "create":
                req.setAttribute("display","none");
                error = create(req);
                break;
            case "showCreateEditForm":
                req.setAttribute("display","inline");
                break;
            case "list":
                req.setAttribute("display","none");
                break;
            default:
                break;
        }
        listAll(req, resp);
    }

    private boolean create(HttpServletRequest req) {
        dao.create(new Meal(TimeUtil.parseStrToLocalDateTime(req.getParameter("dateTime").replace('T',' ')),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories"))));
        return false;
    }

    private boolean edit(Long id, HttpServletRequest req) {
        if (id != null) {
            dao.update(id, new String[]{req.getParameter("dateTime").replace('T', ' '),req.getParameter("description"),req.getParameter("calories")});
            return false;
        } else {
            return true;
        }
    }

    private void listAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("mealToList", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, dao.getExcess()));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    private boolean delete(Long id) {
        if (id != null) {
            dao.delete(id);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
