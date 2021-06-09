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
	<div class="container">
		<div class="row align-content-center mx-auto my-4">
		<div id="details" class="align-content-center justify-content-center col-lg-6 col-md-12">
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
					<jsp:include page="deleteAuctionForm.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include page="bidForm.jsp" />
					<div id="offers_bid_table_container">
					<jsp:include page="bidTable.jsp">
						<jsp:param name="bids" value="${bids}"/>
					</jsp:include>
					</div>
					<script src="js/bids.js"></script>
				</c:otherwise>
			</c:choose>
			</div>
			</div>
		</div>
		</div>
	</div>
	</body>
	<script src="js/websocket.js"></script>
	<script src="js/countdown.js"></script>
</html>