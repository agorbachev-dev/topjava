<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://util.topjava.javawebinar.ru/CustomFunctions" %>
<html>
<head>
    <title>Meals List</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>

<h1>Meals Management</h1>
<h2>
    <a href="${pageContext.request.contextPath}/meals?action=create">Add new meal</a>
    &nbsp;&nbsp;&nbsp;
    <a href="${pageContext.request.contextPath}/meals?action=list">List all meals</a>

</h2>
<table border="1">
    <tr>
        <th>Дата/время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${mealToList}" var="mealTo">
        <tr>
            <td>${f:parseLocalDateTimeToStr(mealTo.dateTime)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td>edit</td>
            <td><a href="?action=delete&id=<c:out value='${mealTo.id}'/>"><img src="img/del.png" alt="delete"></a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
