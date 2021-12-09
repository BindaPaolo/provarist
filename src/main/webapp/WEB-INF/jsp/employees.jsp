<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista Dipendenti</title>
</head>
<body>
	<div>
		<div>
			<h2>Dipendenti</h2>
			<hr />
			<a href="/new-employee">
				<button type="submit">Add new employee</button>
			</a> <br />
			<br />
			<div>
				<div>
					<div>Lista dipendenti</div>
				</div>
				<div>
					<table>
						<tr>
							<th>Id</th>
							<th>Nome</th>
							<th>Cognome</th>
							<th>Ruolo</th>
							<th>C.F.</th>
						</tr>
						<c:forEach var="employee" items="${employees}">
							<tr>
								<td>${employee.id}</td>
								<td>${employee.firstName}</td>
								<td>${employee.lastName}</td>
								<td>${employee.role}</td>
								<td>${employee.cf}</td>
								<td><a href="/showEmployee/${employee.id}">Edit</a>
									<form action="/deleteEmployee/${employee.id}" method="post">
										<input type="submit" value="Delete" />
									</form></td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>