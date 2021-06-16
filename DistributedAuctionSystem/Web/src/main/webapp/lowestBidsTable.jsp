<%@ page import="java.util.Collection" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%--
  Created by IntelliJ IDEA.
  User: nicola
  Date: 07/06/21
  Time: 04:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .my-custom-scrollbar {
        position: relative;
        height: 200px;
        overflow: auto;
    }
    .table-wrapper-scroll-y {
        display: block;
    }
</style>

 <div class="table-wrapper-scroll-y my-custom-scrollbar">
    <table class="table table-bordered table-striped mb-0">
        <thead>
            <th scope="col">Quantity</th>
            <th scope="col">Offer</th>
        </thead>
        <tbody id = "lowest-bids-table-body">
        <% List<Integer> keys = (List<Integer>) pageContext.getRequest().getAttribute("lb_keys"); %>
        <% Map<Integer, Double> map = (Map<Integer, Double>) pageContext.getRequest().getAttribute("lb"); %>
        <% int size = keys.size(); %>
        <% int count = 0; %>
        <c:forEach items="${lb_keys}" var="key">
            <tr scope="row" >
                <td> <%=count == 0 ? "less or equal to " + keys.get(count) : "between " + keys.get(count -1) + " and " +  keys.get(count) %></td>
                <td> ${lb[key]} $</td>
            </tr>
            <% count++; %>
        </c:forEach>
        </tbody>
    </table>
 </div>
