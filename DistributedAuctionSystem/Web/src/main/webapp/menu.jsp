<%@ page import="it.unipi.dsmt.das.model.User" %><%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 05/06/21
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User user = (User) session.getAttribute("user");%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="auction">
        <img src = "style/img/auction.png" width="32" height="32" class="logo_img">
        Distributed Auction System
    </a>

        <ul class="navbar-nav" id="navbarNav">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    <%= user.getUsername() %>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="modifyPassword">Modify Password</a>
                    <a class="dropdown-item" href="logout">Logout</a>
                </div>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    Auctions
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="auction?action=list">Auction List</a>
                    <a class="dropdown-item" href="auction?action=list&target=seller">My Auctions</a>
                    <a class="dropdown-item" href="auction?action=list&target=offers">My Offers</a>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="auction?action=sell">
                    Sell
                </a>
            </li>
        </ul>
</nav>
