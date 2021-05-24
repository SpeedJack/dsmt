<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

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
		<title>Seller Home</title>
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
						<div id="toMyObjectsImgLink" class="menu_item_img to_my_objects_img_0"></div>	
						<span id="toMyObjectsLabelLink" class="normal_label">My Auctions</span>
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
					<li class="poster_auction_item_wrapper">
						<p class = "bid"> <b> Nintendo DS </b> </p>
						<div class="poster_auction_item"> <a href="detailed_object.jsp">
							<img src= "style/img/image1.jpg">
						</a></div>
						<div class="detail_auction_item">
							<p class = "auction_state"> Auction finished </p> 
							<p class = "bid"> Total Object: 5 </p>
							<p class = "bid"> Total Gain: 500$ </p>
						</div>
					<li class="poster_auction_item_wrapper">
						<p class = "bid"> <b> Nutella </b> </p>
						<div class="poster_auction_item"> <a href="detailed_object.jsp">
							<img src= "style/img/image3.jpg">
						</a></div>
						<div class="detail_auction_item">
							<p class = "auction_state"> Auction in Progress </p> 
						</div>
				</ul>
			</section>	
		</div>
	</body>
</html>