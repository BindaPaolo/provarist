<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
    <%@ page isELIgnored="false" %>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Lista Allergeni</title>
</head>
<body>
    <div>
        <h2>Modifica Allergene</h2>
        <div>
            <div>
                <form:form action="/updateAllergen/${allergen.id}" modelAttribute="allergen" method="post">
                    <div>
                        <div>
                            Id: ${allergen.id}
                        </div>
                        <div>
                            <form:label path="name">Nome</form:label>
                            <form:input type="text" id="name" path="name" />
                            <form:errors path="name" />
                        </div>
                    <div>
                        <div>
                            <input type="submit" value="Aggiorna allergene">
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
    </body>
</html>