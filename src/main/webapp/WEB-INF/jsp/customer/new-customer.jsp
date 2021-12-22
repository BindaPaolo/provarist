<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista Clienti</title>
</head>
<body>
	<div>
		<h2>Nuovo cliente</h2>
		<div>
			<div>
				<form:form action="/addCustomerToReservation" modelAttribute="customer" method="post">
					<div>
						<div>
							<form:label path="firstName">Nome</form:label>
							<form:input type="text" id="firstName" path="firstName" />
							<form:errors path="firstName" />
						</div>
						<div>
							<form:label path="lastName">Cognome</form:label>
							<form:input type="text" id="lastName" path="lastName" />
							<form:errors path="lastName" />
						</div>
						<div>
							<form:label path="mobileNumber">Numero di cellulare</form:label>
							<form:input type="text" id="mobileNumber" path="mobileNumber" />
							<form:errors path="mobileNumber" />
						</div>
					</div>
					<div>
						<div>
							<input type="submit" value="Aggiungi cliente">
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>