/*
SERVLET
solo dejamos doget(atiende las peticiones desde url o enlace) y dopost(atiende peticiones desde formulario)
 */
package com.emergentes;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "MainServlet", urlPatterns = {"/MainServlet"})
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        //si tiene accion tiene un valor lo almacenaremos dentro de action y si no tiene ningun valor ponemos el parametro view
        String action = request.getParameter("action") != null ? request.getParameter("action") : "view"; //recuperamos el valor de la variable action que esta en index.jsp
        //con el action hacemos la separacion de las acciones
        switch (action) {
            case "view"://por defecto lo unicio que hara es decolver el control a la vista index
                //la primera vez q sse llama a main servlet se ba a index
                response.sendRedirect("index.jsp");
                break;
            case "nuevo"://cuando hagamos clic en nuevo osea una peticion
                //proporcionaos un formulario
                //creamos c objeto que se recupera en edit.jsp
                Seminario s = new Seminario();//objeto calificacionlo mandaremos a otro jsp donde procese un formulario
                request.setAttribute("seminario", s);//mandamos el atributo calificacion
                request.getRequestDispatcher("edit.jsp").forward(request, response);//transferimos en control
                break;
            case "editar"://cuando haga clic en editar
                int idEditar = Integer.parseInt(request.getParameter("id"));

                HttpSession sesion = request.getSession();
                List<Seminario> lista = (ArrayList<Seminario>) sesion.getAttribute("lista");
                Seminario editSem = new Seminario();
                for (Seminario item : lista) {
                    if (item.getId() == idEditar) {
                        editSem = item;
                        break;
                    }
                }

                request.setAttribute("seminario", editSem);
                request.getRequestDispatcher("edit.jsp").forward(request, response);
                break;
            case "eliminar"://cuando haga clic en eliminar
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                HttpSession sesion1 = request.getSession();
                /*List<Seminario> lista1 = (ArrayList<Seminario>) sesion1.getAttribute("lista");

                for (Seminario item : lista1) {
                    if (item.getId() == idEliminar) {
                        lista1.remove(item);
                    }
                }
                response.sendRedirect("index.jsp");
                break;*/
                List<Seminario> lista1 = (ArrayList<Seminario>) sesion1.getAttribute("lista");

                Iterator<Seminario> iterator = lista1.iterator();
                while (iterator.hasNext()) {
                    Seminario item = iterator.next();
                    if (item.getId() == idEliminar) {
                        iterator.remove();
                        break;
                    }
                }
                response.sendRedirect("index.jsp");
                break;

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //en aqui recuperamos los datos enviados del formulario
        //recuperamos los 4 campos
        int id = Integer.parseInt((request.getParameter("id")));
        String fechaString = request.getParameter("fecha");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String turno = request.getParameter("turno");
        String[] seminariosSeleccionados = request.getParameterValues("seminario");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = dateFormat.parse(fechaString);
            // Formatear la fecha en el formato deseado
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = dateFormat2.format(fecha);
            // Ahora pasamos la fecha formateada al JSP
            request.setAttribute("fechaFormateada", fechaFormateada);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String seminariosConcatenados = "";
        if (seminariosSeleccionados != null && seminariosSeleccionados.length > 0) {
            seminariosConcatenados = String.join(", ", seminariosSeleccionados);
        }
        //NUEVO REGISTRO--------------------------------------
        //ya tenemos el objeto listo ahora lo agregamos a una lista
        //sacamos la lista utilizando la clase httpsession
        HttpSession ses = request.getSession();
        List<Seminario> lista = (ArrayList<Seminario>) ses.getAttribute("lista");

        if (id == 0) {
            //nuevo registro
            Seminario s = new Seminario();
            int idNuevo = obtenerNuevoId(lista);
            s.setId(idNuevo);
            s.setFecha(fecha);
            s.setNombre(nombre);
            s.setApellidos(apellidos);
            s.setTurno(turno);
            s.setSeminario(seminariosConcatenados);

            //adicionamos elemento
            lista.add(s);
        } else {
            for (Seminario item : lista) {
                if (item.getId() == id) {
                    item.setFecha(fecha);
                    item.setNombre(nombre);
                    item.setApellidos(apellidos);
                    item.setTurno(turno);
                    item.setSeminario(seminariosConcatenados);

                    break;
                }
            }
        }

        //almacenar los datos en un objeto de tipo calificacion
        response.sendRedirect("index.jsp");

    }

    private int obtenerNuevoId(List<Seminario> lista) {
        int nuevoId = 1;

        for (Seminario item : lista) {
            if (item.getId() >= nuevoId) {
                nuevoId = item.getId() + 1;
            }
        }
        return nuevoId;
    }
}
