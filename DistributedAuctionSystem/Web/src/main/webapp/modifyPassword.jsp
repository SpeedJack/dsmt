<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE HTML> 

<html lang = "it"> 
	<head>
		<jsp:include page="meta.jsp"/>
		<title> Modify Password </title>
	</head>
	<body >
	<section class="bg-dark text-white m-y-2 m-x-auto p-y-2 p-x-auto container">
		<div class="row">
			<h3> Register </h3>
			<p id = "wrong_login"> ${message} </p>
		</div>
		<div class="row">
			<div class="col">
				<img class="img-fluid rounded" src = "style/img/auction.jpg" alt = "Auctions">
			</div>
			<div class="col">
				<form name="login" action="register" method="post">
					<div class="form-group">
						<label> Old Password
							<input type="text" class="form-control" placeholder="Username" name="old_password" required autofocus>
						</label>
					</div>
					<div class="form-group">
						<label> Password
							<input type="password" class="form-control" placeholder="Password" name="new_password" required>
						</label>
					</div>
					<div class="form-group">
						<label> Confirm Password
							<input type="password"  class="form-control" placeholder="Password" name="confirm_password" required>
						</label>
					</div>
					<input type = "submit" class = "btn btn-secondary" value = "Modify" />
					<a class="btn btn-secondary" href="auction">Home</a>
				</form>
			</div>
		</div>
	</section>
	</body>
</html>