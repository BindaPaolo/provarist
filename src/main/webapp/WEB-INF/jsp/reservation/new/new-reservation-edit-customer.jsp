<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <%@ page isELIgnored="false" %>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
    <title>Modifica Cliente</title>
</head>
<body>
    <h2>Modifica Cliente</h2>

    <form:form  modelAttribute="customer" method="post">
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
                    <c:if test="${not empty mobileNumberError}">
                       <div class="validationError">${mobileNumberError}</div>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="recommendedBy.mobileNumber">Raccomandato da (Numero di telefono)</form:label>
                    <form:errors path="mobileNumber" class="validationError"/>
                    <c:if test="${not empty recommendedByError}">
                       <div class="validationError">${recommendedByError}</div>
                    </c:if>
                </td>
                <td>
                    <form:input type="text" id="recommendedBy.mobileNumber" path="recommendedBy.mobileNumber" />
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="allergies">Allergeni</form:label>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty allergensList}">
                            <table>
                                <c:forEach items="${allergensList}" var="allergen">
                                    <tr>
                                        <td><form:checkbox path="allergies" value="${allergen}" /></td>
                                        <td><c:out value="${allergen.name}" /></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:when>
                        <c:otherwise>
                            Non vi sono allergeni disponibili per la scelta.
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td>
                    <br/><button type="submit" formaction="/reservation/new/editReservationCustomer?action=cancel">Annulla</button>
                </td>
                <td>
                    <br/><button type="submit" formaction="/reservation/new/editReservationCustomer?action=save">Salva modifiche</button>
                </td>
            </tr>
        </table>
    </form:form>

</body>
</html>