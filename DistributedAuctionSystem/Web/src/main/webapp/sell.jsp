<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import = "java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import = "javax.servlet.http.*" %>
<%@ page import = "org.apache.commons.fileupload.*" %>
<%@ page import = "org.apache.commons.fileupload.disk.*" %>
<%@ page import = "org.apache.commons.fileupload.servlet.*" %>
<%@ page import = "org.apache.commons.io.output.*" %>

<!DOCTYPE HTML> 

<html lang = "it"> 
	<head>
		<jsp:include page="meta.jsp"/>
		<title>Sell Object</title>
		</head>
	<body>
		<jsp:include page='menu.jsp' />
		<div class="container">
			<div class="row">
				<div class="col">
					<div class = "card">
						<div class = "card-body">
							<form action="auction" method="POST" enctype="multipart/form-data">
								<h3 class="card-title"> Sell something! </h3>
								<input type="text" hidden readonly name="action" value="create" />
								<div class="form-group">
									<label>Name:</label>
									<input class = "form-control" name="name" type="text" size = "40" required />
									<label>Minimum Bid:</label>
									<input class = "form-control" name="minimum_bid" type="number" step="0.01" required />
									<label>Minimum Raise:</label>
									<input class = "form-control" name="minimum_raise" type="number" step="0.01" required />
									<label >Number of Objects:</label>
									<input class = "form-control" name="object" type="number" step="1" required />
									<label >End Day: </label>
									<input class = "form-control" name="day" type="date" required/>
									<label >End Hour:</label>
									<input class = "form-control" name="hour" type="time" required />
									<label >Description:</label>
									<textarea class="form-control " name = "description" rows="5" cols="80" maxlength = "400"></textarea>
									<label>Image:</label>
									<input name="userfile" class = "form-control" type="file" required />
									<input class = "btn btn-secondary" type="submit" value="Sell" class ="submit" />
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>

		</div>
	</body>
</html>