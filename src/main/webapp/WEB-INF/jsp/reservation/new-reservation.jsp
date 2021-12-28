<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
<title>Nuova Prenotazione</title>
</head>
<body>
	<div>
		<h2>Nuova Prenotazione</h2>
	</div>

		<div>
			<form:form modelAttribute="reservation" method="post">
				<div>
					<button type="submit" class="link-button" formaction="/reservation/addCustomerToReservation">Aggiungi cliente</button>
				</div>		
				<br>	
				<div>
					<form:label path="date">Data</form:label>
					<form:input type="date" name="date" id="date" path="date"/>
					<form:errors path="date" class="validationError"/>
				</div>
				<div>
					<form:label path="shiftEnum">Turno</form:label>
					<form:select name="shiftEnum" path = "shiftEnum">
   						<form:option value = "" label = "Select"/>
   						<form:options items = "${shiftEnum.values()}" />
					</form:select>  
					<form:errors path="shiftEnum" class="validationError"/>
					<br/>
				</div>
				<br>
				<div>
					<form:button type="submit" name="save" formaction="/saveReservation">Salva prenotazione</form:button>
					<form:errors path="reservation_customers" class="validationError"/>
				</div>
				<br>
				<div>
					<button type="submit" name="cancel" formaction="/cancelReservation">Annulla prenotazione</button>
				</div>
				<br>

				<div>
					<table>
					    <tr>
					        <th>Nome</th>
					        <th>Cognome</th>
					        <th>Numero di cellulare</th>
					     </tr>             
					     <c:forEach var="customer" items="${reservation.getReservation_customers()}">
			                  <tr>
			                      <td>${customer.firstName}</td>
			                      <td>${customer.lastName}</td>
			                      <td>${customer.mobileNumber}</td>
			                      <td>	<div>
										<form:button type="submit" name="editReservationCustomer" formaction="/editReservationCustomer/${customer.firstName}&${customer.lastName}&${customer.mobileNumber}">Modifica</form:button>
										</div></td>
								  <td>  <div>
										<form:button type="submit" name="deleteReservationCustomer" formaction="/deleteReservationCustomer/${customer.firstName}&${customer.lastName}&${customer.mobileNumber}">Elimina</form:button>
										</div></td>
			                  </tr>
			             </c:forEach>
			        </table>
				</div>
			</form:form>
		</div>
</body>
</html>