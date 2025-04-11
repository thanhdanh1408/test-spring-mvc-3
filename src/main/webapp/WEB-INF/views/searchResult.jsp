<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.abc.entities.User" %>

<h2>Kết quả tìm kiếm cho: "${keyword}"</h2>

<%
    List<User> results = (List<User>) request.getAttribute("results");
    if (results != null && !results.isEmpty()) {
        for (User u : results) {
%>
            <p><%= u.getUsername() %></p>
<%
        }
    } else {
%>
    <div class="text-center mt-5">
        <img src="<%= request.getContextPath() %>/resources/images/not-found.jpg"
             alt="Không tìm thấy"
             class="img-fluid"
             style="max-width: 300px;">
        <p class="text-muted mt-3">Không tìm thấy người dùng nào.</p>
    </div>
<%
    }
%>

<a href="${pageContext.request.contextPath}/" class="btn btn-primary mt-3">🏠 Về trang chủ</a>


