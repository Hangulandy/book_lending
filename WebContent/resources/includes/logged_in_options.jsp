<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="sideBySide" action="BookAppServlet" method="post">
    <input class="button" type="submit" value="Logout" />
    <input type="hidden" name="action" value="logout" />
</form>

<form class="sideBySide" action="manage_books.jsp">
    <input class="button" type="submit" value="Manage Books" />
</form>

<form class="sideBySide" action="BookAppServlet" method="post">
    <input class="button" type="submit" value="Manage Requests" />
    <input type="hidden" name="action" value="manageRequests" />
</form>

<c:if test="${member.accountType.title == 'admin'}">
<form class="sideBySide" action="admin.jsp" method="post">
    <input class="button" type="submit" value="Administrator Panel" />
</form>
</c:if>