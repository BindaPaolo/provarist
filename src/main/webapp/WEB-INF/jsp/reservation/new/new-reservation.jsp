<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <%@ page isELIgnored="false"%>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
    <title>Nuova Prenotazione</title>
</head>
<body>

	<h2>Nuova Prenotazione</h2>
    <form:form modelAttribute="reservation" method="post">
        <button type="submit" class="link-button" formaction="/reservation/new/newReservationCustomer?action=show">
            Aggiungi cliente alla prenotazione
        </button>

        <br />

        <table>
            <tr>
                <td>
                    <form:label path="date">Data</form:label>
                </td>
                <td>
                    <form:input type="date" name="date" id="date" path="date"/>
                    <form:errors path="date" class="validationError"/>
                </td>
            </tr>
            <tr>
                <td>
                    <form:label path="shiftEnum">Turno</form:label>
                </td>
                <td>
                    <form:select name="shiftEnum" path = "shiftEnum">
                        <form:option value = "" label = "Seleziona"/>
                        <form:options items = "${shiftEnum.values()}" />
                    </form:select>
                    <form:errors path="shiftEnum" class="validationError"/>
                </td>
            </tr>
            <tr>
                <td>Camerieri</td>
                <td>
                    <table>
                        <c:forEach items="${waitersList}" var="employee">
                            <tr>
                                <td><form:checkbox path="reservation_waiters" value="${employee}" /></td>
                                <td><c:out value="${employee.firstName}" /> <c:out value="${employee.lastName}" /></td>
                            </tr>
                        </c:forEach>
                        <form:errors path="reservation_waiters" class="validationError" />
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <br />
                    <form:button type="submit" name="cancel" formaction="/reservation/new/cancelReservation">Annulla prenotazione</form:button>
                </td>
                <td>
                    <br />
                    <form:button type="submit" name="save" formaction="/reservation/new/saveReservation">Salva prenotazione</form:button>
                </td>
            </tr>
        </table>

        <br />
        <form:errors path="reservation_customers" class="validationError"/>

        <h2>Clienti</h2>
        <table>
            <tr>
                <th>Nome</th>
                <th>Cognome</th>
                <th>Allergie</th>
                <th>Numero di cellulare</th>
                <th>Raccomandato da (numero di cellulare)</th>
             </tr>
             <c:forEach var="customer" items="${reservation.getReservation_customers()}">
                  <tr>
                      <td>${customer.firstName}</td>
                      <td>${customer.lastName}</td>
                      <c:choose>
                      	<c:when test="${not empty customer.allergies}">
                      		<td>${customer.allergies}</td>
                      	</c:when>
                      	<c:otherwise>
                      		<td></td>
                      	</c:otherwise>
                      </c:choose>
                      <td>${customer.mobileNumber}</td>
                      <td>${customer.recommendedBy.mobileNumber}</td>
                      <td>
                        <form:button type="submit" formaction="/reservation/new/modifyReservationCustomer/${customer.firstName}&${customer.lastName}&${customer.mobileNumber}?action=edit">Modifica</form:button>				
                      </td>
                      <td>
                        <form:button type="submit" formaction="/reservation/new/modifyReservationCustomer/${customer.firstName}&${customer.lastName}&${customer.mobileNumber}?action=delete">Elimina</form:button>
                      </td>
                  </tr>
             </c:forEach>
        </table>
    </form:form>

</body>
</html>