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
		<title>Detailed Object</title>
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
						<h1> Nutella </h1>
						<img src="style/img/image3.jpg">
						<div class="content_movie_wrapper">
							<span class="title_stats">Vendor</span>: Lorenzo21 <br>
							<span class="title_stats">Lowest bid</span>: 1$ <br>
							<span class="title_stats">Available Objects</span>: 50<br>
							<span class="title_stats">End time</span>: 2021-05-08 21:00<br>	
							<form name = "bid" method="post">
								<table class = "single_bid_table">
									<tr>
										<td> <span class="title_stats">Offer</span>
										<td> <input type="text" placeholder="0" class = "single_bid" required>
									<tr>
										<td> <span class="title_stats">N.Objects</span>
										<td> <input type="text" placeholder="1" class = "single_bid" required>
									<tr>
										<td> <input type="button" value="Unfollow" class = "single_follow_button">
										<td> <input type="submit" value="Bid" class = "single_bid_button">
								</table>
							</form>	
							<span class="bid_state">Currently, you are the winner</span><br><br>
						</div>	
						
					</div>
					<div id="main">
						<h2>Description:</h2>
						<p> Che mondo sarebbe senza Nutella</p>
					</div>
				</div>
			</div>
		</div>

	</body>
</html>