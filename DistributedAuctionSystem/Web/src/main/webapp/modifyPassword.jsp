<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<div class="col-4 align-content-center">
	<h3> Modify Password </h3>
	<img class="img-fluid rounded" src = "style/img/auction.png" alt = "Auctions">
	<div class="col">
		<form name="login" action="register" method="post">
			<div class="form-group">
				<label> Old Password
					<input type="password" class="form-control" placeholder="Username" name="old_password" required autofocus>
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
			<input type = "submit" class = "btn btn-secondary" value = "Modify" />
			<a class="btn btn-secondary" href="auction">Home</a>
		</form>
	</div>
</div>