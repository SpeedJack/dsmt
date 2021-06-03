<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE HTML> 

<html lang = "it"> 
	<head>
		<meta charset="UTF-8">
		<meta name = "author" content = "Lorenzo Cima, Nicola Ferrante, Simone Pampaloni, Niccolò Scatena">
		<meta name="description" content="Modify Password - Auction System">
		<meta name="keywords" content="Auction, buy, sell">
		<link rel = "icon" href = "style/img/auction.jpg" sizes = "32x32" type = "image/jpg">
		<link rel = "stylesheet" href = "style/utility.css" type="text/css" media="screen">
		<link rel="stylesheet" href="style/auctions_menu.css" type="text/css" media="screen">
		<link rel="stylesheet" href="style/auctions.css" type="text/css" media="screen">
		<title> Modify Password </title>
	</head>
	<body id = "login_body">
		<section id="sign_in">
		<div id="modify_form">
			<p align = "center"><b> MODIFY PASSWORD </b></p>
			<p id = "wrong_login"> ${message} </p>
			<form name="login" action="modify" method="post">
					<table class = "login">
						<tr>
							<td class = "modify">
								<label>  Old Password </label>
							</td>
							<td class = "modify">
								<input type="password" placeholder="Password" class = "login" name="old_password" required>
							</td>
						</tr>
						<tr>
							<td class = "modify">
								<label> New Password </label>
							</td>
							<td class = "modify">
								<input type="password" placeholder="Password" class = "login" name="new_password" required>
							</td>
						</tr>
						<tr>
							<td class = "modify">
								<label> Confirm Password </label>
							</td>
							<td class = "modify">
								<input type="password" placeholder="Password" class = "login" name="confirm_password" required>
							</td>
						</tr>
						<tr>
							<td>
								<input type = "button" value = "Home" class = "home_button" onClick="document.location.href='customer'">
							</td>
							<td>
								<input type="submit" value="Submit" id = "modify_button">
							</td>
						</tr>
					</table>
			</form>
		</div>
		</section>
	
	</body>
</html>