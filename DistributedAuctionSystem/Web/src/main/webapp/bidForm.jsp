<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 07/06/21
  Time: 19:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form name="bid" id="bid_form" action="bid" method="post">
    <div class="form-group">
    <label>Offer
        <input class="form-control" type="text" name="value" placeholder="0" required />
    </label>
    </div>
    <div class="form-group">
    <label>N.Objects
        <input class="form-control" type="text" name="quantity" placeholder="1" required/>
    </label>
    </div>
    <div class="form-group">
    <label>
        <input type="submit" id="submit_button" class="btn btn-secondary" value="Send Bid"/>
    </label>
    </div>
    <input hidden name="auction_id" id="auction_id" readonly value="${auction.id}" />
</form>
