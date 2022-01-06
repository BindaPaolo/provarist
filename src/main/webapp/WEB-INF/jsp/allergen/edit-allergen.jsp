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
    <div>
        <h2>Modifica Allergene</h2>
        <a href="#" onclick="history.go(-1)">Torna indietro</a>
        <hr /><br />

        <div>
            <div>
                <form:form action="/updateAllergen/${allergen.id}" modelAttribute="allergen" method="post">
                    <div>
                        <div>
                            <form:label path="name">Nome</form:label>
                            <form:input type="text" id="name" path="name" />
                            <form:errors path="name" />
                            <form:errors path="name" class="validationError"/>
                            <c:if test="${not empty allergenError}">
                               <div class="validationError">Errore: ${allergenError}</div>
                            </c:if>
                        </div>
                    <div>
                        <div>
                        <br />
                            <input type="submit" value="Aggiorna allergene">
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
    </body>
</html>