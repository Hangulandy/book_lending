package com.bookapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

		String query = "SELECT email FROM Member " + "WHERE email = ?";
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

		String query = "SELECT * FROM Member WHERE email = ? and password = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			rs = ps.executeQuery();

			while (rs.next()) {
				int id = rs.getInt(1);
				String firstName = rs.getString(3);
				String lastName = rs.getString(4);
				String userName = rs.getString(5);
				int accountType = rs.getInt(7);

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

}
