<%@ page language="java" contentType="text/html; charset=utf-8"
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
		<div class="row">
		<div id="details" class="card-deck">
				<jsp:include page="auctionDetail.jsp">
					<jsp:param name="target" value="${target}" />
					<jsp:param name="auction" value="${auction}" />
					<jsp:param name="gain" value="${gain}" />
					<jsp:param name="message" value="${message}" />
				</jsp:include>
			<div class="col-4">
				<div class="card">
					<div class="card-header">
						<h3 class="card-title">Control Panel</h3>
					</div>
				<div class="card-body">
				<c:choose>
					<c:when test="${target=='seller'}">
						<jsp:include page="deleteAuctionForm.jsp" />
						<script src="js/seller.js"></script>
					</c:when>
					<c:otherwise>
						<jsp:include page="bidForm.jsp" />
						<div id="offers_bid_table_container">
						<jsp:include page="bidTable.jsp">
							<jsp:param name="bids" value="${bids}"/>
						</jsp:include>
						</div>
						<script src="js/bids.js"></script>
						<script src="js/customer.js"></script>
					</c:otherwise>
				</c:choose>
				</div>
				</div>
			</div>
		</div>
		</div>
	</div>
	</body>
	<script src="js/countdown.js"></script>
</html>