package com.bookapp.servlet;

import java.io.IOException;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bookapp.business.Book;
import com.bookapp.business.Member;
import com.bookapp.business.Request;
import com.bookapp.data.BookDB;
import com.bookapp.data.MemberDB;
import com.bookapp.data.RequestDB;

/**
 * Servlet implementation class BookAppServlet
 */
@WebServlet("/BookAppServlet")
public class BookAppServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BookAppServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());

		// determine action to take in the servlet
		String action = request.getParameter("action");
		if (action == null) {
			action = "home";
		}

		// defaults
		String url = "/index.jsp";
		String message = "";

		// get session object
		HttpSession session = request.getSession();
		final Object lock = session.getId().intern();

		// initialize variables to be used as session attributes
		Member member;
		TreeSet<Book> books;

		synchronized (lock) {

			// get attributes from existing session
			member = (Member) session.getAttribute("member");
			books = (TreeSet<Book>) session.getAttribute("books");

			// ACTION: HOME
			if (action.equalsIgnoreCase("home")) {
				// will just execute defaults which lead to index
			}

			// ACTION: LOGOUT
			if (action.equalsIgnoreCase("logout")) {
				session.invalidate();
			}

			// ACTION: LOGIN
			if (action.equalsIgnoreCase("login")) {
				String email = request.getParameter("email");
				String password = request.getParameter("password");
				member = MemberDB.login(email, password);

				if (member == null) {
					message = "Could not find that user in the database. Check your credentials and try again, or join our community.";
				} else {
					System.out.println("Member is logged in as " + member.toString());
				}
			}

			// ACTION: JOIN
			if (action.equalsIgnoreCase("join")) {

				// Validate proper information exists - what information do we need? what order
				// should we check?
				// Completeness of join form is all that matters
				// Build the member object
				// Check the DB that it doesn't already exist (by email)
				// Insert member into DB
				// Return to login

				// 1. Validate necessary information
				// 2. Perform the task
				// 3. Redirect

				System.out.println("Inside JOIN");
				member = buildMemberData(request);

				if (member.getErrorMsg().equalsIgnoreCase("")) {
					if (MemberDB.emailExists(member.getEmail())) {
						message = "There is already a membership associated with that email address.";
						url = "/join.jsp";
					} else {
						int result = MemberDB.insert(member);
						if (result != 0) {
							message = "";
							url = "/join_success.jsp";
						} else {
							message = "There was an error joining. Try again. ";
							url = "/join.jsp";
						}
					}
				}
			}

			if (action.equalsIgnoreCase("search")) {
				String searchString = "";
				searchString = request.getParameter("searchString") != null ? request.getParameter("searchString") : "";
				books = BookDB.search(searchString);
				url = "/index.jsp";
			}

			// ACTION: Register a book
			if (action.equalsIgnoreCase("registerBook")) {

				member = MemberDB.checkLogin(member);
				Book book = null;
				if (member != null && member.isLoggedIn()) {
					book = buildBookFromData(request, member);

					if (book.getErrorMsg().equalsIgnoreCase("")) {
						int result = BookDB.insert(book);
						if (result != 0) {
							message = String.format("%s was successfully added to the database. ", book.getTitle());
							url = "/manage_books.jsp";
						}
					}
				} else {
					message = "You need to login to add books. ";
					url = "/login.jsp";
				}
			}

			// ACTION: Clear search results (clearBooks)
			if (action.equalsIgnoreCase("clearBooks")) {
				books = null;
			}

			// ACTION: Request book
			if (action.equalsIgnoreCase("requestBook")) {
				message = submitRequestToBorrow(request, member);
			}

			// ACTION: Manage Requests
			if (action.equalsIgnoreCase("manageRequests")) {

				System.out.println("Inside manage requests");

				// first, must check login again
				member = MemberDB.checkLogin(member);
				if (member.isLoggedIn()) {

					System.out.println("Member is logged in");

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";
				} else {
					message = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
			}

			// ACTION: Approve request
			if (action.equalsIgnoreCase("approve")) {

				member = MemberDB.checkLogin(member);

				if (member.isLoggedIn()) {
					boolean success = RequestDB.approve(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						message = "The request was approved. ";
					} else {
						message = "There was an error approving the request. Please try again. ";
					}
				} else {
					message = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}

			}

			// ACTION: Deny request
			if (action.equalsIgnoreCase("deny")) {

				member = MemberDB.checkLogin(member);

				if (member.isLoggedIn()) {
					boolean success = RequestDB.deny(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						message = "The request was declined. ";
					} else {
						message = "There was an error declining the request. Please try again. ";
					}
				} else {
					message = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
			}

			// ACTION: Receive book (as requester)
			if (action.equalsIgnoreCase("receive")) {

				member = MemberDB.checkLogin(member);

				if (member.isLoggedIn()) {
					boolean success = RequestDB.receive(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						message = "The the book has been marked 'received'. ";
					} else {
						message = "There was an error updating the book as received. Please try again. ";
					}
				} else {
					message = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
			}
			
			// ACTION: Cancel book (as requester)
			if (action.equalsIgnoreCase("cancelRequest")) {
				
				member = MemberDB.checkLogin(member);

				if (member.isLoggedIn()) {
					boolean success = RequestDB.cancelRequest(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						message = "The the request has been marked 'Canceled by Requester'. ";
					} else {
						message = "There was an error updating the request as canceled. Please try again. ";
					}
				} else {
					message = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
				
				
			}

			// ACTION: Return Book to Owner
			if (action.equalsIgnoreCase("returnBook")) {

				member = MemberDB.checkLogin(member);

				if (member.isLoggedIn()) {
					boolean success = RequestDB.returnBook(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						message = "The book has been marked 'returned'. ";
					} else {
						message = "There was an error updating the book as received. Please try again. ";
					}
				} else {
					message = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}

			}

			// OTHER ACTIONS GO HERE

		}

		// set session attributes for all cases except logout
		if (!action.equalsIgnoreCase("logout")) {
			setSessionAttributes(session, member, message, books);
		}

		getServletContext().getRequestDispatcher(url).forward(request, response);
	}

	private String submitRequestToBorrow(HttpServletRequest request, Member member) {

		System.out.println("Inside submitRequestToBorrow");

		String message = "";

		String id = request.getParameter("bookRequested");
		System.out.println("Book ID is : " + id);
		int idAsInt = 0;

		if (member == null || !member.isLoggedIn()) {
			message = "You are not currently logged in. ";
		}

		try {
			idAsInt = Integer.parseInt(id);
		} catch (Exception e) {
			message = "Not a valid book id. ";
		}

		// TODO - verify with the book class that this is a valid book id
		if (true) {

			if (!RequestDB.requestExists(idAsInt, member)) {
				System.out.println("There is not a previous request. ");
				int result = RequestDB.insert(idAsInt, member);
				if (result != 0) {
					message = "Your request has been submitted. ";
				} else {
					message = "There was an error submitting the request. Please try again. ";
				}
			} else {
				message = "You have already submitted a request to borrow that book. ";
			}
		}

		System.out.println("Message is : " + message);
		return message;
	}

	private Book buildBookFromData(HttpServletRequest request, Member member) {

		Book book = null;

		if (member != null) {
			String title = request.getParameter("title");
			String author = request.getParameter("author");
			String pages = request.getParameter("pages");
			String recommendedAge = request.getParameter("recommendedAge");

			if (title != null && author != null) {
				book = new Book();
				book.setTitle(title);
				book.setAuthor(author);
				book.setPages(pages);
				book.setRecommendedAge(recommendedAge);
				book.setOwner(member);
				book.setHolder(member);
				book.setLendable(false);
			}
		}
		return book;
	}

	private void setSessionAttributes(HttpSession session, Member member, String message, TreeSet<Book> books) {
		session.setAttribute("message", message);
		session.setAttribute("member", member);
		session.setAttribute("books", books);
	}

	private Member buildMemberData(HttpServletRequest request) {

		String email = request.getParameter("email");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String userName = request.getParameter("userName");
		String password = null;

		Member member = new Member(email, firstName, lastName, userName, password);

		if (pwSame(request)) {
			member.setPassword(request.getParameter("pw1"));
		}

		return member;
	}

	private boolean pwSame(HttpServletRequest request) {

		return request.getParameter("pw1").equalsIgnoreCase(request.getParameter("pw2"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
