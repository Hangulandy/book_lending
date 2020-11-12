<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.html" />

<c:if test="${member.isLoggedIn() == false || member == null}">
    <c:import url="/resources/includes/login.jsp" />
</c:if>

<c:if test="${member.isLoggedIn() == true}">
    <p>Logged In as ${member.userName}</p>
</c:if>

<c:import url="/resources/includes/options.jsp" />

<c:import url="/resources/includes/footer.html" />
