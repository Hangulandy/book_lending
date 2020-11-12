<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/resources/includes/header.html" />

<div class="mainDiv">

    <p class="errorMsg">${message}</p>

    <c:import url="/resources/includes/options.jsp" />

</div>

<%
	session.setAttribute("message", "");
	session.invalidate();
%>


<c:import url="/resources/includes/footer.html" />
