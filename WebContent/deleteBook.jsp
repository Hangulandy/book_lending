<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/cannot_view.jsp" />
</c:if>


<c:if test="${member.isLoggedIn() == true}">
    <h1>Delete Book</h1>
    <div class="deleteWarning">
		<p>Deleting this book will remove all requests for the book, and it will be considered returned.</p>
		<p>This action is irreversible.</p>
    </div>
    <form class="sideBySide" action="BookAppServlet" method="post">
        <input type="hidden" name="action" value="deleteBook"/>
        <input type="hidden" name="bookId" value="${bookToDelete.id}"/>
		
        <label class="pad_top">Title:</label>
        <input readonly type="text" name="title" value="${bookToDelete.title}" required/>
        <br>
        <label class="pad_top">Author:</label>
        <input readonly type="text" name="author" value="${bookToDelete.author}" required/>
        <br>
        <label class="pad_top">No. Pages:</label>
        <input readonly type="number" name="pages" value="${bookToDelete.pages}" min="1">
        <br>
        <label class="pad_top">Rec. Age:</label>
        <input readonly type="number" name="recommendedAge" value="${bookToDelete.recommendedAge}" min="1">
        <br>
        <label class="pad_top">Lendable</label>
        <input readonly type="text" name="lendable" 
        	   value="${bookToDelete.isLendable() ? 'Yes' : 'No'}"
        >
        <hr>
        <label>&nbsp;</label>
	    <input class="redButton" type="submit" value="Delete" class="margin_left" />
    </form>
        <form class="sideBySide" action="manage_books.jsp" method="post">
			<input class="button" type="submit" value="Go Back" />
		</form>
</c:if>