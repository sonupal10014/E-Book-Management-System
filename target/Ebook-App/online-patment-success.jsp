<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="com.DB.DBConnect"%>
<%@page import="com.entity.User"%>
<%
    // Get the payment ID and total price from request
    String paymentId = request.getParameter("payment_id");
    String totalPriceStr = request.getParameter("totalPrice");

    if (paymentId == null || totalPriceStr == null) {
        out.println("<h2 style='color:red;'>Error: Missing payment details!</h2>");
        return;
    }

    // Convert total price from string to double
    double totalPrice = 0.0;
    try {
        totalPrice = Double.parseDouble(totalPriceStr);
    } catch (NumberFormatException e) {
        out.println("<h2 style='color:red;'>Error: Invalid Total Price format!</h2>");
        return;
    }

    // Fetch user details from session
    User user = (User) session.getAttribute("userobj");
    if (user == null) {
        out.println("<h2 style='color:red;'>Error: User not logged in.</h2>");
        return;
    }

    // Insert payment details into the database (optional, but good practice to track the payment)
    String query = "INSERT INTO payments (user_id, payment_id, amount, status) VALUES (?, ?, ?, ?)";
    Connection conn = null;
    PreparedStatement ps = null;
    try {
        conn = DBConnect.getConn();
        ps = conn.prepareStatement(query);
        ps.setInt(1, user.getId());
        ps.setString(2, paymentId);
        ps.setDouble(3, totalPrice);
        ps.setString(4, "Success");

        int result = ps.executeUpdate();
        if (result > 0) {
            // Payment recorded successfully
        } else {
            out.println("<h2 style='color:red;'>Error saving payment details to database!</h2>");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        out.println("<h2 style='color:red;'>Error saving payment details!</h2>");
    } finally {
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Success</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 50px;
        }
        h2 {
            color: green;
        }
        .button {
            background-color: #4CAF50;
            color: white;
            padding: 15px 32px;
            font-size: 16px;
            border: none;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin-top: 20px;
            border-radius: 5px;
        }
        .button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>

    <h2>Payment Successful!</h2>
    <p>Your payment was completed successfully.</p>
    <p><strong>Payment ID:</strong> <%= paymentId %></p>
    <p><strong>Total Amount Paid:</strong> ₹<%= totalPrice %></p>

    <!-- Button to go back to home page -->
    <a href="index.jsp" class="button">Return to Home Page</a>

</body>
</html>
