package com.bookapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import org.apache.commons.lang3.BooleanUtils;

import com.bookapp.business.Book;

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
			ps.setInt(5, book.getOwnerId());
			ps.setInt(6, book.getHolderId());
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
	
	/**
	 * Get book by title
	 * @param title
	 * @return
	 */
	public static Book selectBookByTitle(String title) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//TODO This needs exact match? regex?
		String query = "SELECT * FROM Book WHERE TITLE = ?";
		
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, title);
			rs = ps.executeQuery();
			
			Book book = null;
			if(rs.next()) {
				book = resultToBook(rs);
			}
			return book;
			
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Get book by ID
	 * @param id
	 * @return
	 */
	public static Book selectBookById(int id) {
ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//TODO This needs exact match? regex?
		String query = "SELECT * FROM Book WHERE ID = ?";
		
try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			Book book = null;
			if (rs.next()) {
				book = resultToBook(rs);
			}
			return book;
			
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Update book in DB
	 * @param book
	 * @return
	 */
	public static int update(Book book) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		
		String query = "UPDATE Book SET "
				+ "title = ?, "
				+ "author = ?, "
				+ "pages = ?, "
				+ "recommendedAge = ?, "
				+ "ownerId = ?, "
				+ "holderId = ?, "
				+ "lendable = ? "
				+ "WHERE id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setInt(3, book.getPages());
			ps.setInt(4, book.getRecommendedAge());
			ps.setInt(5, book.getOwnerId());
			ps.setInt(6, book.getHolderId());
			ps.setBoolean(7, book.isLendable());
			ps.setInt(8, book.getId());
			return ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	public static int delete(Book book) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		
		String query = "DELETE FROM Book "
				+ "WHERE id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, book.getId());
			return ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Query the Book DB and get all the books
	 * @return 
	 */
	public static TreeSet<Book> getAllBooks() {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		TreeSet<Book> allBooks = new TreeSet<>(); 
		
		String query = "SELECT * FROM Book";
		
		try {
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				allBooks.add(resultToBook(rs));
			}
			return allBooks;
			
		} catch (SQLException e) {
			System.out.println(e);
			return allBooks;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Get all books owned by member
	 * @param memberId
	 * @return
	 */
	public static TreeSet<Book> getMemberBooks(int memberId) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		TreeSet<Book> allBooks = new TreeSet<>(); 
		
		String query = "SELECT * FROM Book WHERE ownerId = ?";
		
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, memberId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				System.out.println(rs.getString("title"));
				allBooks.add(resultToBook(rs));
			}
			return allBooks;
			
		} catch (SQLException e) {
			System.out.println(e);
			return allBooks;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	/**
	 * Helper method to turn resultSet to Book Object
	 * @param rs
	 * @return
	 */
	private static Book resultToBook(ResultSet rs) {
		Book book = new Book();
		
		try {
			book.setId(rs.getInt("id"));
			book.setTitle(rs.getString("title"));
			book.setAuthor(rs.getString("author"));
			//FIXME  string or int?
			book.setPages(Integer.toString(rs.getInt("pages")));
			book.setRecommendedAge(Integer.toBinaryString(rs.getInt("recommendedAge")));
			book.setOwnerId(rs.getInt("ownerId"));
			book.setHolderId(rs.getInt("holderId"));
			book.setLendable(BooleanUtils.toBoolean(rs.getInt("lendable")));
		} catch (SQLException e) {
			System.out.println(e);
		}
		return book;
	}
}
