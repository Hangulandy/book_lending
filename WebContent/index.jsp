<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/login.jsp" />
</c:if>

<c:import url="/resources/includes/search_results.jsp" />

<c:import url="/resources/includes/footer.html" />