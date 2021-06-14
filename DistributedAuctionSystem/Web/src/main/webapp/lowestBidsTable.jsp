
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
        <tbody id = "lowest-bids-table-body-${target}">
        <c:forEach items="${lb_keys}" var="key">
            <tr scope="row" >
                <td> Less or equal to ${key} Items</td>
                <td> ${lb[key]} $</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
 </div>
