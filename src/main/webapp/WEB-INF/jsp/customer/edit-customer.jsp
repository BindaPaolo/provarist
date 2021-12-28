<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <%@ page isELIgnored="false" %>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Modifica Cliente</title>
</head>
<body>
    <h2>Modifica Cliente</h2>
    <a href="#" onclick="history.go(-1)">Torna indietro</a>
    <hr /><br />

    <form:form action="/updateCustomer/${customer.id}" modelAttribute="customer" method="post">
        <table>
            <tr>
                <td>
                    <form:label path="firstName">Nome</form:label>
                </td>
                <td>
                    <form:input type="text" id="firstName" path="firstName" />
                    <form:errors path="firstName" />
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="lastName">Cognome</form:label>
                </td>
                <td>
                    <form:input type="text" id="lastName" path="lastName" />
                    <form:errors path="lastName" />
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="mobileNumber">Numero di cellulare</form:label>
                </td>
                <td>
                    <form:input type="text" id="mobileNumber" path="mobileNumber" />
                    <form:errors path="mobileNumber" />
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="allergies">Allergeni</form:label>
                </td>
                <td>
                    <table>
                        <c:forEach items="${allergensList}" var="allergen">
                            <tr>
                                <td><form:checkbox path="allergies" value="${allergen}" /></td>
                                <td><c:out value="${allergen.name}" /></td>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <br/><input name="submit" type="submit" value="Aggiorna cliente" />
                </td>
            </tr>
        </table>
    </form:form>

</body>
</html>