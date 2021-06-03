<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE HTML> 

<html lang = "it"> 
	<head>
		<meta charset="UTF-8">
		<meta name = "author" content = "Lorenzo Cima, Nicola Ferrante, Simone Pampaloni, Niccolò Scatena">
		<meta name="description" content="Registration - Auction System">
		<meta name="keywords" content="Auction, buy, sell">
		<link rel = "icon" href = "style/img/auction.jpg" sizes = "32x32" type = "image/jpg">
		<link rel = "stylesheet" href = "style/utility.css" type="text/css" media="screen">
		<link rel="stylesheet" href="style/auctions_menu.css" type="text/css" media="screen">
		<link rel="stylesheet" href="style/auctions.css" type="text/css" media="screen">
		<title> Registration </title>
	</head>
	<body id = "login_body">
		<section id="sign_in">
		<div id="modify_form">
		<p align = "center"><b> REGISTRATION </b></p>
		<p id = "wrong_login"> ${message} </p>
			<form name="login" action="registration" method="post">
					<table class = "login">
						<tr>
							<td class = "modify">
								<label> Username </label>
							</td>
							<td class = "modify" colspan = '2'>
								<input type="text" placeholder="Username" class = "login" name="username" required autofocus>
							</td>
						</tr>
						<tr>
							<td class = "modify">
								<label> Password </label>
							</td>
							<td class = "modify" colspan = '2'>
								<input type="password" placeholder="Password" class = "login" name="new_password" required>
							</td>
						</tr>
						<tr>
							<td class = "modify">
								<label> Confirm Password </label>
							</td>
							<td class = "modify" colspan = '2'>
								<input type="password" placeholder="Password" class = "login" name="confirm_password" required>
							</td>
						</tr>
						<tr>
							<td colspan = '3' id = "td_register">
								<input type = "button" class = "point" value = "Login" id = "login_button" onClick="document.location.href='index.jsp'">
								<input type = "submit" class = "point" value = "Register" id = "register_button">
							</td>
						</tr>
					</table>
			</form>
		</div>
		</section>
	
	</body>
</html>