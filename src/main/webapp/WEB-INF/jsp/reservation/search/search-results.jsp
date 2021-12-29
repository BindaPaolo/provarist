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
		<c:when test="${reservations.size() == 0}">
				<p> Non ci sono prenotazioni con questi parametri!</p>
		</c:when>
		<c:otherwise>
			  <c:forEach var="reservation" items="${reservations}">
			  		<div>
			  			<p>Data: "${reservation.getDate()}"</p>
			  			<p>Turno: "${reservation.getShiftEnum()}"</p>
			  		</div>
			  
			        <div>
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
			        <hr noshade size="20">
			</c:forEach>
		</c:otherwise>
	</c:choose>


</body>
</html>