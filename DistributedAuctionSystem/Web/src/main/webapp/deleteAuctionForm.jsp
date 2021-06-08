<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 07/06/21
  Time: 19:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="auction?action=delete&auctionID=${auction.id}" method="post">
    <div class="form-control">
        <input class="btn btn-secondary" id="submit_button" type="submit" value="Delete Auction" />
    </div>
</form>
<script src="js/seller.js"></script>
