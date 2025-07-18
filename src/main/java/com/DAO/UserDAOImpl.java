package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

import com.entity.Feedback;
import com.entity.User;

public class UserDAOImpl implements UserDAO {

    private Connection conn;

    public UserDAOImpl(Connection conn) {
        super();
        this.conn = conn;
    }

    // Method to register a user with password hashing
    public boolean userRegister(User us) {
        boolean f = false;
        try {
            // Hash the password before saving to DB
            String hashedPassword = BCrypt.hashpw(us.getPassword(), BCrypt.gensalt());
            String sql = "INSERT INTO user(name, email, phno, password) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, us.getName());
            ps.setString(2, us.getEmail());
            ps.setString(3, us.getPhno());
            ps.setString(4, hashedPassword);  // Save the hashed password

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    // Method to login a user and check password using bcrypt
    public User login(String email, String password) {
        User us = null;

        try {
            String sql = "SELECT * FROM user WHERE email=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");  // Get the stored hashed password
                if (BCrypt.checkpw(password, storedPassword)) {  // Verify the entered password
                    us = new User();
                    us.setId(rs.getInt(1));
                    us.setName(rs.getString(2));
                    us.setEmail(rs.getString(3));
                    us.setPhno(rs.getString(4));
                    us.setPassword(storedPassword);  // Store the hashed password
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return us;
    }

    // Method to check password during some operations (e.g., for profile update)
    public boolean checkPassword(int id, String ps) {
        boolean f = false;
        try {
            String sql = "SELECT * FROM user WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (BCrypt.checkpw(ps, storedPassword)) {  // Verify the entered password
                    f = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // Method to update user profile details
    public boolean updateProfile(User us) {
        boolean f = false;
        try {
            String sql = "UPDATE user SET name=?, email=?, phno=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, us.getName());
            ps.setString(2, us.getEmail());
            ps.setString(3, us.getPhno());
            ps.setInt(4, us.getId());

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    // Method to check if email already exists in the database
    public boolean checkUser(String em) {
        boolean f = true;

        try {
            String sql = "SELECT * FROM user WHERE email=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, em);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                f = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // Method to authenticate user using email and phone number
    public boolean checkPasswordAuthen(String email, String phno) {
        boolean f = false;
        try {
            String sql = "SELECT * FROM user WHERE email=? AND phno=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, phno);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    // Method to reset password (used in forgot password flow)
    public boolean forgotPassword(String email, String phno, String password) {
        boolean f = false;
        try {
            // Hash the new password before saving it
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            String sql = "UPDATE user SET password=? WHERE email=? AND phno=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, hashedPassword);  // Store the hashed password
            ps.setString(2, email);
            ps.setString(3, phno);

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    // Method to save feedback for a book
    public boolean saveFeedback(Feedback f) {
        boolean fa = false;
        try {
            String sql = "INSERT INTO feedback(bookId, userId, comment) VALUES(?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, f.getBookId());
            ps.setInt(2, f.getUserId());
            ps.setString(3, f.getComment());

            int i = ps.executeUpdate();

            if (i == 1) {
                fa = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fa;
    }

    // Method to get user details by ID
    public User getUserById(int uid) {
        User us = null;

        try {
            String sql = "SELECT * FROM user WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                us = new User();
                us.setId(rs.getInt(1));
                us.setName(rs.getString(2));
                us.setEmail(rs.getString(3));
                us.setPhno(rs.getString(4));
                us.setPassword(rs.getString(5));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return us;
    }
    
    
}
