<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/resources/includes/header.jsp" />

<jsp:useBean id="memberHelper" scope="request" class="com.bookapp.business.MemberHelperBean"/>

<c:choose>
<c:when test="${member.isLoggedIn() == false || member == null || member.accountType.title != 'admin'}">
	<c:import url="/resources/includes/cannot_view.jsp" />
</c:when>

<c:otherwise>
<p>
	<i>${requestMessage}</i>
</p>

   <H1>Manage Members</H1>
<table>
	<thead>
		<tr>
		<th>Id</th>
		<th>First Name</th>
		<th>Last Name</th>
		<th>User Name</th>
		<th>Email </th>
		<th>Account Type</th>
		<th>Action</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${memberHelper.getAllMembers()}" var="member">
		<tr>
			<td>${member.id}</td>
			<td>${member.firstName}</td>
			<td>${member.lastName}</td>
			<td>${member.userName}</td>
			<td>${member.email}</td>
			<td>${member.accountType.title}</td>
			<td> 
				<form action="BookAppServlet">
					<input type="hidden" name="action" value="editMember"/>
					<input type="hidden" name="memberIdToEdit" value="${member.id}"/>
					<input class="button" type="submit" value="Edit"/>
				</form>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</c:otherwise>
</c:choose>