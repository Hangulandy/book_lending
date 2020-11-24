
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<p>${message}</p>

<h1>Requests to Others:</h1>

<c:if test="${requestsToOthers != null && requestsToOthers.size() > 0}">
    <table>
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Owner</th>
            <th>Email</th>
            <th>Requested On</th>
            <th>Answer</th>
            <th>Received</th>
            <th>Returned</th>
            <th>Remarks</th>
        </tr>
        <c:forEach items="${requestsToOthers}" var="request">
            <tr>
                <td>${request.book.title}</td>
                <td>${request.book.author}</td>
                <td>${request.book.owner.userName}</td>
                <td>${request.book.owner.email}</td>
                <td>${request.dateRequested}</td>
                <td><c:choose>
                        <c:when test="${request.getStage() == 1}">
                            <p>
                                <i>Pending Approval</i>
                            </p>
                        </c:when>
                        <c:when test="${request.dateAnswered != null}">${request.dateAnswered}</c:when>
                    </c:choose></td>
                <td><c:choose>
                        <c:when test="${request.getStage() ==2}">
                            <form action="BookAppServlet" method="get">
                                <input type="hidden" name="action"
                                    value="receive" />
                                <input type="hidden" name="requestId"
                                    value="${request.id}" />
                                <input class="button" type="submit"
                                    value="Receive" />
                            </form>
                            <form action="BookAppServlet" method="get">
                                <input type="hidden" name="action"
                                    value="cancelRequest" />
                                <input type="hidden" name="requestId"
                                    value="${request.id}" />
                                <input class="redButton" type="submit"
                                    value="Cancel" />
                            </form>
                        </c:when>
                        <c:when
                            test="${request.dateReceivedByRequester != null}">${request.dateReceivedByRequester}</c:when>
                    </c:choose></td>
                <td>${request.dateClosedByOwner}</td>
                <td>${request.remarks}</td>
            </tr>
        </c:forEach>

    </table>
</c:if>