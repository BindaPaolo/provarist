<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
    <%@ page isELIgnored="false" %>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Modifica Cliente</title>
</head>
<body>
    <div>
        <h2>Modifica Cliente</h2>
        <div>
            <div>
				<form:form modelAttribute="customer" method="post">					<div>
						<div>
							<form:label path="firstName">Nome</form:label>
							<form:input type="text" id="firstName" path="firstName" />
							<form:errors path="firstName" class="validationError"/>
						</div>
						<div>
							<form:label path="lastName">Cognome</form:label>
							<form:input type="text" id="lastName" path="lastName" />
							<form:errors path="lastName" class="validationError"/>
						</div>
						<div>
							<form:label path="mobileNumber">Numero di cellulare</form:label>
							<form:input type="text" id="mobileNumber" path="mobileNumber" />
							<form:errors path="mobileNumber" class="validationError"/>
						</div>
					</div>
					<div>
						<div>
							<button type="submit" formaction="/addCustomer">Salva modifiche</button>
							<button type="submit" formaction="/addCustomer">Annulla</button>
						</div>
					</div>
				</form:form>
            </div>
        </div>
    </div>
    </body>
</html>