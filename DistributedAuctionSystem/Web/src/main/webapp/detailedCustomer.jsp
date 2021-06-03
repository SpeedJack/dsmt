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
		<script src="js/ajax.js"></script>
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
						<img src=${auction.image}>
						<div class="content_movie_wrapper">
							<span class="title_stats">Vendor ID</span>: ${auction.agent} <br>
							<span class="title_stats">Lowest bid</span>: ${auction.minPrice} <br>
							<span class="title_stats">Available Objects</span>: ${auction.saleQuantity}<br>
							<span class="title_stats">End time</span>: ${date}<br>
							<form name = "bid" id="bid_form" action="/web/bid" method="post">
								<table class = "single_bid_table">
									<tr>
										<td> <span class="title_stats">Offer</span>
										<td> <input type="text" name="value" placeholder="0" class = "single_bid" required />
									<tr>
										<td> <span class="title_stats">N.Objects</span>
										<td> <input type="text" name="quantity" placeholder="1" class = "single_bid" required/>
									<tr>
										<td>
										<td> <input type="submit" class = "single_bid_button" />
								</table>
								<input hidden=True name="auction_id" id="auction_id" readonly value="${auction.id}" />
								<input hidden=True name="user_id" id="user_id" readonly value="${user.id}" />
							</form>
							<p id="result"></p>
							<table id = "offers_bid_table">
								<caption>Your offers</caption>
								<tr>
									<td> <span class="title_stats">Quantity</span>
									<td> <span class="title_stats">Offer</span>
									<td>
								<c:forEach items="${bids}" var="bid">
								<tr>
									<td> <span class="title_stats">${bid.quantity}</span>
									<td> <span class="title_stats">${bid.value} $</span>
									<td> <input type="button" value="Delete" class = "single_bid_button">
								</c:forEach>
							</table>

							<span class="bid_state">${message}</span><br><br>
						</div>	
						
					</div>
					<div id="main">
						<h2>Description:</h2>
						<p> ${auction.description}</p>
					</div>
				</div>
			</div>
		</div>

	</body>

</html>