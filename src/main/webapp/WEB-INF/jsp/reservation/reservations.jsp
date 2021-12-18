<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista Prenotazioni</title>
<style type="text/css">
  td {
    padding: 0 15px;
  }
</style>
</head>
<body>
	<div>
		<div>
			<h2>Prenotazioni</h2>
			<a href="/">Torna alla homepage</a>
			<hr />
			<a href="/new-reservation">
				<button type="submit">Aggiungi nuova prenotazione</button>
			</a> <br />
			<br />
            <div>
                <table>
                    <tr>
                        <th>Id</th>
                        <th>Turno</th>
                        <th>Data</th>
                        <th>Clienti</th>
                        <th>Tavoli</th>
                        <th></th>
                    </tr>
                    <c:forEach var="reservation" items="${reservations}">
                        <tr>
                            <td>${reservation.id}</td>
                            <td>${reservation.shiftEnum}</td>
                            <td>${reservation.date}</td>
                            <td><a href="/manageReservationClients/${reservation.id}">Gestisci</a></td>
							<td>${reservation.reservedTables}</td>
                            <td><form action="/deleteReservation/${reservation.id}" method="post">
                                    <input type="submit" value="Elimina" />
                                </form></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
		</div>
	</div>
</body>
</html>