<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE HTML> 

<html lang = "it"> 
	<head>
		<meta charset="UTF-8">
			<meta charset="UTF-8">
		<meta name = "author" content = "Lorenzo Cima, Nicola Ferrante, Simone Pampaloni, Niccolò Scatena">
		<meta name="description" content="Modify Password - Auction System">
		<meta name="keywords" content="Auction, buy, sell">
		<link rel = "icon" href = "style/img/auction.jpg" sizes = "32x32" type = "image/jpg">
		<link rel = "stylesheet" href = "style/utility.css" type="text/css" media="screen">
		<link rel="stylesheet" href="style/auctions_menu.css" type="text/css" media="screen">
		<link rel="stylesheet" href="style/auctions.css" type="text/css" media="screen">
		<title>Sell Object</title>
	</head>
	<body class = "personal">
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
						<a href="followed">
							<span class="menu_item_img profile_img"></span>
							<span>Followed Auctions</span>	
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
						<div id="sellImgLink" class="menu_item_img my_auctions_img_0"></div>	
						<span id="sellLabelLink" class="normal_label">Sell New Object</span>
				</ul>
			</div>
		</div>
		
		<div id = "content">
			<div id = "sell_form">
			<form action="../php/add_scores.php" method="POST">
				<p> Object Information </p>
				<label class = "little">Name:</label>     				 
		  			<input class = "form" name="name" type="text" size = "40" required><br>
    			<label class = "little">Minimum Bid:</label>  				 
		  			<input class = "form" name="minimum_bid" type="text" size = "5" required><br>
				<label class = "little">Number of Objects:</label>		  			 
		  			<input class = "form" name="object" type="text" size = "5" required><br>
				<label class = "little">End Day: </label>     				
		  			<input class = "form" name="day" type="text" size = "15" required><br>
    			<label class = "little">End Hour:</label>   				 
		  			<input class = "form" name="hour" type="text" size = "10" required><br><br>
				<label class = "little">Description:</label><br>	 
		  			<textarea name = "description" rows="5" cols="80" maxlength = "400"></textarea><br>
				<label class = "little">Image:</label>
		  			<input name="userfile" class = "form" type="file" required><br><br>
				<input type="submit" value="Confirm" class ="submit">
			</form>
			</div>
		</div>
	</body>
</html>