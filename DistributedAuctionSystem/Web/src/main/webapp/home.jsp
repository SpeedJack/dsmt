<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it">
	<head>
		<jsp:include page="meta.jsp"/>
		<title>Auction List</title>
	</head>
	<body>
	<jsp:include page='menu.jsp'>
		<jsp:param name="username" value="${user.username}"/>
		<jsp:param name="ID" value="${user.id}"/>
	</jsp:include>

	<div id="content">
		<c:choose>
			<c:when test="${empty auctionList}">
				<h1 align = "center">${message}</h1>
			</c:when>
			<c:otherwise>
				<jsp:include page='auctionList.jsp'>
					<jsp:param name="auctionList" value="${auctionList}"/>
				</jsp:include>
			</c:otherwise>
		</c:choose>
	</div>
	</body>
</html>