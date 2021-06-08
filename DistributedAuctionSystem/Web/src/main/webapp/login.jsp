<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<div class="col-4 align-content-center">
	<h3> Login </h3>
	<img class="img-fluid rounded" src = "style/img/auction.png" alt = "Auctions">
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
		<p> ${message} </p>
		<input type="submit" value="Login" class="btn btn-secondary">
		<a class ="btn btn-secondary" href ="register">Register</a>
	</form>
</div>