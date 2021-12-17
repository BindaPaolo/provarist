<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <%@ page isELIgnored="false"%>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Lista Allergeni</title>
    <style type="text/css">
      td {
        padding: 0 15px;
      }
    </style>
</head>
<body>
	<div>
		<div>
			<h2>Allergeni</h2>
			<a href="/">Torna alla homepage</a>
			<hr />
			<a href="/new-allergen">
				<button type="submit">Aggiungi nuovo allergene</button>
			</a> <br />
			<br />
            <div>
                <table>
                    <tr>
                        <th>Id</th>
                        <th>Allergene</th>
                    </tr>
                    <c:forEach var="allergen" items="${allergens}">
                        <tr>
                            <td>${allergen.id}</td>
                            <td>${allergen.name}</td>
                            <td><a href="/showAllergen/${allergen.id}">Modifica</a>
                                <form action="/deleteAllergen/${allergen.id}" method="post">
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