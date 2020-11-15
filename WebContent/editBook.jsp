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
    <h1>Edit book:</h1>
    <form action="BookAppServlet" method="post">
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
        <hr>
        <label>&nbsp;</label>
        <input type="submit" value="Edit" class="margin_left" />
    </form>
</c:if>