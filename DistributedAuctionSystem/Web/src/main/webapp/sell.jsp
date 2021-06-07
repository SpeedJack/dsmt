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
	<body class = "personal">
	<jsp:include page='menu.jsp'>
		<jsp:param name="username" value="${username}"/>
		<jsp:param name="ID" value="${ID}"/>
	</jsp:include>
		<div id = "content">
			<div id = "sell_form">
			<form action="auction" method="POST" enctype="multipart/form-data">
				<p>${message}</p>
				<p> Object Information </p>
				<input type="text" hidden readonly name="action" value="create" />
				<label class = "little">Name:</label>     				 
		  			<input class = "form" name="name" type="text" size = "40" required><br>
    			<label class = "little">Minimum Bid:</label>  				 
		  			<input class = "form" name="minimum_bid" type="text" size = "5" required><br>
				<label class = "little">Minimum Raise:</label>
					<input class = "form" name="minimum_raise" type="text" size = "5" required><br>
				<label class = "little">Number of Objects:</label>		  			 
		  			<input class = "form" name="object" type="text" size = "5" required><br>
				<label class = "little">End Day: </label>     				
		  			<input class = "form" name="day" type="text" size = "15" placeholder = "dd/mm/aaaa"
						   pattern = '(?:((?:0[1-9]|1[0-9]|2[0-9])\/(?:0[1-9]|1[0-2])|(?:30)\/(?!02)(?:0[1-9]|1[0-2])|31\/(?:0[13578]|1[02]))\/(?:19|20)[0-9]{2})' required><br>
    			<label class = "little">End Hour:</label>   				 
		  			<input class = "form" name="hour" type="text" size = "10" pattern = "([01]?[0-9]{1}|2[0-3]{1}):[0-5]{1}[0-9]{1}" required><br><br>
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