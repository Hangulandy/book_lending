package com.bookapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import com.bookapp.business.AccountType;

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
				output.add(resultToBook(rs));
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
		
		String query = "SELECT * FROM Book AS b "
						+ "JOIN Member AS o ON b.ownerId = o.id "
						+ "WHERE b.ownerId = ? AND TITLE = ?";
		
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, book.getOwner().getId());
			ps.setString(2, book.getTitle());
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
	public static Book selectBookById(String parameter) {
		
		int bookId = 0;

		try {
			bookId = Integer.parseInt(parameter);
		} catch (NumberFormatException nfe) {
			return null;
		}

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM Book AS b "
				+ "JOIN Member AS o ON b.ownerId = o.id "
				+ "JOIN AccountType AS oa ON o.accountType = oa.id "
				+ "JOIN Member AS h ON b.holderId = h.id "
				+ "JOIN AccountType AS ha ON h.accountType = ha.id "
				+ "WHERE b.id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, bookId);
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
	public static boolean update(Book book) {
		
		if (book == null) {
			return false;
		}
		
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
			ps.setInt(5, book.getOwner().getId());
			ps.setInt(6, book.getHolder().getId());
			ps.setBoolean(7, book.isLendable());
			ps.setInt(8, book.getId());
			return ps.executeUpdate() > 0;
			
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	public static boolean delete(Book book) {
		
		if (book == null) {
			System.out.println("HERE");
			return false;
		}
		
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		
		String query = "DELETE FROM Book "
				+ "WHERE id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, book.getId());
			return ps.executeUpdate() > 0;
			
		} catch (SQLException e) {
			System.out.println(e);
			return false;
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
		
		String query = "SELECT * FROM Book AS b "
				+ "JOIN Member AS o ON b.ownerId = o.id "
				+ "JOIN AccountType AS oa ON o.accountType = oa.id "
				+ "JOIN Member AS h ON b.holderId = h.id "
				+ "JOIN AccountType AS ha ON h.accountType = ha.id ";

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
		
		TreeSet<Book> memberBooks = new TreeSet<>(); 
		
		String query = "SELECT * FROM Book AS b "
				+ "JOIN Member AS o ON b.ownerId = o.id "
				+ "JOIN AccountType AS oa ON o.accountType = oa.id "
				+ "JOIN Member AS h ON b.holderId = h.id "
				+ "JOIN AccountType AS ha ON h.accountType = ha.id "
				+ "WHERE b.ownerId = ?";

		
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, memberId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				memberBooks.add(resultToBook(rs));
			}
			
		} catch (SQLException e) {
			System.out.println(e);
			return memberBooks;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		
		return memberBooks;
	}
	
	public static TreeSet<Book> getBorrowedBooks(int memberId) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		TreeSet<Book> holderBooks = new TreeSet<>(); 
		
		String query = "SELECT * FROM Book AS b "
				+ "JOIN Member AS o ON b.ownerId = o.id "
				+ "JOIN AccountType AS oa ON o.accountType = oa.id "
				+ "JOIN Member AS h ON b.holderId = h.id "
				+ "JOIN AccountType AS ha ON h.accountType = ha.id "
				+ "WHERE b.holderId = ? AND b.ownerId != ?";

		
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, memberId);
			ps.setInt(2, memberId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				holderBooks.add(resultToBook(rs));
			}
			
		} catch (SQLException e) {
			System.out.println(e);
			return holderBooks;
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		
		return holderBooks;
	}
	
	/**
	 * Helper method to turn resultSet to Book Object
	 * @param rs
	 * @return
	 */
	private static Book resultToBook(ResultSet rs) {
		Book book = new Book();
		
		try {
			AccountType ownerAT = new AccountType();
			ownerAT.setId(rs.getInt("oa.id"));
			ownerAT.setTitle(rs.getString("oa.title"));

			Member owner = new Member();
			owner.setId(rs.getInt("o.id"));
			owner.setEmail(rs.getString("o.email"));
			owner.setFirstName(rs.getString("o.firstName"));
			owner.setLastName(rs.getString("o.lastName"));
			owner.setUserName(rs.getString("o.userName"));
			owner.setAccountType(ownerAT);

			AccountType holderAT = new AccountType();
			holderAT.setId(rs.getInt("ha.id"));
			holderAT.setTitle(rs.getString("ha.title"));

			Member holder = new Member();
			holder.setId(rs.getInt("h.id"));
			holder.setEmail(rs.getString("h.email"));
			holder.setFirstName(rs.getString("h.firstName"));
			holder.setLastName(rs.getString("h.lastName"));
			holder.setUserName(rs.getString("h.userName"));
			holder.setAccountType(holderAT);

			book.setId(rs.getInt("b.id"));
			book.setTitle(rs.getString("b.title"));
			book.setAuthor(rs.getString("b.author"));
			book.setPages(rs.getInt("b.pages"));
			book.setRecommendedAge(rs.getInt("b.recommendedAge"));
			book.setOwner(owner);
			book.setHolder(holder);
			book.setLendable(rs.getBoolean("b.lendable"));
		} catch (SQLException e) {
			System.out.println(e);
		}
		return book;
	}
}
