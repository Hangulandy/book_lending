<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/cannot_view.jsp" />
</c:if>


<c:if test="${member.isLoggedIn() == true}">
    <h1>Edit book:</h1>
    <form class="sideBySide" action="BookAppServlet" method="post">
        <input type="hidden" name="action" value="updateBook"/>
        <input type="hidden" name="bookId" value="${bookToEdit.id}"/>
		
        <label class="pad_top">Title:</label>
        <input type="text" name="title" value="${bookToEdit.title}" required/>
        <br>
        <label class="pad_top">Author:</label>
        <input type="text" name="author" value="${bookToEdit.author}" required/>
        <br>
        <label class="pad_top">No. Pages:</label>
        <input type="number" name="pages" value="${bookToEdit.pages}" min="1">
        <br>
        <label class="pad_top">Rec. Age:</label>
        <input type="number" name="recommendedAge" value="${bookToEdit.recommendedAge}" min="1">
        <br>
        <label class="pad_top">Lendable</label>
        <select name="lendable" ${bookToEdit.owner.id != bookToEdit.holder.id ? 'disabled' : '' }>
        	<option ${bookToEdit.isLendable() ? 'selected' : ''} value="1">Yes</option>
        	<option ${!bookToEdit.isLendable() ? 'selected' : ''} value="0">No</option>
        </select>
        <hr>
        <label>&nbsp;</label>
	    <input class="button" type="submit" value="Update" class="margin_left" />
    </form>
        <form class="sideBySide" action="manage_books.jsp" method="post">
			<input class="button" type="submit" value="Go Back" />
		</form>
</c:if>