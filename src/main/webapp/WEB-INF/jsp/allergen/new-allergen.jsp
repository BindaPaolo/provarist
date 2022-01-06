<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <%@ page isELIgnored="false" %>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<style><%@include file="/WEB-INF/static/css/styles.css"%></style>
    <title>Lista Allergeni</title>
</head>
<body>
    <h2>Nuovo Allergene</h2>

    <form:form modelAttribute="allergen" method="post">
        <table>
            <tr>
                <td>
                    <form:label path="name">Nome</form:label>
                </td>
                <td>
                    <form:input type="text" id="name" path="name" />
                    <form:errors path="name" class="validationError" />
                    <c:if test="${not empty allergenError}">
                       <div class="validationError">Errore: ${allergenError}</div>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>
                    <br /><form:button type="submit" formaction="/cancelAllergenOp">Annulla</form:button>
                </td>
                <td>
                    <br /><form:button type="submit" formaction="/addAllergen">Aggiorna</form:button>
                </td>
            </tr>
        </table>
    </form:form>
</body>
</html>