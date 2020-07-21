<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <jsp:include page="fragments/headTag.jsp"/>
</head>
<body>
<section>
    <jsp:include page="fragments/bodyHeader.jsp"/>
    <hr>
    <c:choose>
        <c:when test="${param.action == 'create'}">
            <h2><spring:message code="mealform.title.create"/></h2>
        </c:when>
        <c:otherwise>
            <h2><spring:message code="mealform.title.edit"/></h2>
        </c:otherwise>
    </c:choose>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.date"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="action.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="action.cancel"/></button>
    </form>
</section>
</body>
</html>
