package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.DAO.MealDAO.MealDaoInMemoryImplementation;
import ru.javawebinar.topjava.util.MealsUtil;

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
        switch (action.toLowerCase()) {
            case "edit":
                break;
            case "delete":
                error = delete(id);
                listAll(req,resp);
                break;
            case "create":
                break;
            default:
                listAll(req, resp);
                break;
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
