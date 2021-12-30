<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<%@ page isELIgnored="false"%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<style> <%@include file="/WEB-INF/static/css/styles.css"%></style>
	<title>Gestione Prenotazioni</title>
</head>
<body>

    <h2>Gestione Prenotazioni</h2>
    <a href="/">Torna alla homepage</a>
    <hr />

    <ul>
        <li><a href="/reservation/new-reservation">Nuova prenotazione</a></li>
        <li><a href="/reservation/search/search-reservation-by-customer">Ricerca prenotazione per cliente</a></li>
        <li><a href="/reservation/search/search-reservation-by-date">Ricerca prenotazione per data</a></li>
    </ul>

</body>
</html>