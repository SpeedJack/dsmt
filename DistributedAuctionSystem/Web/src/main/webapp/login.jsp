<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE HTML> 

<html lang = "it"> 
	<head>
		<jsp:include page="meta.jsp"/>
		<title> Login </title>
	</head>
	<body >
		<section class="bg-dark text-white m-y-2 m-x-auto p-y-2 p-x-auto container" >
			<div class="row">
				<h3> Login </h3>
				<p id = "wrong_login"> ${message} </p>
			</div>
			<div class="row">
				<div class="col">
					<img class="img-fluid rounded" src = "style/img/auction.jpg" alt = "Auctions">
				</div>
				<div class="col">
					<form  name="login" action="login" method="post">
						<div class="form-group">
						<label> Username
						<input type="text" placeholder="Username" class = "form-control" name="username" required autofocus>
						</label>
						</div>
						<div class="form-group">
							<label> Password
							<input type="password" placeholder="Password" class = "form-control" name="password" required>
						</label>
						</div>
						<input type="submit" value="Login" class="btn btn-secondary">
						<a class ="btn btn-secondary" href ="register">Register</a>
					</form>
				</div>
			</div>
		</section>
	</body>
</html>