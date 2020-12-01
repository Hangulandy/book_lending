package com.bookapp.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import com.bookapp.business.Book;
import com.bookapp.business.Member;
import com.bookapp.business.Request;

public class RequestDB {

	public static boolean requestExists(int id, Member member) {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT id FROM Request WHERE bookId = ? AND requesterId = ? AND dateClosedByOwner IS NULL";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ps.setInt(2, member.getId());
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

	public static int insert(int idAsInt, Member member) {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();

		Date sqlDate = new java.sql.Date(date.getTime());

		String query = "INSERT INTO Request (bookId, requesterId, dateRequested) VALUES (?, ?, ?)";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, idAsInt);
			ps.setInt(2, member.getId());
			ps.setDate(3, sqlDate);
			return ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
	
	public static int delete(Request request) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		
		String query = "DELETE FROM Request "
				+ "WHERE id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, request.getId());
			return ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
	}
		
	
	public static TreeSet<Request> getRequestsToMe(int ownerId) {

		TreeSet<Request> output = new TreeSet<Request>();

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "SELECT * FROM Request AS r INNER JOIN Book AS b ON r.bookId = b.id INNER JOIN Member AS m ON r.requesterId = m.id WHERE b.ownerId = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, ownerId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Member requester = new Member();
				requester.setId(rs.getInt(17));
				requester.setEmail(rs.getString(18));
				requester.setUserName(rs.getString(21));

				//FIXME: Not getting whole book object
				Book book = new Book();
				book.setId(rs.getInt(9));
				book.setTitle(rs.getString(10));
				book.setAuthor(rs.getString(11));

				Request request = new Request();
				request.setId(rs.getInt(1));
				request.setBook(book);
				request.setRequester(requester);
				request.setDateRequested(rs.getDate(4));
				request.setDateAnswered(rs.getDate(5));
				request.setDateReceivedByRequester(rs.getDate(6));
				request.setDateClosedByOwner(rs.getDate(7));
				request.setRemarks(rs.getString(8));

				output.add(request);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return output;
	}

	public static Object getRequestsToOthers(Integer requesterId) {

		TreeSet<Request> output = new TreeSet<Request>();

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		String query = "SELECT * FROM Request AS r INNER JOIN Book AS b ON r.bookId = b.id INNER JOIN Member AS owner ON b.ownerId = owner.id WHERE r.requesterId = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, requesterId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Member owner = new Member();
				owner.setId(rs.getInt(17));
				owner.setEmail(rs.getString(18));
				owner.setUserName(rs.getString(21));

				Book book = new Book();
				book.setId(rs.getInt(9));
				book.setTitle(rs.getString(10));
				book.setAuthor(rs.getString(11));
				book.setOwner(owner);

				Request request = new Request();
				request.setId(rs.getInt(1));
				request.setBook(book);
				request.setDateRequested(rs.getDate(4));
				request.setDateAnswered(rs.getDate(5));
				request.setDateReceivedByRequester(rs.getDate(6));
				request.setDateClosedByOwner(rs.getDate(7));
				request.setRemarks(rs.getString(8));

				output.add(request);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return output;
	}

	public static boolean approve(Member member, String parameter) {

		int requestId = 0;

		try {
			requestId = Integer.parseInt(parameter);
		} catch (NumberFormatException e) {
			return false;
		}

		// Make sure this member has authority to modify this request
		if (!isRequestToMe(member, requestId)) {
			return false;
		}

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Date sqlDate = new java.sql.Date(date.getTime());

		String query = "Update Request SET dateAnswered = ?, remarks = ? WHERE id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setDate(1, sqlDate);
			ps.setString(2, "Approved. ");
			ps.setInt(3, requestId);
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return false;
	}

	private static boolean isRequestToMe(Member member, int requestId) {

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM Request AS r JOIN Book as b ON r.bookId = b.id WHERE b.ownerId = ? AND r.id = ?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, member.getId());
			ps.setInt(2, requestId);
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

	public static boolean deny(Member member, String parameter) {

		int requestId = 0;

		try {
			requestId = Integer.parseInt(parameter);
		} catch (NumberFormatException e) {
			return false;
		}

		// Make sure this member has authority to modify this request
		if (!isRequestToMe(member, requestId)) {
			return false;
		}

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Date sqlDate = new java.sql.Date(date.getTime());

		String query = "Update Request SET dateAnswered = ?, dateClosedByOwner = ?, remarks = ? WHERE id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setDate(1, sqlDate);
			ps.setDate(2, sqlDate);
			ps.setString(3, "Declined. ");
			ps.setInt(4, requestId);
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return false;
	}

	public static boolean receive(Member member, String parameter) {

		int requestId = 0;

		try {
			requestId = Integer.parseInt(parameter);
		} catch (NumberFormatException e) {
			return false;
		}

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Date sqlDate = new java.sql.Date(date.getTime());

		String query = "Update Request SET dateReceivedByRequester = ? WHERE id = ? AND requesterId = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setDate(1, sqlDate);
			ps.setInt(2, requestId);
			ps.setInt(3, member.getId());
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return false;
	}

	public static boolean returnBook(Member member, String parameter) {

		int requestId = 0;

		try {
			requestId = Integer.parseInt(parameter);
		} catch (NumberFormatException e) {
			return false;
		}

		// Make sure this member has authority to modify this request
		if (!isRequestToMe(member, requestId)) {
			return false;
		}

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Date sqlDate = new java.sql.Date(date.getTime());

		String query = "Update Request SET dateClosedByOwner = ?, remarks = ? WHERE id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setDate(1, sqlDate);
			ps.setString(2, "Returned. ");
			ps.setInt(3, requestId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return false;
	}

	public static boolean cancelRequest(Member member, String parameter) {

		int requestId = 0;

		try {
			requestId = Integer.parseInt(parameter);
		} catch (NumberFormatException e) {
			return false;
		}

		ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = pool.getConnection();
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Date sqlDate = new java.sql.Date(date.getTime());

		String query = "Update Request SET dateClosedByOwner = ?, remarks = ? WHERE id = ? AND requesterId = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setDate(1, sqlDate);
			ps.setString(2, "Canceled by Requester. ");
			ps.setInt(3, requestId);
			ps.setInt(4, member.getId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			pool.freeConnection(connection);
		}
		return false;

	}

}
