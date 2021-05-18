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
		<title>Followed Auctions</title>
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
						<span class="menu_item_img profile_img"></span>
						<span>Followed Auctions</span>	
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
					<li class="poster_auction_item_wrapper">
						<p class = "bid"> <b> Nutella </b> </p>
						<div class="poster_auction_item"> <a href="detailed_object.jsp">
							<img src= "style/img/image3.jpg">
						</a></div>
						<div class="detail_auction_item">
							<p class = "bid"> 50 available </p> 
							<p class = "bid"> Lowest bid: 1$ </p>
							<form name = "bid" method="post">
								<table class = "bid_table">
									<tr>
										<td> <label> Offer </label>
										<td> <input type="text" placeholder="0" class = "bid" required>
									<tr>
										<td> <label> N.Objects </label>
										<td> <input type="text" placeholder="1" class = "bid" required>
									<tr>
										<td> <input type="button" value="Unfollow" class = "bid_button">
										<td> <input type="submit" value="Bid" class = "bid_button">
								</table>
							</form>
						</div>
				</ul>
			</section>	
		</div>
	</body>
</html>