
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h1>Requests for Your Books:</h1>

<c:if test="${requestsToMe == null || requestsToMe.size() == 0}">
    <p>There are no requests for your books.</p>
</c:if>

<c:if test="${requestsToMe != null && requestsToMe.size() > 0}">
    <table>
        <tr>
            <th class="left">Title</th>
            <th>Author</th>
            <th>Requester</th>
            <th>Email</th>
            <th>Requested On</th>
            <th>Answer</th>
            <th>Lent</th>
            <th>Closed</th>
            <th>Remarks</th>
        </tr>
        <c:forEach items="${requestsToMe}" var="request">
            <tr>
                <td class="left">${request.book.title}</td>
                <td>${request.book.author}</td>
                <td>${request.requester.userName}</td>
                <td>${request.requester.email}</td>
                <td>${request.dateRequested}</td>
                <td><c:choose>
                        <c:when test="${request.getStage() == 1}">
                            <form class="sideBySide" action="BookAppServlet" method="get">
                                <input type="hidden" name="action"
                                    value="approve" />
                                <input type="hidden" name="requestId"
                                    value="${request.id}" />
                                <input class="button" type="submit" value="OK" />
                            </form>
                            <form class="sideBySide" action="BookAppServlet" method="get">
                                <input type="hidden" name="action" value="deny" />
                                <input type="hidden" name="requestId"
                                    value="${request.id}" />
                                <input class="redButton" type="submit"
                                    value="NO" />
                            </form>
                        </c:when>
                        <c:when test="${request.dateAnswered != null}">${request.dateAnswered}</c:when>
                    </c:choose></td>
                <td>${request.dateReceivedByRequester}</td>
                <td><c:choose>
                        <c:when test="${request.getStage() == 3}">
                            <form action="BookAppServlet" method="get">
                                <input type="hidden" name="action"
                                    value="returnBook" />
                                <input type="hidden" name="requestId"
                                    value="${request.id}" />
                                <input class="button" type="submit" value="Returned" />
                            </form>
                        </c:when>
                        <c:when test="${request.dateClosedByOwner != null}">${request.dateClosedByOwner}</c:when>
                    </c:choose></td>
                <td>${request.remarks}</td>
            </tr>
        </c:forEach>

    </table>
</c:if>