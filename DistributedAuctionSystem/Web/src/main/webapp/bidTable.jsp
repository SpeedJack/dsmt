
<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 07/06/21
  Time: 04:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .my-custom-scrollbar {
        position: relative;
        height: 200px;
        overflow: auto;
    }
    .table-wrapper-scroll-y {
        display: block;
    }
</style>

<c:choose>
    <c:when test="${empty bids}">
        <p>No offers</p>
    </c:when>
    <c:otherwise>
        <div class="table-wrapper-scroll-y my-custom-scrollbar">
        <table class="table table-bordered table-striped mb-0" id = "offers_bid_table">
            <thead>
                <th scope="col">Quantity</th>
                <th scope="col">Offer</th>
                <c:if test="${target == 'customer'}">
                    <th scope="col">Is Winning</th>
                    <th scope="col"></th>
                </c:if>
            </thead>
            <tbody id = "offers-bid-table-body">
                <c:forEach items="${bids}" var="bid">
                    <tr scope="row" id="${bid.id}">
                        <td> ${bid.quantity}</td>
                        <td> ${bid.value} </td>
                        <c:if test="${target == 'customer'}">
                            <td> ${bid.id == winning.id}</td>
                            <td> <input class="btn btn-danger delete-bid-button" type="button" value="Delete"/></td>
                        </c:if>
                        </tr>
                </c:forEach>
            </tbody>
        </table>
        </div>
    </c:otherwise>
</c:choose>


