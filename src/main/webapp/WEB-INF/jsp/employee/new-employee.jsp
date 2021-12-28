<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
    <%@ page isELIgnored="false"%>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
    <title>Lista Dipendenti</title>
</head>
<body>

	<h2>Nuovo Dipendente</h2>
    <a href="#" onclick="history.go(-1)">Torna indietro</a>
    <hr /><br />

    <form:form action="/addEmployee" modelAttribute="employee" method="post">
        <table>
            <tr>
                <td>
                    <form:label path="firstName">Nome</form:label>
                </td>
                <td>
                    <form:input type="text" id="firstName" path="firstName" />
                    <form:errors path="firstName" class="validationError" />
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="lastName">Cognome</form:label>
                </td>
                <td>
                    <form:input type="text" id="lastName" path="lastName" />
                    <form:errors path="lastName" class="validationError" />
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="role">Ruolo</form:label>
                </td>
                <td>
                    <form:select name="role" path = "role">
                        <form:option value = "" label = "Seleziona"/>
                        <form:options items = "${role.values()}" />
                    </form:select>
                    <form:errors path="role" class="validationError" />
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="cf">Codice Fiscale</form:label>
                </td>
                <td>
                    <form:input type="text" id="cf" path="cf" />
                    <form:errors path="cf" class="validationError" />
                </td>
            </tr>
            <tr>
                <td>
                </td>
                <td>
                    <br /><input type="submit" value="Aggiungi Dipendente">
                </td>
            </tr>
        </table>
    </form:form>

</body>
</html>