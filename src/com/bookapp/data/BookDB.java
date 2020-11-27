package com.bookapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import com.bookapp.business.AccountType;
import org.apache.commons.lang3.BooleanUtils;

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

	public static TreeSet<Book> search(String string) {

		if (string == null || string.equalsIgnoreCase("")) {
			return null;
		}

		TreeSet<Book> output = new TreeSet<Book>();

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "SELECT * FROM Book AS b "
				+ "JOIN Member AS o ON b.ownerId = o.id "
				+ "JOIN AccountType AS oa ON o.accountType = oa.id "
				+ "JOIN Member AS h ON b.holderId = h.id "
				+ "JOIN AccountType AS ha ON h.accountType = ha.id "
				+ "WHERE b.title LIKE ? OR b.author LIKE ?";

		String searchString = "%" + string + "%";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, searchString);
			ps.setString(2, searchString);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AccountType ownerAT = new AccountType();
				ownerAT.setId(rs.getInt(16));
				ownerAT.setTitle(rs.getString(17));

				Member owner = new Member();
				owner.setId(rs.getInt(9));
				owner.setEmail(rs.getString(10));
				owner.setFirstName(rs.getString(11));
				owner.setLastName(rs.getString(12));
				owner.setUserName(rs.getString(13));
				owner.setAccountType(ownerAT);

				AccountType holderAT = new AccountType();
				holderAT.setId(rs.getInt(24));
				holderAT.setTitle(rs.getString(25));

				Member holder = new Member();
				holder.setId(rs.getInt(18));
				holder.setEmail(rs.getString(19));
				owner.setFirstName(rs.getString(20));
				owner.setLastName(rs.getString(21));
				owner.setUserName(rs.getString(22));
				owner.setAccountType(holderAT);

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

	
	/**
	 * Check if book already exists for owner in DB
	 * @param boolean
	 * @return
	 */
	public static boolean bookExists(Book book) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM Book WHERE TITLE = ? AND ownerId = ?";
		
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, book.getTitle());
			ps.setInt(2, book.getOwnerId());
			rs = ps.executeQuery();
			return rs.next();
			
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		} finally {
			DBUtil.closeResultSet(rs);
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
			book.setRecommendedAge(Integer.toString(rs.getInt("recommendedAge")));
			book.setOwnerId(rs.getInt("ownerId"));
			book.setHolderId(rs.getInt("holderId"));
			book.setLendable(BooleanUtils.toBoolean(rs.getInt("lendable")));
		} catch (SQLException e) {
			System.out.println(e);
		}
		return book;
	}
}
