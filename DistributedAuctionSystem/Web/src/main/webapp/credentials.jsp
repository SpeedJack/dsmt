<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 08/06/21
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>

<html lang = "it">
<head>
    <jsp:include page="meta.jsp"/>
    <title> Login </title>
</head>
<body >
<section class="text-white my-4 mx-auto py-4 px-auto container align-content-center" >

    <div class="row align-content-center justify-content-center">
        <c:choose>
            <c:when test="${target == 'modifyPassword'}">
                <jsp:include page="modifyPassword.jsp">
                    <jsp:param name="message" value="${message}"/>
                </jsp:include>
            </c:when>
            <c:when test="${target == 'register'}">
                <jsp:include page="register.jsp">
                    <jsp:param name="message" value="${message}"/>
                </jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="login.jsp">
                    <jsp:param name="message" value="${message}"/>
                </jsp:include>
            </c:otherwise>
        </c:choose>
    </div>
</section>
</body>
</html>
