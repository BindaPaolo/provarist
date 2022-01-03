<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <%@ page isELIgnored="false"%>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Lista Dipendenti</title>
    <style><%@include file="/WEB-INF/static/css/styles.css"%></style>
    <style type="text/css">
      td {
        padding: 0 15px;
      }
    </style>
</head>
<body>
	<div>
		<div>
			<h2>Dipendenti</h2>
			<a href="/">Torna alla homepage</a>
			<hr />
			<c:if test="${not empty dataIntegrityError}">
                <br /><div class="validationError">${dataIntegrityError}</div><br />
            </c:if>
			<a href="/new-employee">
				<button type="submit">Aggiungi nuovo dipendente</button>
			</a> <br />
			<br />
            <div>
                <table>
                    <tr>
                        <th>Id</th>
                        <th>Nome</th>
                        <th>Cognome</th>
                        <th>Ruolo</th>
                        <th>Codice Fiscale</th>
                    </tr>
                    <c:forEach var="employee" items="${employees}">
                        <tr>
                            <td>${employee.id}</td>
                            <td>${employee.firstName}</td>
                            <td>${employee.lastName}</td>
                            <td>${employee.role}</td>
                            <td>${employee.cf}</td>
                            <td>
                                <a href="/showEmployee/${employee.id}">Modifica</a>
                                <form action="/deleteEmployee/${employee.id}" method="post">
                                    <input type="submit" value="Elimina" />
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
		</div>
	</div>
</body>
</html>