<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <%@ page isELIgnored="false"%>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Lista Tavoli</title>
    <style type="text/css">
      td {
        padding: 0 15px;
      }
    </style>
</head>
<body>
	<div>
		<div>
			<h2>Tavoli</h2>
			<a href="/">Torna alla homepage</a>
			<hr />
			<a href="/new-table">
				<button type="submit">Aggiungi nuovo tavolo</button>
			</a> <br />
			<br />
            <div>
                <table>
                    <tr>
                        <th>Id</th>
                        <th>Capacita</th>
                    </tr>
                    <c:forEach var="table" items="${tables}">
                        <tr>
                            <td>${table.id}</td>
                            <td>${table.capacity}</td>
                            <td><a href="/showTable/${table.id}">Modifica</a>
                                <form action="/deleteTable/${table.id}" method="post">
                                    <input type="submit" value="Elimina" />
                                </form></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
		</div>
	</div>
</body>
</html>