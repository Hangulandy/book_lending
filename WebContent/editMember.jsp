<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<c:choose>
	<c:when test="${member.isLoggedIn() == false || member == null || member.accountType.title != 'admin'}">
		<c:import url="/resources/includes/cannot_view.jsp" />
	</c:when>

	<c:otherwise>
		<h1>Edit Member:</h1>
		<form class="sideBySide" action="BookAppServlet" method="post">
			<input type="hidden" name="action" value="updateMember"/>
			<input type="hidden" name="memberId" value="${memberToEdit.id}"/>
			
			<label class="pad_top">First Name:</label>
			<input type="text" name="firstName" value="${memberToEdit.firstName}" required/>
			<br>
			<label class="pad_top">Last Name:</label>
			<input type="text" name="lastName" value="${memberToEdit.lastName}" required/>
			<br>
			<label class="pad_top">User Name</label>
			<input type="text" name="userName" value="${memberToEdit.userName}" required>
			<br>
			<label class="pad_top">Email</label>
			<input type="email" name="email" value="${memberToEdit.email}" required>
			<br>
			<label class="pad_top">Change Password</label>
			<input type="password" name="pw1" value="" placeholder="Optional" min=5 max=20>
			<br>
			<label class="pad_top">Confirm Password</label>
			<input type="password" name="pw2" value="" placeholder="Optional" min=5 max=20>
			<br>

			<label class="pad_top">Account Type</label>
			<select name="accountType">
				<option ${memberToEdit.accountType.id == 1 ? 'selected' : ''} value="1">Member</option>
				<option ${memberToEdit.accountType.id == 2 ? 'selected' : ''} value="2">Administrator</option>
				<option ${memberToEdit.accountType.id == 3 ? 'selected' : ''} value="3">Limited</option>
			</select>
			<hr>
			<label>&nbsp;</label>
			<input class="button" type="submit" value="Update" class="margin_left" />
		</form>
		<form class="sideBySide" action="admin.jsp" method="post">
			<input class="button" type="submit" value="Go Back" />
		</form>
	</c:otherwise>
</c:choose>