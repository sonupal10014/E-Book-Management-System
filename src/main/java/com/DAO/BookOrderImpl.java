package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.entity.Book_Order;

public class BookOrderImpl implements BookOrderDAO {

	private Connection conn;

	public BookOrderImpl(Connection conn) {
		super();
		this.conn = conn;
	}

	public int getOrderNo() {
		int i=0;
		try {
			String sql=" Select*from book_order ";
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				i++;
			}
		}
			catch(Exception e) {
				e.printStackTrace();
			}
		return i;
}
	
	public boolean saveOrder(List<Book_Order> blist) {
		boolean f = false;
		try {

			String sql = "insert into book_order(order_id,user_name,email,address,phone,book_name,author,price,payment) values(?,?,?,?,?,?,?,?,?)";

			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);

			for (Book_Order b : blist) {
				ps.setString(1, b.getOrderId());
				ps.setString(2, b.getUserName());
				ps.setString(3, b.getEmail());
				ps.setString(4, b.getFulladd());
				ps.setString(5, b.getPhno());
				ps.setString(6, b.getBookName());
				ps.setString(7, b.getAuthor());
				ps.setString(8, b.getPrice());
				ps.setString(9, b.getPaymentType());
//				ps.setString(10, "Order Processing");
//				ps.setString(11, b.getIsbn());
				ps.addBatch();

			}

			int[] count = ps.executeBatch();
			conn.commit();
			f = true;
			conn.setAutoCommit(true);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public boolean updateStatus(String st, int id) {
		boolean f = false;
		try {

			String sql = "update book_order set status=? where id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, st);
			ps.setInt(2, id);

			int i = ps.executeUpdate();
			if (i == 1) {
				f = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public List<Book_Order> getBook(String email) {
		List<Book_Order> list = new ArrayList<Book_Order>();
		Book_Order o = null;

		try {

			String sql = "select * from book_order where email=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				o = new Book_Order();
				o.setId(rs.getInt(1));
				o.setOrderId(rs.getString(2));
				o.setUserName(rs.getString(3));
				o.setEmail(rs.getString(4));
				o.setFulladd(rs.getString(5));
				o.setPhno(rs.getString(6));
				o.setBookName(rs.getString(7));
				o.setAuthor(rs.getString(8));
				o.setPrice(rs.getString(9));
				o.setPaymentType(rs.getString(10));
//				o.setOrderStatus(rs.getString(11));
//				o.setIsbn(rs.getString(12));
				list.add(o);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

//	public List<Book_Order> getAllOrder() {
//		List<Book_Order> list = new ArrayList<Book_Order>();
//		Book_Order o = null;
//
//		try {
//
//			String sql = "select * from book_order where email=? ";
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setString(1,  Email);		
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				o = new Book_Order();
//				o.setId(rs.getInt(1));
//				o.setOrderId(rs.getString(2));
//				o.setUserName(rs.getString(3));
//				o.setEmail(rs.getString(4));
//				o.setFulladd(rs.getString(5));
//				o.setPhno(rs.getString(6));
//				o.setBookName(rs.getString(7));
//				o.setAuthor(rs.getString(8));
//				o.setPrice(rs.getString(9));
//				o.setPaymentType(rs.getString(10));
//				
//				list.add(o);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return list;
//	}

	
	public List<Book_Order> getAllOrder() {
		List<Book_Order> list = new ArrayList<Book_Order>();
		Book_Order o = null;

		try {
			String sql = "select * from book_order";
			PreparedStatement ps = conn.prepareStatement(sql);	
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				o = new Book_Order();
				o.setId(rs.getInt(1));
				o.setOrderId(rs.getString(2));
				o.setUserName(rs.getString(3));
				o.setEmail(rs.getString(4));
				o.setFulladd(rs.getString(5));
				o.setPhno(rs.getString(6));
				o.setBookName(rs.getString(7));
				o.setAuthor(rs.getString(8));
				o.setPrice(rs.getString(9));
				o.setPaymentType(rs.getString(10));

				list.add(o);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
