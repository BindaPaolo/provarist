<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lista Allergeni</title>
</head>
<body>
	<div>
		<h2>Nuovo Allergene</h2>
		<div>
			<div>
				<form:form action="/addAllergen" modelAttribute="allergen" method="post">
					<div>
						<div>
							<form:label path="Name">Nome</form:label>
							<form:input type="text" id="Name" path="Name" />
							<form:errors path="Name" />
						</div>
					</div>
					<div>
						<div>
							<input type="submit" value="Aggiungi allergene">
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>