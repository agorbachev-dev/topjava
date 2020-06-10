package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.dao.MealDaoInMemoryImplementation;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

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
        if ("delete".equals(action)) {
            log.debug("action = delete");
            if (idFromParameter != null && !"".equals(idFromParameter)) {
                delete(Long.parseLong(idFromParameter));
            }
        }
        req.setAttribute("mealToList", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.getExcess()));
        log.debug("forward to meals page");
        log.debug("method get stop running");
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    private void delete(Long id) {
        dao.delete(id);
        log.debug("meal with id = " + id + " deleted");
    }
}
