<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 07/06/21
  Time: 11:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card">
    <div class="card-header">
        <h2 class="card-title">${auction.name}</h2>
    </div>
    <div class="card-body">
        <img class="card-img-top" src="data:image/gif;base64,${auction.image}">
            <h4>Description:</h4>
            <p class="card-text"> ${auction.description}</p>
            <p class="card-text">Vendor ID: ${auction.agent} </p>
            <p class="card-text">Lowest bid: <span id="min-price">${auction.minPrice}</span></p>
            <p class="card-text">Available Objects: <span id="sale-quantity">${auction.saleQuantity}</span></p>
            <p class="card-text">End time: <span hidden class="end_date">${date}</span></p>
        <c:if test='${target=="seller"}'>
            <p class="card-text">Total Gain:<span hidden id="gain">${gain}</span>$</p>
            <input hidden=True name="auction_id" id="auction_id" readonly value="${auction.id}" />
        </c:if>
</div>
</div>