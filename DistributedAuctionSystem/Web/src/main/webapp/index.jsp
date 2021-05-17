<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE HTML> 

<html lang = "it"> 
	<head>
		<meta charset="UTF-8">
		<meta name = "author" content = "Lorenzo Cima, Nicola Ferrante, Simone Pampaloni, Niccolò Scatena">
		<meta name="description" content="Login - Auction System">
		<meta name="keywords" content="Auction, buy, sell">
		<link rel = "icon" href = "style/img/auction.jpg" sizes = "32x32" type = "image/jpg">
		<link rel = "stylesheet" href = "style/utility.css" type="text/css" media="screen">
		<link rel= "stylesheet" href="style/auctions_menu.css" type="text/css" media="screen">
		<link rel= "stylesheet" href="style/auctions.css" type="text/css" media="screen">
		<title> Login </title>
	</head>
	<body id = "login_body">
		<section id="sign_in">
		<div id="login_form">
			<p align = "center"><b> LOGIN </b></p>
			<p id = "wrong_login"> ${message} </p>
			<form name="login" action="login" method="post">
					<table class = "login">
						<tr>
							<td rowspan = "3" id = "tab_logo">
								<img id = "logo" src = "style/img/auction.jpg" alt = "Auctions">
							</td>
							<td>
								<input type="text" placeholder="Username" class = "login" name="username" required autofocus>
							</td>
						</tr>
						<tr>
							<td>
								<input type="password" placeholder="Password" class = "login" name="password" required>
							</td>
						</tr>
						<tr>
							<td>
								<input type="submit" class = "point" value="Login" id = "login_button">
								<input type = "button" class = "point" value = "Register" id = "register_button" onClick="document.location.href='registration_page.jsp'">
							</td>
						</tr>
					</table>
			</form>
		</div>
		</section>
	
	</body>
</html>