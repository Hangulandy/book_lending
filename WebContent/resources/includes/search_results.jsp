<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<p class="sideBySide">${searchMessage}</p>
<c:if test="${searchBooks != null}">
    <form class="sideBySide">
        <input class="redButton" type="submit" value="Clear Results" />
        <input type="hidden" name="action" value="clearBooks" />
    </form>
</c:if>

<c:if test="${searchBooks.size() > 0}">
    <h1>Recent Search Results:</h1>
    <table>
        <tr>
            <th class="left">Title</th>
            <th>Author</th>
            <th>Pages</th>
            <th>Rec. Age</th>
            <c:if test="${member.isLoggedIn() == true}">
                <th>Status</th>
            </c:if>
        </tr>
        <c:forEach items="${searchBooks}" var="book">
            <tr>
                <td class="left">${book.title}</td>
                <td>${book.author}</td>
                <td>${book.pages}</td>
                <td>${book.recommendedAge}</td>
                <c:choose>
                    <c:when
                        test="${member.isLoggedIn() == true && member.canLendAndBorrow() && book.owner.id != member.id && book.lendable && book.owner.canLendAndBorrow()}">
                        <td>
                            <form action="BookAppServlet" method="get">
                                <input type="hidden" name="action"
                                    value="requestBook" />
                                <input type="hidden" name="bookRequested"
                                    value="${book.id}" />
                                <input class="button" type="submit"
                                    value="Request Book" />
                            </form>
                        </td>
                    </c:when>
                    <c:when test="${book.owner.id == member.id}">
                        <td><i>This is your book.</i></td>
                    </c:when>
                </c:choose>
            </tr>
        </c:forEach>
    </table>
</c:if>



