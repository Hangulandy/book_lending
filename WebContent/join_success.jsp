<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<%@ page import="com.bookapp.business.Member"%>

<p>
    Welcome to our community, <b>${member.firstName}</b>!
</p>

<p>
    You are registered as a <b>${member.userName}</b>
</p>

<p>
    We will send an email confirmation to: <b>${member.email}</b>
</p>

<c:import url="/resources/includes/footer.html" />
