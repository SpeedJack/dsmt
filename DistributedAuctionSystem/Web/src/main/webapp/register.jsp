<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<div class="col-4 align-content-center">
	<h3> Register </h3>
	<img class="img-fluid rounded" src = "style/img/auction.jpg" alt = "Auctions">
	<form name="login" action="register" method="post">
		<div class="form-group">
			<label> Username
				<input type="text" class="form-control" placeholder="Username" name="username" required autofocus>
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
		<p id = "wrong_login"> ${message} </p>
		<input type = "submit" class = "btn btn-secondary" value = "Register" />
		<a class="btn btn-secondary" href="login">Login</a>
	</form>
</div>
</div>