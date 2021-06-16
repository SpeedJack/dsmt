<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="it">
	<head>
		<jsp:include page="meta.jsp" />
		<title>Detailed Object</title>
	</head>
	<body>
	<jsp:include page='menu.jsp' />
		<div align="center" id="content">
			<h1 align="center">${status}</h1>
			<p align="center">${message}</p>
		</div>
	</body>
</html>