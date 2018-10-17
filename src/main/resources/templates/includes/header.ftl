<sec:authorize ifAllGranted="ROLE_ADMIN">
    <a href="page.htm">Some Admin Stuff</a>
</sec:authorize>
<a href="<c:url value="/j_spring_security_logout" />">Logout <c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></a>