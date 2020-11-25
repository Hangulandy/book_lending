<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<p>${requestMessage}</p>

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/login.jsp" />
</c:if>

<c:if test="${member.isLoggedIn() == true}">
    <c:import url="/resources/includes/requests_to_me.jsp" />
    <c:import url="/resources/includes/requests_to_others.jsp" />
</c:if>

<%
    final Object lock = session.getId().intern();

    synchronized (lock) {
        session.setAttribute("requestMessage", null);
    }
%>

<c:import url="/resources/includes/footer.html" />