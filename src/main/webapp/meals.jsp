<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        form {
            border: 2px black;
        }
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <form method="post" action="meals?action=filter">
        <table>
            <tr>
                <td>
                    <dl>
                        <dt>Date start:</dt>
                        <dd><input type="date" name="dateStart"></dd>
                    </dl>
                </td>
                <td>
                    <dl>
                    <dt>Date end:</dt>
                    <dd><input type="date" name="dateEnd"></dd>
                    </dl>
                </td>
                <td>
                    <dl>
                    <dt>Time start:</dt>
                    <dd><input type="time" name="timeStart"></dd>
                    </dl>
                </td>
                <td>
                    <dl>
                        <dt>Time end:</dt>
                        <dd><input type="time" name="timeEnd"></dd>
                    </dl>
                </td>
            </tr>
        </table>
        <button type="submit">Filter</button>
        <button><a href="meals?action=nofilter"></a>Cancel</button>
    </form>
    <button><a href="meals?action=create">Add Meal</a></button>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>