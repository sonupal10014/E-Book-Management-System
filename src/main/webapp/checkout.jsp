<%@page import="java.util.List"%>
<%@page import="com.entity.User"%>
<%@page import="com.DB.DBConnect"%>
<%@page import="com.DAO.CartDAOImpl"%>
<%@page import="com.entity.Cart"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Cart Page</title>
    <%@include file="all_component/allCss.jsp"%>
    <style type="text/css">
        .paint-card {
            box-shadow: 0 0 3px 0 rgba(0, 0, 0, 0.3);
        }
    </style>
</head>
<body style="background-color: #f0f1f2;">
    <%@include file="all_component/navbar.jsp"%>

    <c:if test="${empty userobj}">
        <c:redirect url="login.jsp"></c:redirect>
    </c:if>

    <c:if test="${not empty succMsg}">
        <div class="alert alert-success text-center" role="alert">${succMsg}</div>
        <c:remove var="succMsg" scope="session"/>
    </c:if>

    <c:if test="${not empty failedMsg}">
        <div class="alert alert-danger text-center" role="alert">${failedMsg}</div>
        <c:remove var="failedMsg" scope="session"/>
    </c:if>

    <div class="container-fluid">
        <div class="row p-2">
            <div class="col-md-6">
                <div class="card bg-white paint-card">
                    <div class="card-body">
                        <h3 class="text-center text-success">Your Selected Item</h3>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th scope="col">Book Name</th>
                                    <th scope="col">Author</th>
                                    <th scope="col">Price</th>
                                    <th scope="col">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    User u = (User) session.getAttribute("userobj");
                                    CartDAOImpl dao = new CartDAOImpl(DBConnect.getConn());
                                    List<Cart> cart = dao.getBookByUser(u.getId());
                                    Double totalPrice = 0.00;
                                    for (Cart c : cart) {
                                        totalPrice = c.getTotalPrice();
                                %>
                                <tr>
                                    <th scope="row"><%=c.getBookName()%></th>
                                    <td><%=c.getAuthor()%></td>
                                    <td><%=c.getPrice()%></td>
                                    <td>
                                        <a href="remove_book?bid=<%=c.getBid()%>&&uid=<%=c.getUserId()%>&&cid=<%=c.getCid()%>"
                                           class="btn btn-sm btn-danger">Remove</a>
                                    </td>
                                </tr>
                                <%
                                    }
                                %>
                                <tr>
                                    <td>Total Price</td>
                                    <td></td>
                                    <td></td>
                                    <td><%=totalPrice%></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Right Column - User Details -->
            <div class="col-md-6">
                <div class="card paint-card">
                    <div class="card-body">
                        <h3 class="text-center text-success">Your Details for Order</h3>
                        <form action="order" method="post">
                            <input type="hidden" value="${userobj.id}" name="id">
                            <input type="hidden" name="totalPrice" value="<%=totalPrice%>">

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>Name</label>
                                    <input type="text" readonly name="username" class="form-control" value="${userobj.name}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label>Email</label>
                                    <input type="email" readonly name="email" class="form-control" value="${userobj.email}" required>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>Phone Number</label>
                                    <input name="phno" type="number" readonly class="form-control" value="${userobj.phno}" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label>Address</label>
                                    <input type="text" name="address" class="form-control" required>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>Landmark</label>
                                    <input type="text" name="landmark" class="form-control" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label>City</label>
                                    <input type="text" name="city" class="form-control" required>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label>State</label>
                                    <input type="text" name="state" class="form-control" required>
                                </div>
                                <div class="form-group col-md-6">
                                    <label>Pin code</label>
                                    <input type="number" name="pincode" class="form-control" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Payment Mode</label>
                                <select class="form-control" name="payment" id="paymentOption" onchange="toggleOrderButton()" required>
                                    <option value="noselect">--Select--</option>
                                    <option value="COD">Cash On Delivery</option>
                                    <option value="Online">Online Payment</option>
                                </select>
                            </div>

                            <div class="text-center">
                                <button type="submit" id="codBtn" class="btn btn-warning">Order Now</button>
                                <button type="button" id="onlinePayBtn" onclick="payNow()" class="btn btn-primary" style="display:none;">Pay Online</button>
                                <a href="index.jsp" class="btn btn-success">Continue Shopping</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <!-- Razorpay Script -->
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    <script>
        function toggleOrderButton() {
            var paymentOption = document.getElementById("paymentOption").value;
            if (paymentOption === "Online") {
                document.getElementById("onlinePayBtn").style.display = "inline-block";
                document.getElementById("codBtn").style.display = "none";
            } else {
                document.getElementById("onlinePayBtn").style.display = "none";
                document.getElementById("codBtn").style.display = "inline-block";
            }
        }

        function payNow() {
            var amount = <%= (int)(totalPrice * 100) %>; // Razorpay takes amount in paisa
            var options = {
                "key": "rzp_test_bIKXfwiqhgJfDC", // TODO: Replace with your Razorpay Key
                "amount": amount,
                "currency": "INR",
                "name": "E-Book Store",
                "description": "Book Purchase",
                "handler": function (response) {
                    window.location.href = "payment-success.jsp?payment_id=" + response.razorpay_payment_id;
                },
                "prefill": {
                    "name": "${userobj.name}",
                    "email": "${userobj.email}"
                },
                "theme": {
                    "color": "#3399cc"
                }
            };
            var rzp = new Razorpay(options);
            rzp.open();
        }
    </script>
</body>
</html>
