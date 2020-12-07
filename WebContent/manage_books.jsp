<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<jsp:useBean id="bookHelper" scope="request" class="com.bookapp.business.BookHelperBean"/>

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/cannot_view.jsp" />
</c:if>

<p>
    <i>${requestMessage}</i>
</p>

<c:if test="${member.isLoggedIn() == true}">
    <h1>Add a book:</h1>
    <form action="BookAppServlet" method="post">
        <input type="hidden" name="action" value="registerBook" />

        <label class="pad_top">Title:</label>
        <input type="text" name="title" required />
        <br>
        <label class="pad_top">Author:</label>
        <input type="text" name="author" required />
        <br>
        <label class="pad_top">No. Pages:</label>
        <input type="number" name="pages" min="1">
        <br>
        <label class="pad_top">Rec. Age:</label>
        <input type="number" name="recommendedAge" min="1">
        <br>
        <label>&nbsp;</label>
        <input class="button" type="submit" value="Register Book" class="margin_left" />
    </form>
</c:if>

<H1>Your Books</H1>
<table>
	<thead>
		<tr>
			<th>Book Id</th>
			<th>Title</th>
			<th>Author</th>
			<th>Pages</th>
			<th>Recommended Age</th>
			<th>Lendable</th>
			<th>Action</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${bookHelper.getMemberBooks(member.id)}" var="book">
		<tr>
			<td>${book.id}</td>
			<td>${book.title}</td>
			<td>${book.author}</td>
			<td>${book.pages}</td>
			<td>${book.recommendedAge}</td>
			<td>${book.lendable}</td>
			<td> 
				<form class="sideBySide" action="BookAppServlet">
					<input type="hidden" name="action" value="editBook"/>
					<input type="hidden" name="bookIdToEdit" value="${book.id}"/>
					<input class="button" type="submit" value="Edit"/>
				</form>
				<form class="sideBySide" action="BookAppServlet">
					<input type="hidden" name="action" value="deleteBook"/>
					<input type="hidden" name="bookId" value="${book.id}"/>
					<input class="redButton"
						   type="submit" 
						   value="Delete" ${book.owner.id != book.holder.id ? 'disabled' : '' }/>
				</form>
			</td>
		</tr>
	</c:forEach>	
	</tbody>
</table>

<c:import url="/resources/includes/footer.html" />