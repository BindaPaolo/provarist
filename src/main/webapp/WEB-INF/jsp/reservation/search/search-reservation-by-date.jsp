<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
	<%@ page isELIgnored="false"%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
	<title>Ricerca prenotazione per data</title>
</head>
<body>	
	<div><h1>Ricerca prenotazione per data</h1></div>
	
	<div>
		<form:form modelAttribute="dateContainer" method="post">
		
			<c:if test="${not empty emptyDateField}">
                <br /><div class="validationError">${emptyDateField}</div><br />
            </c:if>
			<tr>
              <td>
				<form:label path="date">Data</form:label>
			  </td>
            <td>
				<form:input type="date" name="date" id="date" path="${dateContainer.date}"/>
              </td>
            </tr>
            
            <br>
            <table>
	            <tr>
	              <td>
	                  <br/><button type="submit" formaction="/reservation/search/executeSearchReservationByDate">Esegui Ricerca</button>
	              </td>
			
	                <td> 
	                	<br/> <button type="submit" formaction="/reservation/search/cancelSearchReservation">Annulla</button>
	               </td>
	            </tr>
            </table>
		</form:form>	
	   </div>
</body>
</html>