<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
<title>Risultati ricerca</title>
</head>
<body>
	<div><h1>Risultati ricerca</h1></div>
	<a href="/reservation/search/search-reservation-by-customer">Nuova ricerca per cliente</a>
	&nbsp;
	&nbsp;
	<a href="/reservation/search/search-reservation-by-date">Nuova ricerca per data</a>	
	&nbsp;
	&nbsp;
	<a href="/">Torna alla Home</a>
	 <hr noshade size="20">
	 
	<c:choose>
		<c:when test="${reservationListContainer.getReservations().size() == 0}">
				<p> Non ci sono prenotazioni con questi parametri!</p>
		</c:when>
		<c:otherwise>
		  <form:form modelAttribute="reservationListContainer" method="post">
			  <c:forEach var="reservation" varStatus="tagStatus" items="${reservationListContainer.reservations}">
			  		<div>
			  			<label>Id prenotazione</label>
			  			<form:input path="reservations[${tagStatus.index}].reservation_id" value="${reservation.getReservation_id()}" readonly="true"/>
			  		</div>
			  		<div>
			  			<label>Data</label>
			  			<form:input path="reservations[${tagStatus.index}].date" value="${reservation.getDate()}" readonly="true"/>
					</div>
					<div>
			  			<label>Turno</label>
			  			<form:input path="reservations[${tagStatus.index}].shiftEnum" value="${reservation.getShiftEnum()}" readonly="true"/>		
			  		</div>
			        <div>
			        	<h3>Clienti</h3>
			        	
			            <table>
			                <tr>
			                    <th>Id</th>
			                    <th>Nome</th>
			                    <th>Cognome</th>
			                    <th>Allergie</th>
			                    <th>Numero di cellulare</th>
			                </tr>
								<c:forEach var="customer" items="${reservation.getReservation_customers()}">
								    <tr>
                            			<td>${customer.id}</td>
                            			<td>${customer.firstName}</td>
                            			<td>${customer.lastName}</td>
                           	 			<td>${customer.allergies}</td>
                            			<td>${customer.mobileNumber}</td>
								    </tr>
								</c:forEach>
			            </table>
			        </div>
			        <br>
			        	<div><form:button type="submit" name="delete" formaction="/reservation/search/deleteReservation/${reservation.getReservation_id()}">Elimina prenotazione</form:button></div>
			        <hr noshade size="20">
				</c:forEach>
		 	</form:form>
		</c:otherwise>
	</c:choose>


</body>
</html>