<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name = "author" content = "Lorenzo Cima, Nicola Ferrante, Simone Pampaloni, Niccolò Scatena">
    <meta name="description" content="Login - Auction System">
    <meta name="keywords" content="Auction, buy, sell">
    <link rel = "icon" href = "style/img/auction.jpg" sizes = "32x32" type = "image/jpg">
    <link rel = "stylesheet" href = "style/utility.css" type="text/css" media="screen">
    <link rel="stylesheet" href="style/auctions_menu.css" type="text/css" media="screen">
    <link rel="stylesheet" href="style/auctions.css" type="text/css" media="screen">
    <title>Detailed Object</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="js/websocket.js"></script>
    <script src="js/bids.js"></script>
    <script src="js/countdown.js"></script>
</head>
<body>
<div id="menu">
    <div id="menu_home" class="menu_single_section">
        <a href="customer">
            <img src = "style/img/auction.jpg" class="logo_img">
        </a>
    </div>
    <div id="sign_out_menu" class="menu_single_section">
        <ul>
            <li>
                <p> User: ${username} </p>
                <p> UserID: ${ID} </p>
            <li>
                <a href="password_modify.jsp">
                    <div class="menu_item_img sign_out_img"></div>
                    <span>Modify Password</span>
                </a>
            <li>
                <a href="logout">
                    <div class="menu_item_img sign_out_img"></div>
                    <span>Logout</span>
                </a>
        </ul>
    </div>
    <div id="user_menu" class="menu_single_section">
        <ul>
            <li>
                <a href="customer">
                    <span class="menu_item_img profile_img"></span>
                    <span>Auctions List</span>
                </a>
            <li>
                <a href="offers">
                    <span class="menu_item_img profile_img"></span>
                    <span>My Offers</span>
                </a>
        </ul>
    </div>
    <div id="auction_menu" class="menu_single_section">
        <ul>
            <li>
                <a href="seller">
                    <div id="toMyObjectsImgLink" class="menu_item_img to_my_objects_img_0"></div>
                    <span id="toMyObjectsLabelLink" class="normal_label">My Auctions</span>
                </a>
            <li>
                <a href="sell">
                    <div id="sellImgLink" class="menu_item_img my_auctions_img_0"></div>
                    <span id="sellLabelLink" class="normal_label">Sell New Object</span>
                </a>
        </ul>
    </div>
</div>

<div id="content">
    <div id="detailed_auction_tab">
        <div id="left">
            <div id="detailed_poster">
                <h1> ${auction.name} </h1>
                <img src="data:image/gif;base64,${auction.image}">
                <div class="content_movie_wrapper">
                    <span class="title_stats">Lowest Bid</span>: ${auction.minPrice} $<br>
                    <span class="title_stats">Available Objects</span>: ${auction.saleQuantity}<br>
                    <span class="title_stats">End Time</span>: <span hidden id="end_date">${date}</span><br>
                    <span class="title_stats">Sold Objects</span>: ${sold}<br>
                    <span class="title_stats">Total Gain</span>: ${gain} $<br>
                    <span class="bid_state">${message}</span><br><br>
                </div>

            </div>
            <div id="main">
                <h2>Description:</h2>
                <p> ${auction.description}</p>
                <input type = "button" value = "Delete Auction" onClick="document.location.href='deleteAuction?auctionID=${auction.id}'"/>
            </div>
        </div>
    </div>
</div>

</body>

</html>