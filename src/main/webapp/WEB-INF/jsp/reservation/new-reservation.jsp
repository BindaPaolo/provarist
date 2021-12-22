<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Nuova Prenotazione</title>
</head>
<body>
	<div>
		<h2>Nuova Prenotazione</h2>
	</div>
		<div>
			<a href="/add-customer-to-reservation">
				<button type="submit">Aggiungi cliente</button>
			</a> <br/>
		</div>
		<div>
			<form:form modelAttribute="reservation" method="post">
				<div>
					<form:label path="date">Data</form:label>
					<form:input type="date" id="date" path="date"/>
					<br/>
				</div>
				<div>
					<form:label path="shiftEnum">Turno</form:label>
					<form:select path = "shiftEnum">
   						<form:option value = "NONE" label = "Select"/>
   						<form:options items = "${shiftEnum.values()}" />
					</form:select> <br/> 
				</div>
				<div>
				<button type="submit" name="save" formaction="/saveReservation">Salva prenotazione</button>
				<button type="submit" name="cancel" formaction="/cancelReservation">Annulla prenotazione</button>
				</div>
			</form:form>
		</div>
	<div>
        <table>
            <tr>
                <th>Id</th>
                <th>Nome</th>
                <th>Cognome</th>
                <th>Numero di cellulare</th>
             </tr>
             <c:forEach var="customer" items="${reservation.reservation_customers}">
                  <tr>
                      <td>${customer.person_id}</td>
                      <td>${customer.firstName}</td>
                      <td>${customer.lastName}</td>
                      <td>${customer.mobileNumber}</td>
                      <td><a href="/showCustomer/${customer.person_id}">Modifica</a>
                          <form action="/deleteCustomer/${customer.person_id}" method="post">
                              <input type="submit" value="Elimina" />
                          </form></td>
                  </tr>
             </c:forEach>
        </table>
	</div>
</body>
</html>