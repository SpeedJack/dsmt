<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 06/06/21
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class ="col-lg-3 col-md-6 col-sm-12">
  <div class="card">
    <div class="card-header">
      <h5 class="card-title">${auction.name}</h5>
    </div>
    <a href="auction?action=detail&auctionID=${auction.id}">
      <c:choose>
        <c:when test="${empty auction.image}">
          <img class="card-img-top" alt="..." src="style/img/auction.jpg">
        </c:when>
        <c:otherwise>
          <img  class="card-img-top" alt="..." class="card-img-top" src="data:image/gif;base64,${auction.image}">
        </c:otherwise>
      </c:choose>
    </a>
    <div class="card-body">
      <p class = "card-text"> End: <span hidden class="end_date">${auction.endDate * 1000}</span></p>
      <p class = "card-text"> ${auction.saleQuantity} available </p>
      <p class = "card-text"> Starting bid: ${auction.minPrice}$ </p>
    </div>
    <div class="card-footer">
      <a class="btn btn-secondary" href="auction?action=detail&auctionID=${auction.id}" >
        Details
      </a>
    </div>
  </div>
</div>
