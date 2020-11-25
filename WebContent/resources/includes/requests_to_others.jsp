
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h1>Your Requests for Others' Books:</h1>

<c:if test="${requestsToOthers == null || requestsToOthers.size() == 0}">
    <p>You have no requests for others' books.</p>
</c:if>

<c:if test="${requestsToOthers != null && requestsToOthers.size() > 0}">
    <table>
        <tr>
            <th class="left">Title</th>
            <th>Author</th>
            <th>Owner</th>
            <th>Email</th>
            <th>Requested On</th>
            <th>Answer</th>
            <th>Received</th>
            <th>Closed</th>
            <th>Remarks</th>
        </tr>
        <c:forEach items="${requestsToOthers}" var="request">
            <tr>
                <td class="left">${request.book.title}</td>
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
                            <form class="sideBySide" action="BookAppServlet"
                                method="get">
                                <input type="hidden" name="action"
                                    value="receive" />
                                <input type="hidden" name="requestId"
                                    value="${request.id}" />
                                <input class="button" type="submit" value="YES" />
                            </form>
                            <form class="sideBySide" action="BookAppServlet"
                                method="get">
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
                <c:choose>
                    <c:when test="${request.getStage() == 1}">
                        <td><form class="sideBySide"
                                action="BookAppServlet" method="get">
                                <input type="hidden" name="action"
                                    value="cancelRequest" />
                                <input type="hidden" name="requestId"
                                    value="${request.id}" />
                                <input class="redButton" type="submit"
                                    value="Cancel" />
                            </form></td>
                    </c:when>
                    <c:when test="${request.getStage() != 1}">
                        <td>${request.remarks}</td>
                    </c:when>
                </c:choose>

            </tr>
        </c:forEach>
    </table>
</c:if>