<%@ page contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it">
	<head>
		<jsp:include page="meta.jsp" />
		<title>Detailed Object</title>
	</head>
	<body>
	<jsp:include page='menu.jsp' />
	<div id="details" class="container">
		<div class="row align-content-center mx-auto my-4">
		<div class="align-content-center justify-content-center col-lg-6 col-md-12">
			<c:choose>
				<c:when test="${finished}">
					<jsp:include page="auctionResult.jsp">
						<jsp:param name="bids" value="${bids}" />
						<jsp:param name="auction" value="${auction}" />
						<jsp:param name="status" value="${status}" />
					</jsp:include>
				</c:when>
				<c:otherwise>
					<jsp:include page="auctionDetail.jsp">
						<jsp:param name="target" value="${target}" />
						<jsp:param name="auction" value="${auction}" />
						<jsp:param name="gain" value="${gain}" />
						<jsp:param name="message" value="${message}" />
					</jsp:include>
				</div>
				<div id="control-panel" class="col-lg-6 col-md-12 align-content-center justify-content-center">
					<div class="card">
						<div class="card-header">
							<h3 class="card-title">Control Panel</h3>
						</div>
					<div class="card-body">
					<c:choose>
						<c:when test="${target=='seller'}">
							<jsp:include page="sellerPanel.jsp" />
						</c:when>
						<c:otherwise>
							<jsp:include page="customerPanel.jsp" />

						</c:otherwise>
					</c:choose>
				</div>
			</div>
			</c:otherwise>
			</c:choose>
		</div>
		</div>
	</div>
	<c:if test="${not finished}">
		<script src="js/websocket.js"></script>
		<script src="js/countdown.js"></script>
		<script src="js/bids.js"></script>
	</c:if>
	</body>
</html>