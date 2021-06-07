<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 07/06/21
  Time: 04:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${empty bids}">
        <h2>You have not made any offer yet!</h2>
    </c:when>
    <c:otherwise>
        <table id = "offers_bid_table">
            <caption>Your offers</caption>
            <thead>
                <th>Quantity</th>
                <th>Offer</th>
                <th>User</th>
                <th></th>
            </thead>
            <tbody id = "offers-bid-table-body">
                <c:forEach items="${bids}" var="bid">
                    <tr id="${bid.id}">
                        <td> ${bid.quantity}</td>
                        <td> ${bid.value} </td>
                        <td> <input class="delete-bid-button" type="button" value="Delete"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

