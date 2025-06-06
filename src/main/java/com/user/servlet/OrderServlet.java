package com.user.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.DAO.BookDAOImpl;
import com.DAO.BookOrderImpl;
import com.DAO.CartDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDtls;
import com.entity.Book_Order;
import com.entity.Cart;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();

            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("username");
            String email = req.getParameter("email");
            String phno = req.getParameter("phno");
            String address = req.getParameter("address");
            String landmark = req.getParameter("landmark");
            String city = req.getParameter("city");
            String state = req.getParameter("state");
            String pincode = req.getParameter("pincode");
            String paymentType = req.getParameter("payment");
            String totalPrice = req.getParameter("totalPrice");

            String fullAdd = address + "," + landmark + "," + city + "," + state + "," + pincode;

            if ("COD".equals(paymentType)) {

                CartDAOImpl dao = new CartDAOImpl(DBConnect.getConn());
                BookDAOImpl daox = new BookDAOImpl(DBConnect.getConn());
                List<Cart> blist = dao.getBookByUser(id);

                if (blist.isEmpty()) {
                    session.setAttribute("failedMsg", "Add Item");
                    resp.sendRedirect("checkout.jsp");
                    return;
                } else {
                    BookOrderImpl dao2 = new BookOrderImpl(DBConnect.getConn());
                    ArrayList<Book_Order> orderList = new ArrayList<>();
                    Random r = new Random();

                    for (Cart c : blist) {
                        BookDtls b = daox.getBookById(c.getBid());

                        Book_Order o = new Book_Order();
                        o.setOrderId("BOOK-ORD-00" + r.nextInt(1000));
                        o.setUserName(name);
                        o.setEmail(email);
                        o.setPhno(phno);
                        o.setFulladd(fullAdd);
                        o.setBookName(c.getBookName());
                        o.setAuthor(c.getAuthor());
                        o.setPrice(c.getPrice() + "");
                        o.setPaymentType("COD");
                        // o.setIsbn(b.getIsbn()); // Uncomment if ISBN needed
                        orderList.add(o);
                    }

                    if ("noselect".equals(paymentType)) {
                        session.setAttribute("failedMsg", "Choose Payment Method");
                        resp.sendRedirect("checkout.jsp");
                    } else {
                        boolean f = dao2.saveOrder(orderList);

                        if (f) {
                            // ✅ Clear the cart from the database
                            dao.deleteBook(id);

                            // ✅ Optional: clear from session if stored
                            session.removeAttribute("cart");

                            resp.sendRedirect("order_success.jsp");
                        } else {
                            session.setAttribute("failedMsg", "Your Order Failed");
                            resp.sendRedirect("checkout.jsp");
                        }
                    }
                }
            } else if ("noselect".equals(paymentType)) {
                session.setAttribute("failedMsg", "Choose Payment Method");
                resp.sendRedirect("checkout.jsp");
            } else {
                resp.sendRedirect("cardpayment.jsp?un=" + name + "&&em=" + email + "&&ph=" + phno + "&&ad=" + fullAdd
                        + "&&ta=" + totalPrice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
