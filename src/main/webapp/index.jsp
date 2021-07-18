<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "This is a test page for the Mp3Splitter project" %>
</h1>
<br/>
<a href="hello-servlet?name=mp3-112.mp3">Hello Servlet</a>
<form action="${pageContext.request.contextPath}/HashUploadServlet" enctype="text/plain" method="get">
    <label>userToken:</label>
    <input type="text" name="userToken" value="0">
    <label>begin:</label>
    <input type="text" name="begin" value="0">
    <label>end:</label>
    <input type="text" name="end" value="20000">
    <input type="text" name="hash" value="hash">
    <input type="submit">
</form>
<form action="${pageContext.request.contextPath}/FileUploadServlet" enctype="multipart/form-data" method="post">
    <label>userToken:</label>
    <input type="text" name="userToken" value="0">
    <label>begin:</label>
    <input type="text" name="begin" value="0">
    <label>end:</label>
    <input type="text" name="end" value="20000">
    <input type="file" name="mp3">
    <input type="submit">
</form>
</body>
</html>