package com.bookapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import com.bookapp.business.Book;
import com.bookapp.business.Member;

public class BookDB {

	public static int insert(Book book) {
		
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "INSERT INTO Book (title, author, pages, recommendedAge, ownerId, holderId, lendable) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setInt(3, book.getPages());
			ps.setInt(4, book.getRecommendedAge());
			ps.setInt(5, book.getOwner().getId());
			ps.setInt(6, book.getHolder().getId());
			ps.setBoolean(7, book.isLendable());
			return ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	public static TreeSet<Book> search(String string){
		
		TreeSet<Book> output = new TreeSet<Book>();
		
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "SELECT * FROM Book AS b JOIN Member AS o ON b.ownerId = o.id JOIN Member AS h ON b.holderId = h.id WHERE title LIKE ? OR author LIKE ?";
		
		String searchString = "%" + string + "%";
				
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, searchString);
			ps.setString(2, searchString);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				
				Member owner = new Member();
				owner.setId(rs.getInt(9));
				owner.setEmail(rs.getString(10));
				owner.setEmail(rs.getString(13));
				
				Member holder = new Member();
				holder.setId(rs.getInt(16));
				holder.setEmail(rs.getString(17));
				holder.setEmail(rs.getString(20));
				
				Book book = new Book();

				book.setId(rs.getInt(1));
				book.setTitle(rs.getString(2));
				book.setAuthor(rs.getString(3));
				book.setPages(rs.getInt(4));
				book.setRecommendedAge(rs.getInt(5));
				book.setOwner(owner);
				book.setHolder(holder);
				book.setLendable(rs.getBoolean(8));
				
				output.add(book);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		
		return output;
	}
	

}
