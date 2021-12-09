<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
    <%@ page isELIgnored="false" %>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Lista Dipendenti</title>
</head>
<body>
    <div>
        <h2>Modifica Dipendente</h2>
        <div>
            <div>
                <form:form action="/updateEmployee/${employee.id}" modelAttribute="employee" method="post">
                    <div>
                        <div>
                            Id: ${employee.id}
                        </div>
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
							<form:label path="role">Ruolo</form:label>
							<form:input type="text" id="role" path="role" />
							<form:errors path="role" />
						</div>
						<div>
							<form:label path="cf">Codice Fiscale</form:label>
							<form:input type="text" id="cf" path="cf" />
							<form:errors path="cf" />
						</div>
                    </div>
                    <div>
                        <div>
                            <input type="submit" value="Aggiorna dipendente">
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
    </body>
</html>