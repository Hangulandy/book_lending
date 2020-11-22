package com.bookapp.servlet;

import java.io.IOException;
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
			action = "join";
		}

		// get session object
		HttpSession session = request.getSession();
		final Object lock = session.getId().intern();

		// default url
		String url = "/index.jsp";

		Member member = (Member) session.getAttribute("member");
		String message = "";
		String underConstruction = "That action is currently under construction.";
		
		// TODO - TreeSet<Book> as a session attribute
		
		synchronized (lock) {

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
				setSessionAttributes(session, member, message);
			}

			// ACTION: JOIN
			if (action.equalsIgnoreCase("join")) {
				
				// Validate proper information exists - what information do we need? what order should we check?
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
				setSessionAttributes(session, member, message);
			}

			if (action.equalsIgnoreCase("search")) {
				// TODO
				message = underConstruction;
				url = "/under_construction.jsp";
				member = null;
				setSessionAttributes(session, member, message);
			}

			// ACTION: Register a book
			if (action.equalsIgnoreCase("registerBook")) {

				member = MemberDB.checkLogin(member);
				Book book = null;
				if (member != null && member.isLoggedIn()) {
					book = buildBookFromData(request, member);
					
					if (book.getErrorMsg().equalsIgnoreCase("")) {
						url = "/manage_books.jsp";
						if (BookDB.bookExists(book)) {
							message = String.format("%s already exists in the database", book.getTitle());
						} else {
							int result = BookDB.insert(book);
							if (result != 0) {
								message = String.format("%s was successfully added to the database. ", book.getTitle());
							}
						}
					}
				} else {
					message = "You need to login to add books. ";
					url = "/login.jsp";
				}
				setSessionAttributes(session, member, message);
			}
			
			// ACTION: Edit a book
			if (action.equalsIgnoreCase("editBook")) {
				member = MemberDB.checkLogin(member);
				
				if (member != null && member.isLoggedIn()) {
					int bookIdToEdit = Integer.parseInt(request.getParameter("bookIdToEdit"));
					Book bookToEdit = BookDB.selectBookById(bookIdToEdit);
					
					//Check that the book exists and person editing the book is the owner
					if (bookToEdit != null && bookToEdit.getOwnerId() == member.getId()) {
						url = "/editBook.jsp";
						
						//No need to keep this in the session
						request.setAttribute("bookToEdit", bookToEdit);
					} else {
						//TODO ERROR MESSAGE
					}
					
				}
			}
			// ACTION: Update book
			if (action.equalsIgnoreCase("updateBook")) {
				System.out.println("UPDATING BOOK");
				member = MemberDB.checkLogin(member);
				int bookId = Integer.parseInt(request.getParameter("bookId"));
				if (member != null && member.isLoggedIn()) {
					Book oldBook = BookDB.selectBookById(bookId);

					if (oldBook != null && oldBook.getOwnerId() == member.getId()) {
						Book updatedBook = buildBookFromData(request, member);
						// Get values from book before updating
						updatedBook.setId(bookId);
						updatedBook.setHolderId(oldBook.getHolderId());
						updatedBook.setLendable(oldBook.isLendable());

						System.out.println("UPDATING BOOK --> " + updatedBook.toString());

						int updated = BookDB.update(updatedBook);
					}
				}
				//TODO add some error message if ifs are false
				
				url = "/manage_books.jsp";
			}
			
			// ACTION: Delete a book
			if (action.equalsIgnoreCase("deleteBook")) {
				System.out.println("DELETING BOOK");
				member = MemberDB.checkLogin(member);
				int bookId = Integer.parseInt(request.getParameter("bookId"));
				if (member != null && member.isLoggedIn()) {
					Book bookToDelete = BookDB.selectBookById(bookId);

					if (bookToDelete != null && bookToDelete.getOwnerId() == member.getId()) {
						int deleted = BookDB.delete(bookToDelete);
						System.out.println("Deleted Book " + deleted);
					}
				}
				//TODO add some error message if ifs are false
				url = "/manage_books.jsp";

			}
			
			// OTHER ACTIONS GO HERE
		}
		
		// TODO - set session attributes?
		getServletContext().getRequestDispatcher(url).forward(request, response);
		
		System.out.println("This is a test for github.");
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
				book.setOwnerId(member.getId());
				book.setHolderId(member.getId());
				book.setLendable(false);
			}
		}
		return book;
	}

	private void setSessionAttributes(HttpSession session, Member member, String message) {
		session.setAttribute("message", message);
		session.setAttribute("member", member);
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
