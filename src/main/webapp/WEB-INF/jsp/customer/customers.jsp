<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista Clienti</title>
<style type="text/css">
  td {
    padding: 0 15px;
  }
</style>
</head>
<body>
	<div>
		<div>
			<h2>Clienti</h2>
			<a href="/">Torna alla homepage</a>
			<hr />
			<a href="/customer/new-customer">
				<button type="submit">Aggiungi nuovo cliente</button>
			</a> <br />
			<br />
            <div>
                <table>
                    <tr>
                        <th>Id</th>
                        <th>Nome</th>
                        <th>Cognome</th>
                        <th>Allergie</th>
                        <th>Numero di cellulare</th>
                        <th>Raccomandato da</th>
                    </tr>
                    <c:forEach var="customer" items="${customers}">
                        <tr>
                            <td>${customer.id}</td>
                            <td>${customer.firstName}</td>
                            <td>${customer.lastName}</td>
                            <td>${customer.allergies}</td>
                            <td>${customer.mobileNumber}</td>
                            <td>${customer.recommendedById.id}</td>
                            <td>
                                <a href="/customer/showCustomer/${customer.id}">Modifica</a>
                                <form action="/customer/deleteCustomer/${customer.id}" method="post">
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