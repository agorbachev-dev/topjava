<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="f" uri="http://util.topjava.javawebinar.ru/CustomFunctions" %>
<html>
<head>
    <style>
        td{
            padding: 5px;
            border: 2px solid black;
        }
    </style>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>

<h1>Meals</h1>
<h2>
    <a href="${pageContext.request.contextPath}/meals?action=showCreateEditForm&actionType=create">Add new meal</a>
    &nbsp;&nbsp;&nbsp;
    <a href="${pageContext.request.contextPath}/meals?action=list">List all meals</a>

</h2>
<table style="border: 4px solid black;border-collapse: collapse">
    <tr>
        <th>Дата/время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${mealToList}" var="mealTo">
        <tr <c:if test="${mealTo.excess}">
            style="color: crimson"
        </c:if> >
            <td>${f:parseLocalDateTimeToStr(mealTo.dateTime)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td>
                <a href="${pageContext.request.contextPath}/meals?action=showCreateEditForm&actionType=edit
                                                                             &id=<c:out value="${mealTo.id}"/>
                                                                             &dateTime=<c:out value="${mealTo.dateTime}"/>
                                                                             &description=<c:out value="${mealTo.description}"/>
                                                                             &calories=<c:out value="${mealTo.calories}"/>
                ">edit</a>
            </td>
            <td><a href="${pageContext.request.contextPath}/meals?action=delete&id=<c:out value='${mealTo.id}'/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<br><br>
<form action="meals?action=<c:out value="${actionType}"/>" method="post" style="display: <c:out value="${display}"/>" >
    <input style="display: none" type="text" id="id" name="id" <c:if test="${fn:contains(actionType, 'edit')}">
           value="${mealToEdit.id}"
    </c:if>>
    <label for="dateTime">Дата/время</label><br>
    <input type="datetime-local" id="dateTime" name="dateTime" <c:if test="${fn:contains(actionType, 'edit')}">
                                                                        value="${mealToEdit.dateTime}"
                                                                       </c:if> required><br>
    <label for="description">Описание</label><br>
    <input type="text" id="description" name="description" <c:if test="${fn:contains(actionType, 'edit')}">
                                                                    value="${mealToEdit.description}"
                                                                        </c:if> required><br>
    <label for="calories">Калории</label><br>
    <input type="text" id="calories" name="calories" <c:if test="${fn:contains(actionType, 'edit')}">
                                                                    value="${mealToEdit.calories}"
                                                            </c:if> required><br>
    <input type="submit" value="Сохранить">
</form>
</body>
</html>
