<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<p class="sideBySide">Options:</p>

<form class="sideBySide" action="BookAppServlet" method="post">
    <input type="text" name="searchString" placeholder="Search by Title or Author" required></input>
    <input class="button" type="submit" value="Search Books" />
    <input type="hidden" name="action" value="search" />
</form>

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/not_logged_in_options.jsp" />
</c:if>

<c:if test="${member.isLoggedIn() == true}">
    <c:import url="/resources/includes/logged_in_options.jsp" />
</c:if>

<hr/>