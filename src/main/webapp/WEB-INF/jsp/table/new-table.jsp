<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista Tavoli</title>
</head>
<body>
	<div>
		<h2>Nuovo tavolo</h2>
		<div>
			<div>
				<form:form action="/addTable" modelAttribute="table" method="post">
					<div>
                        <form:label path="capacity">Capacita</form:label>
                        <form:select id="capacity" path="capacity">
                            <form:option value="2">2</form:option>
                            <form:option value="3">3</form:option>
                            <form:option value="4">4</form:option>
                            <form:option value="5">5</form:option>
                            <form:option value="6">6</form:option>
                            <form:option value="7">7</form:option>
                            <form:option value="8">8</form:option>
                        </form:select>
                        <form:errors path="capacity" />
					</div>
					<div>
						<div>
							<input type="submit" value="Aggiungi tavolo">
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>