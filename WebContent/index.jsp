<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/login.jsp" />
</c:if>

<p>${message}</p>

<c:import url="/resources/includes/search_results.jsp" />

<%
    final Object lock = session.getId().intern();

    synchronized (lock) {
        session.setAttribute("message", null);
    }
%>

<c:import url="/resources/includes/footer.html" />