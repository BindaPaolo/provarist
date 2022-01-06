<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <%@ page isELIgnored="false"%>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
    <title>Lista Dipendenti</title>
</head>
<body>
	<h2>Nuovo Dipendente</h2>

    <form:form modelAttribute="employee" method="post">
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
                    <form:errors path="cf" class="validationError"/>
                    <c:if test="${not empty CfError}">
                       <div class="validationError">Errore: ${CfError}</div>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>
                    <br /><form:button type="submit" formaction="/cancelEmployeeOp">Annulla</form:button>
                </td>
                <td>
                    <br /><form:button type="submit" formaction="/addEmployee">Aggiungi</form:button>
                </td>
            </tr>
        </table>
    </form:form>

</body>
</html>