<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<h1>Join our community</h1>
<p>To become a basic member of our book-lending group, enter your
    information below:</p>
<p>
    <i class="errorMsg">${member.errorMsg}</i>
</p>
<p>
    <i class="errorMsg">${joinMessage}</i>
</p>

<form action="BookAppServlet" method="post">
    <input type="hidden" name="action" value="join" />

    <label class="pad_top">Email:</label>
    <input type="email" name="email" value="${member.email}" required />
    <br>
    <label class="pad_top">First Name:</label>
    <input type="text" name="firstName" value="${member.firstName}" required />
    <br>
    <label class="pad_top">Last Name:</label>
    <input type="text" name="lastName" value="${member.lastName}" required />
    <br>

    <label class="pad_top">Username:</label>
    <input type="text" name="userName" value="${member.userName}" required />
    <br>
    <label class="pad_top">Password:</label>
    <input type="password" name="pw1" required />
    <br>
    <label class="pad_top">Confirm PW:</label>
    <input type="password" name="pw2" required />
    <br>
    <label>&nbsp;</label>
    <input class="button" type="submit" value="Join Now" class="margin_left" />
</form>

<hr/>

<%
    final Object lock = session.getId().intern();

    synchronized (lock) {
        session.setAttribute("joinMessage", null);
    }
%>

<c:import url="/resources/includes/footer.html" />