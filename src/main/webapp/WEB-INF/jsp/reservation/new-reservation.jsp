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
	<div>
		<div>
			<a href="/add-customer-reservation">
				<button type="submit">Aggiungi cliente</button>
			</a> <br/>
			
			<form:form action="/newReservation" modelAttribute="reservation" method="post">
				<div>
					<form:label path="date">Data</form:label>
					<form:input type="date" id="date" path="date"/>
					<br/>
				</div>
				<div>
					<form:label path="shiftEnum">Turno</form:label>
					<form:select path = "shiftEnum">
   						<form:option value = "NONE" label = "Select"/>
   						<form:options items = "${shiftEnumValues}" />
					</form:select> <br/> 
				</div>
			</form:form>
			<div>

			</div>
		</div>
	 </div>		
		
	</div>
</body>
</html>