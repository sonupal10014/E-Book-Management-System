

<%@page import="java.sql.*"%>
<%@page import="com.DB.DBConnect"%>
<%
    // Get the payment ID from Razorpay
    String paymentId = request.getParameter("payment_id");
    int userId = ((com.entity.User)session.getAttribute("userobj")).getId(); // Get the logged-in user ID

    // Get the total price of the cart (this can be passed from previous page)
    double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));

    // Status of payment (optional, but you may want to track)
    String status = "Success"; // assuming the payment was successful

    // SQL query to insert payment details into the 'payments' table
    String query = "INSERT INTO payments (user_id, payment_id, amount, status) VALUES (?, ?, ?, ?)";

    // Establish database connection
    Connection conn = DBConnect.getConn();
    PreparedStatement ps = null;

    try {
        // Create prepared statement
        ps = conn.prepareStatement(query);

        // Set parameters for the prepared statement
        ps.setInt(1, userId); // user_id
        ps.setString(2, paymentId); // payment_id
        ps.setDouble(3, totalPrice); // amount
        ps.setString(4, status); // status (Success/Failed)

        // Execute the insert statement
        int result = ps.executeUpdate();

        if (result > 0) {
            // If the record was inserted successfully
            out.println("<h2>Payment Successful! Your payment ID is: " + paymentId + "</h2>");
        } else {
            // If the record wasn't inserted
            out.println("<h2>Payment Failed to Register</h2>");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        out.println("<h2>Error while saving payment data</h2>");
    } finally {
        // Close the resources
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>
