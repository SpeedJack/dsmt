<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 05/06/21
  Time: 17:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="container">
    <div class="card-deck">
        <c:forEach items="${auctionList}" var="auction">
            <c:set var="auction" value="${auction}" scope="request" />
            <jsp:include page="auction.jsp">
                <jsp:param name="auction" value="${auction}" />
            </jsp:include>
        </c:forEach>
    </div>
</section>