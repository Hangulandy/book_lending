<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.html" />

<jsp:useBean id="bookHelper" scope="request" class="com.bookapp.business.BookHelperBean"/>

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/cannot_view.jsp" />
</c:if>

<p>
    <i>${message}</i>
</p>

<c:if test="${member.isLoggedIn() == true}">
    <p>Logged In as ${member.userName} -- #${member.id}</p>
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
        <hr>
        <label>&nbsp;</label>
        <input type="submit" value="Register" class="margin_left" />
    </form>
</c:if>

<H1>Your Books</H1>
<table>
	<thead>
		<tr>
			<th>Id</th>
			<th>Title</th>
			<th>Author</th>
			<th>Pages</th>
			<th>Recommended Age</th>
			<th>Owner Id</th>
			<th>Holder Id</th>
			<th>Lendable</th>
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
			<td>${book.ownerId}</td>
			<td>${book.holderId}</td>
			<td>${book.lendable}</td>
			<td> 
				<form action="BookAppServlet">
					<input type="hidden" name="action" value="editBook"/>
					<input type="hidden" name="bookIdToEdit" value="${book.id}"/>
					<input type="submit" value="Edit"/>
				</form>
			</td>
			<td>
				<form action="BookAppServlet">
					<input type="hidden" name="action" value="deleteBook"/>
					<input type="hidden" name="bookId" value="${book.id}"/>
					<input type="submit" value="Delete" ${book.ownerId != book.holderId ? 'disabled' : '' }/>
				</form>
			</td>
		</tr>
	</c:forEach>	
	</tbody>
</table>


<H1>!!Debug!! All Books</H1>
<c:forEach items="${bookHelper.getAllBooks()}" var="book">
	<p>${book}</p>
</c:forEach>

<c:import url="/resources/includes/options.jsp" />

<c:import url="/resources/includes/footer.html" />