<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/resources/includes/header.jsp" />

<p class="errorMsg">${message}</p>

<%
	session.setAttribute("message", "");
	session.invalidate();
%>

<c:import url="/resources/includes/footer.html" />
