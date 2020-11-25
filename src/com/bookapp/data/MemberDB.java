package com.bookapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bookapp.business.AccountType;
import com.bookapp.business.Member;

public class MemberDB {

	public static int insert(Member member) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "INSERT INTO Member (lastName, firstName, email, userName, password, accountType) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, member.getLastName());
			ps.setString(2, member.getFirstName());
			ps.setString(3, member.getEmail());
			ps.setString(4, member.getUserName());
			ps.setString(5, member.getPassword());
			ps.setInt(6, 1);
			return ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}

	public static boolean emailExists(String email) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT email FROM Member WHERE email = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, email);
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

	public static Member login(String email, String password) {

		Member member = null;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM Member AS m JOIN AccountType AS a ON m.accountType = a.id WHERE email = ? and password = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccountType accountType = new AccountType();
				accountType.setId(rs.getInt(8));
				accountType.setTitle(rs.getString(9));
				
				
				int id = rs.getInt(1);
				String firstName = rs.getString(3);
				String lastName = rs.getString(4);
				String userName = rs.getString(5);

				member = new Member(email, firstName, lastName, userName, password);
				member.setId(id);
				member.setAccountType(accountType);
				member.setLoggedIn(true);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return member;
	}

	public static Member checkLogin(Member member) {

		Member output = null;

		if (member != null) {
			output = login(member.getEmail(), member.getPassword());
		}

		return output;
	}

	public static Member getMember(int memberId) {

		Member member = null;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM Member AS m JOIN AccountType AS a ON m.accountType = a.id WHERE id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, memberId);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccountType accountType = new AccountType();
				accountType.setId(rs.getInt(8));
				accountType.setTitle(rs.getString(9));
				
				int id = rs.getInt(1);
				String firstName = rs.getString(3);
				String lastName = rs.getString(4);
				String userName = rs.getString(5);

				member = new Member();
				member.setId(id);
				member.setFirstName(firstName);
				member.setLastName(lastName);
				member.setUserName(userName);
				member.setAccountType(accountType);
				member.setLoggedIn(false);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return member;
	}

	public static Member getBookOwner(int bookId) {

		Member member = null;
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM Book AS b JOIN Member AS o ON b.ownerId = o.id JOIN AccountType AS a ON o.accountType = a.id WHERE b.id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, bookId);
			rs = ps.executeQuery();

			while (rs.next()) {

				AccountType accountType = new AccountType();
				accountType.setId(rs.getInt(16));
				accountType.setTitle(rs.getString(17));

				int id = rs.getInt(9);
				String email = rs.getString(10);
				String firstName = rs.getString(11);
				String lastName = rs.getString(12);
				String userName = rs.getString(13);

				member = new Member();
				member.setId(id);
				member.setEmail(email);
				member.setFirstName(firstName);
				member.setLastName(lastName);
				member.setUserName(userName);
				member.setAccountType(accountType);
				member.setLoggedIn(false);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return member;
	}

}
