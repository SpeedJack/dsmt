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
		<title>Customer Home</title>
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
						<span class="menu_item_img profile_img"></span>
						<span>Auctions List</span>	
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
			<section class="auction_dashboard">	
				<ul class="poster_auctions_list">
					<c:forEach items="${auctionList}" var="auction">
					<li class="poster_auction_item_wrapper">
						<p class = "bid"> <b> ${auction.name} </b> </p>
						<div class="poster_auction_item"> <a href="detailedCustomer?auctionID=${auction.id}">
							<img src= ${auction.image}>
						</a></div>
						<div class="detail_auction_item">
							<p class = "bid"> ${auction.saleQuantity} available </p>
							<p class = "bid"> Starting bid: ${auction.minPrice}$ </p>
						</div>
						</c:forEach>
				</ul>
			</section>	
		</div>
	</body>
</html>