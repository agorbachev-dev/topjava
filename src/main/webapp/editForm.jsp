<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix = "c"  uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
    <title>Meal ${param.action} form</title>
    <style>
        #id{
            display: none;
        }
    </style>
    </head>
    <body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meal ${param.action}</h2>
    <form action="meals?action=${param.action}" method="post">
        <input
                type="text"
                id="id"
                name="id"
                <c:if test="${param.action == 'edit'}">
                       value="${param.id}"
                required
                </c:if>
        >
        <label for="dateTime">Дата/время</label>
        <br>
        <input
                type="datetime-local"
                id="dateTime"
                name="dateTime"
                <c:if test="${param.action == 'edit'}">
                    value="${param.dateTime}"
                </c:if>
                required
        >
        <br>
        <label for="description">Описание</label>
        <br>
        <input
                type="text"
                id="description"
                name="description"
                <c:if test="${param.action == 'edit'}">
                    value="${param.description}"
                </c:if>
                required
        >
        <br>
        <label for="calories">Калории</label>
        <br>
        <input
                type="number"
                id="calories"
                name="calories"
                <c:if test="${param.action == 'edit'}">
                    value="${param.calories}"
                </c:if>
                required
        >
        <br>
        <input
                type="submit"
                value="Сохранить"
        >
    </form>
    </body>
</html>
