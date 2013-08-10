<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>basic-authentication</title>
	</head>
	<body>
	
		<h1>basic-authentication test case</h1>
	
		UserPrincipal: <%=request.getUserPrincipal()%> <br/>
		User has role "architect": <%=request.isUserInRole("architect")%> <br/>
		
		<%=request.getSession().getId()%>
		
	</body>
</html>
