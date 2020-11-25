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

}
