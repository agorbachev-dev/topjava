<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix = "c"  uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        td{
            padding: 5px;
            border: 2px solid black;
        }
        table{
            border: 4px solid black;
            border-collapse: collapse;
        }
        th{
            color: black;
        }
    </style>
    <title>Meals</title>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <h3><a href="${pageContext.request.contextPath}/edit-meal?action=create">Add new meal</a></h3>
    <table>
        <tr>
            <th>Дата/время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${mealToList}" var="mealTo">
        <tr            <c:if test="${mealTo.excess}">style="color: red"</c:if>
                       <c:if test="${not mealTo.excess}">style="color: green"</c:if>>
            <td>${mealTo.dateTime}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="${pageContext.request.contextPath}/edit-meal?action=edit&id=${mealTo.id}">edit</a></td>
            <td><a href="${pageContext.request.contextPath}/meals?action=delete&id=${mealTo.id}">delete</a></td>
        </tr>
        </c:forEach>
    </table>
</body>
</html>
