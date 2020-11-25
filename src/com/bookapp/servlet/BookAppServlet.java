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

		// get session object
		HttpSession session = request.getSession();
		final Object lock = session.getId().intern();

		synchronized (lock) {

			// Reset all messages from previous requests;
			session.setAttribute("message", null);
			session.setAttribute("requestMessage", null);
			session.setAttribute("loginMessage", null);
			session.setAttribute("joinMessage", null);

			// get member attribute from existing session
			Member member = (Member) session.getAttribute("member");

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
				String loginMessage = null;
				member = MemberDB.login(email, password);

				if (member == null) {
					loginMessage = "Could not find that user in the database. Check your credentials and try again, or join our community.";
				} else {
					loginMessage = "Member is logged in as " + member.toString();
				}
				session.setAttribute("loginMessage", loginMessage);
				session.setAttribute("member", member);
			}

			// ACTION: JOIN
			if (action.equalsIgnoreCase("join")) {

				String joinMessage = null;
				member = buildMemberFromRequest(request);

				if (member.getErrorMsg().equalsIgnoreCase("")) {
					if (MemberDB.emailExists(member.getEmail())) {
						joinMessage = "There is already a membership associated with that email address.";
						url = "/join.jsp";
					} else {
						int result = MemberDB.insert(member);
						if (result != 0) {
							url = "/join_success.jsp";
						} else {
							joinMessage = "There was an error joining. Try again. ";
							url = "/join.jsp";
						}
					}
				}
				session.setAttribute("member", member);
				session.setAttribute("joinMessage", joinMessage);
			}

			// ACTION: Search
			if (action.equalsIgnoreCase("search")) {

				String searchString = request.getParameter("searchString") != null
						? request.getParameter("searchString")
						: "";

				TreeSet<Book> books = BookDB.search(searchString);
				session.setAttribute("searchBooks", books);
				session.setAttribute("searchMessage", String.format("Your search returned %d results. ", books.size()));
				
				if (memberLoginGood(member)) {
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));
				}
				url = "/index.jsp";
			}

			// ACTION: Register a book
			if (action.equalsIgnoreCase("registerBook")) {

				String message = null;
				Book book = null;

				if (memberLoginGood(member)) {
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
				session.setAttribute("message", message);
			}

			// ACTION: Clear search results (clearSearch)
			if (action.equalsIgnoreCase("clearSearch")) {
				session.setAttribute("searchBooks", null);
				session.setAttribute("searchMessage", null);
			}

			// ACTION: Request a book
			if (action.equalsIgnoreCase("requestBook")) {
				session.setAttribute("message", submitRequestToBorrow(request, member));
				
				if (memberLoginGood(member)) {
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));
				}
				
			}

			// ACTION: Manage Requests
			if (action.equalsIgnoreCase("manageRequests")) {

				String requestMessage = null;

				if (memberLoginGood(member)) {

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";
				} else {
					requestMessage = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
				session.setAttribute("requestMessage", requestMessage);
			}

			// ACTION: Approve request
			if (action.equalsIgnoreCase("approve")) {

				String requestMessage = null;

				if (memberLoginGood(member)) {
					boolean success = RequestDB.approve(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						requestMessage = "The request was approved. ";
					} else {
						requestMessage = "There was an error approving the request. Please try again. ";
					}
				} else {
					requestMessage = "You need to login to manage requests. Please login or join or community. ";
					url = "/index.jsp";
				}
				session.setAttribute("requestMessage", requestMessage);
			}

			// ACTION: Deny request
			if (action.equalsIgnoreCase("deny")) {

				String requestMessage = null;

				if (memberLoginGood(member)) {
					boolean success = RequestDB.deny(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						requestMessage = "The request was declined. ";
					} else {
						requestMessage = "There was an error declining the request. Please try again. ";
					}
				} else {
					requestMessage = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
				session.setAttribute("requestMessage", requestMessage);
			}

			// ACTION: Receive book (as requester)
			if (action.equalsIgnoreCase("receive")) {

				String requestMessage = null;

				if (memberLoginGood(member)) {
					boolean success = RequestDB.receive(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						requestMessage = "The the book has been marked 'received'. ";
					} else {
						requestMessage = "There was an error updating the book as received. Please try again. ";
					}
				} else {
					requestMessage = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
				session.setAttribute("requestMessage", requestMessage);
			}

			// ACTION: Cancel book (as requester)
			if (action.equalsIgnoreCase("cancelRequest")) {

				String requestMessage = null;

				if (memberLoginGood(member)) {
					boolean success = RequestDB.cancelRequest(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						requestMessage = "The the request has been marked 'Canceled by Requester'. ";
					} else {
						requestMessage = "There was an error updating the request as canceled. Please try again. ";
					}
				} else {
					requestMessage = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
				session.setAttribute("requestMessage", requestMessage);
			}

			// ACTION: Return Book to Owner
			if (action.equalsIgnoreCase("returnBook")) {

				String requestMessage = null;

				if (memberLoginGood(member)) {
					boolean success = RequestDB.returnBook(member, request.getParameter("requestId"));

					session.setAttribute("requestsToMe", RequestDB.getRequestsToMe(member.getId()));
					session.setAttribute("requestsToOthers", RequestDB.getRequestsToOthers(member.getId()));

					url = "/manage_requests.jsp";

					if (success) {
						requestMessage = "The book has been marked 'returned'. ";
					} else {
						requestMessage = "There was an error updating the book as received. Please try again. ";
					}
				} else {
					requestMessage = "You need to login to manage requests. Please login or join or community. ";
					url = "/login.jsp";
				}
				session.setAttribute("requestMessage", requestMessage);
			}

			// OTHER ACTIONS GO HERE

		}

		// Forward the request
		getServletContext().getRequestDispatcher(url).forward(request, response);
	}

	private boolean memberLoginGood(Member member) {

		member = MemberDB.checkLogin(member);
		return member != null && member.isLoggedIn();
	}

	private String submitRequestToBorrow(HttpServletRequest request, Member requester) {

		String bookIdAsString = request.getParameter("bookRequested");
		int bookId = 0;

		if (!memberLoginGood(requester)) {
			return "You must login to request to borrow a book. ";
		}

		try {
			bookId = Integer.parseInt(bookIdAsString);
		} catch (Exception e) {
			return "An invalid book id was submitted. ";
		}

		// TODO - verify with the BookDB class that this is a valid book id, and use that
		// as the condition here, return message if not

		if (!requester.canLendAndBorrow()) {
			return "You cannot submit requests to borrow while in limited membership status. ";
		}

		Member owner = MemberDB.getBookOwner(bookId);
		if (owner == null) {
			return "Cannot find the owner of that book. No request was submitted. ";
		}
		if (!owner.canLendAndBorrow()) {
			return "The owner of this book is in limited status. You cannot borrow books from members in limited membership status. ";
		}

		if (!RequestDB.requestExists(bookId, requester)) {
			int result = RequestDB.insert(bookId, requester);
			if (result != 0) {
				return "Your request has been submitted. ";
			} else {
				return "There was an error submitting the request. Please try again. ";
			}
		} else {
			return "You have already submitted a request to borrow that book. ";
		}
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

	private Member buildMemberFromRequest(HttpServletRequest request) {

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
