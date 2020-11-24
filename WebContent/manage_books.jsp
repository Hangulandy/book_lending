<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/cannot_view.jsp" />
</c:if>

<p>
    <i>${message}</i>
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

<c:import url="/resources/includes/footer.html" />