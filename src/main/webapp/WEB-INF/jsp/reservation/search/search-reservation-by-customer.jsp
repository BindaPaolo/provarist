<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
	<%@ page isELIgnored="false"%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
	<title>Ricerca prenotazione per cliente</title>
</head>
<body>	
	<div><h1>Ricerca prenotazione per cliente</h1></div>
	
	<div>
		<form:form modelAttribute="customer" method="post">
			<tr>
              <td>
                  <form:label path="firstName">Nome</form:label>
              </td>
              <td>
                  <form:input type="text" id="firstName" path="firstName" />
                  <form:errors path="firstName" class="validationError"/>
              </td>
            </tr>
            
            <br>
            
            <tr>
              <td>
                  <form:label path="lastName">Cognome</form:label>
              </td>
              <td>
                  <form:input type="text" id="lastName" path="lastName" />
                  <form:errors path="lastName" class="validationError"/>
              </td>              
            </tr>
          
            <br>
           
            <tr>
              <td>
                  <br/><button type="submit" formaction="/reservation/search/executeSearchReservationByCustomer">Esegui Ricerca</button>
              </td> 
            </tr>
            
             <br>
             
            <tr>
                <td> 
                	<br/> <button type="submit" formaction="/reservation/search/cancelSearchReservation">Annulla</button>
               </td>
            </tr>
		</form:form>
	</div>
</body>
</html>