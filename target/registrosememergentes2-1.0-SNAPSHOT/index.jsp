<%-- 
    Document   : index
    Created on : 15 abr. 2024, 22:55:44
    Author     : Lenovo

1 primero creamos index jsp

3 CREACION DE VISTA 
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.emergentes.Seminario"%>
<!-- crear conexion, la conexion mediante el atributo lista -->
<%
    if (session.getAttribute("lista") == null) {
//solo la primera vez q se ejecuta la app
        ArrayList<Seminario> listaAux = new ArrayList<Seminario>(); //creamos una coleccion con el nombre listaaux usando objetos de calificacion
        session.setAttribute("lista", listaAux); //ponemos como atributo de session listaaaux y tambien creamos una coleccion vaciaa

    }
    //en caso que la lista no este vacia debemos mostrar los datos
    List<Seminario> lista = (ArrayList<Seminario>) session.getAttribute("lista");
%>
<!<!-- con lo de arriba ya tenemos la lista disponible para mostrar los datos  -->
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Listado de Inscritos</h1>
<button onclick="window.location.href='MainServlet?action=nuevo'">Nuevo</button> <!-- vamos a llamar al servlet mainservlet para q haga el proceso -->
        <!-- creacion de tabla donde albergara los registros de calificaciones -->
        <table border="1">
            <tr>
                <th>Id</th>
                <th>Fecha</th>
                <th>Nombre</th>
                <th>Apellidos</th>
                <th>Turno</th>
                <th>Seminarios</th>
                <th></th>
                <th></th>
            </tr>
            <%
                for (Seminario sem : lista) {
                String fechaFormateada = "";
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    fechaFormateada = dateFormat.format(sem.getFecha());
                } catch (Exception e) {
                    fechaFormateada = "Error en formato de fecha";
                }

            %>
            
            <tr><!<!-- incluimos los datos con el objeto cal -->
                <td><%= sem.getId()%></td>
                <td><%= fechaFormateada%></td>
                <td><%= sem.getNombre()%></td>
                <td><%= sem.getApellidos()%></td>
                <td><%= sem.getTurno()%></td>
                <td><%= sem.getSeminario()%></td>
                
                <td><a href="MainServlet?action=editar&id=<%= sem.getId()%>">Editar</a></td><!-- utilizamos el mainservlet para la edicion y eliminacion con referencia -->
                <td><a href="MainServlet?action=eliminar&id=<%= sem.getId()%>">Eliminar</a></td>
            </tr>
            <%
                }
            %>
        </table>
    </body>
</html>
