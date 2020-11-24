<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<p>${message}</p>

<c:if test="${books.size() > 0}">
    <h1>Recent Search Results:</h1>
    <table>
        <tr>
            <th class="left">Title</th>
            <th>Author</th>
            <th>Pages</th>
            <th>Rec. Age</th>
            <th>
                <form>
                    <input class="redButton" type="submit" value="Clear Results" />
                    <input type="hidden" name="action" value="clearBooks" />
                </form>
            </th>
        </tr>
        <c:forEach items="${books}" var="book">
            <tr>
                <td class="left">${book.title}</td>
                <td>${book.author}</td>
                <td>${book.pages}</td>
                <td>${book.id}</td>
                <td><c:if test="${member.isLoggedIn() == true && book.owner.id != member.id && book.lendable}">
                        <form action="BookAppServlet" method="get">
                            <input type="hidden" name="action"
                                value="requestBook" />
                            <input type="hidden" name="bookRequested"
                                value="${book.id}" />
                            <input class="button" type="submit" value="Request Book" />
                        </form>
                    </c:if>
                    <c:if test="${book.owner.id == member.id}"><i>This is your book.</i></c:if></td>                    
            </tr>
        </c:forEach>
    </table>
</c:if>



