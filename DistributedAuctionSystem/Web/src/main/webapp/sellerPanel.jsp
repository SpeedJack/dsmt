<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 10/06/21
  Time: 18:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="it.unipi.dsmt.das.model.Bid" %>
<%@ page import="java.util.HashSet" %>
<% HashSet<Bid> winnings = (HashSet<Bid>) pageContext.getRequest().getAttribute("winnings"); %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <jsp:include page="deleteAuctionForm.jsp" />
    <div id="offers_bid_table_container">
        <h3>Winning Bids</h3>
        <%pageContext.getRequest().setAttribute("showWinning", true); %>
        <%pageContext.getRequest().setAttribute("toprint", winnings); %>
        <jsp:include page="bidTable.jsp" />
    </div>
</div>
