<%@ page import="it.unipi.dsmt.das.model.Auction" %>
<%@ page import="it.unipi.dsmt.das.model.AuctionState" %>
<%@ page import="it.unipi.dsmt.das.model.User" %><%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 09/06/21
  Time: 00:47
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
<jsp:include page='menu.jsp' />
<section class="my-4 mx-auto py-4 px-auto container align-content-center" >
    <div class="row align-items-center align-content-center justify-content-center">
        <div class="col align-content-center justify-content-center">
            <h1>${status}</h1>
            <jsp:include page="auction.jsp">
                <jsp:param name="auction" value="${auction}" />
            </jsp:include>
            <c:choose>
                <c:when test="${empty bids}">

                </c:when>
                <c:otherwise>
                    <table class="table table-bordered table-striped">
                        <thead>
                        <th>Quantity</th><td>Price</td>
                        </thead>
                        <tbody>
                        <tr>
                            <c:forEach items="${bids}" var="bid">
                                <td>${bid.quantity}</td>
                                <td>${bid.value}</td>
                            </c:forEach>
                        </tr>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</section>
