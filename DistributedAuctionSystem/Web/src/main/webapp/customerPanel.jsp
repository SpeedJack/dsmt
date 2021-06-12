<%@ page import="it.unipi.dsmt.das.model.Bid" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashSet" %>
<% HashSet<Bid> winnings = (HashSet<Bid>) pageContext.getRequest().getAttribute("winnings"); %>
<% ArrayList<Bid> bids = (ArrayList<Bid>) pageContext.getRequest().getAttribute("bids"); %>
<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 10/06/21
  Time: 18:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div>
    <p id="errorMessage"></p>
    <jsp:include page="bidForm.jsp" />
    <div id="offers_bid_table_container">
        <h4>Your Bids</h4>
        <jsp:include page="bidTable.jsp" />
    </div>
</div>
