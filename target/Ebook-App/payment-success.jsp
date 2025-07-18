<%@page import="java.sql.*"%>
<%@page import="com.DB.DBConnect"%>
<%@page import="com.entity.User"%>
<%
    // Get user object from session
    User user = (User) session.getAttribute("userobj");
    if (user == null) {
        out.println("<h2>Error: User not logged in. Please login first.</h2>");
        return;
    }
    int userId = user.getId(); // Get user ID

    // Get payment details from request
    String paymentId = request.getParameter("payment_id");
    String totalPriceStr = request.getParameter("totalPrice");

    if (paymentId == null || paymentId.isEmpty()) {
        out.println("<h2>Error: Payment ID is missing!</h2>");
        return;
    }

    if (totalPriceStr == null || totalPriceStr.isEmpty()) {
        out.println("<h2>Error: Total Price is missing!</h2>");
        return;
    }

    double totalPrice = 0.0;
    try {
        totalPrice = Double.parseDouble(totalPriceStr);
    } catch (NumberFormatException e) {
        out.println("<h2>Error: Invalid Total Price format!</h2>");
        return;
    }

    // Payment status (can be dynamic if you want)
    String status = "Success";

    // Database insert query
    String query = "INSERT INTO payments (user_id, payment_id, amount, status) VALUES (?, ?, ?, ?)";

    Connection conn = null;
    PreparedStatement ps = null;
    try {
        // Get database connection
        conn = DBConnect.getConn();

        // Create PreparedStatement
        ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ps.setString(2, paymentId);
        ps.setDouble(3, totalPrice);
        ps.setString(4, status);

        // Execute insert
        int result = ps.executeUpdate();

        if (result > 0) {
            // Clear the cart after successful payment (Online payment only)
            session.removeAttribute("cart");  // Assuming cart is stored in session

            // Debugging: Check if the cart is removed
            if (session.getAttribute("cart") == null) {
                out.println("<h2>Cart is now empty.</h2>");
            } else {
                out.println("<h2>Cart not empty after payment.</h2>");
            }

            out.println("<h2 style='color:green;'>Payment Successful!<br>Payment ID: " + paymentId + "<br>Amount Paid: ₹" + totalPrice + "</h2>");
        } else {
            out.println("<h2 style='color:red;'>Payment save failed! Please contact support.</h2>");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        out.println("<h2 style='color:red;'>Database error while saving payment details.</h2>");
    } finally {
        // Close resources
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>
<html>
    <head>
        <title>Payment Success</title>
    </head>
    <body>
        <h1>Payment Successful</h1>
        <p>Your payment was successful. Thank you for your purchase!</p>
        <form action="home.jsp" method="get">
            <button type="submit">Return to Home</button>
        </form>
    </body>
</html>
