<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 09/06/21
  Time: 00:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="my-4 mx-auto py-4 px-auto align-content-center" >
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
</section>
