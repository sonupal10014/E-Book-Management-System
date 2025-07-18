import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.DAO.UserDAOImpl;
import com.DB.DBConnect;
import com.entity.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Create DAO instance
            UserDAOImpl dao = new UserDAOImpl(DBConnect.getConn());
            HttpSession session = req.getSession();

            // Get user input
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            // Check if email matches admin (hardcoded for simplicity)
            if ("admin@gmail.com".equals(email)) {
                // Admin login
                if ("admin".equals(password)) {
                    User us = new User();
                    us.setName("Admin");
                    session.setAttribute("userobj", us);
                    resp.sendRedirect("admin/home.jsp");
                } else {
                    session.setAttribute("failedMsg", "Invalid Admin Password");
                    resp.sendRedirect("login.jsp");
                }
            } else {
                // Get user from the database
                User us = dao.login(email,password);
                if (us != null && BCrypt.checkpw(password, us.getPassword())) {
                    // Password matches
                    session.setAttribute("userobj", us);
                    resp.sendRedirect("index.jsp");
                } else {
                    // Invalid credentials
                    session.setAttribute("failedMsg", "Email or Password is Invalid");
                    resp.sendRedirect("login.jsp");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("login.jsp");
        }
    }
}
