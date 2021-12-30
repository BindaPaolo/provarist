<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <%@ page isELIgnored="false" %>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
    <title>Aggiungi cliente alla prenotazione</title>
</head>
<body>
    <h2>Aggiungi cliente alla prenotazione</h2>
	<form:form   modelAttribute="customer" method="post">
        <table>	
            <tr>
                <td>
                    <form:label path="firstName">Nome</form:label>
                </td>
                <td>
                    <form:input type="text" id="firstName" path="firstName" />
                    <form:errors path="firstName" class="validationError"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="lastName">Cognome</form:label>
                </td>
                <td>
                    <form:input type="text" id="lastName" path="lastName" />
                    <form:errors path="lastName" class="validationError"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="mobileNumber">Numero di cellulare</form:label>
                </td>
                <td>
                    <form:input type="text" id="mobileNumber" path="mobileNumber" />
                    <form:errors path="mobileNumber" class="validationError"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="recommendedById.mobileNumber">Raccomandato da (Numero di telefono)</form:label>
                </td>
                <td>
                    <form:input type="text" id="recommendedById.mobileNumber" path="recommendedById.mobileNumber" />
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
                    <br/><button type="submit" formaction="/reservation/newReservationCustomer?action=add">Aggiungi cliente</button>
                </td>
   
              	<td>
    				<br/><button type="submit" formaction="/reservation/newReservationCustomer?action=cancel">Annulla</button>
				</td>
			</tr>
        </table>
     </form:form>

</body>
</html>