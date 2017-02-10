<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
   <form action="/one/menu" method="post">
   <fieldset>
   <legend>创建菜单</legend>
      <input type="hidden" name="opr" value="create"/><br/>
      <textarea rows="50" cols="80" name="menujson"></textarea><br/>
      <input type="submit" value="创建菜单"/>
   </fieldset>
   </form>
</body>
</html>