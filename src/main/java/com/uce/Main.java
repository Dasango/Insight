package com.uce;

import com.services.FaseService;
import com.services.ProyectoService;
import com.services.ProyectoUsuarioService;

import com.modelo.Fase;
import com.modelo.Proyecto;
import com.modelo.ProyectoUsuario;
import javafx.collections.ObservableList;


import java.time.LocalDate;
import java.util.Scanner;

public class Main {


    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        ProyectoService proyectoService = new ProyectoService();
        ProyectoUsuarioService proyectoUsuarioService = new ProyectoUsuarioService();
        FaseService faseService = new FaseService();

        // Programador 2: Login
        System.out.println("Opciones:");
        System.out.println("1. Ingresar");
        System.out.println("2. Crear usuario");
        Thread.sleep(1000);

        while (true) {
            // Menú Principal
            System.out.println("Opciones");
            System.out.println("1. Módulo de Proyectos");       // Programador 1
            System.out.println("2. Módulo de Actividades");     // Programador 2
            System.out.println("3. Chat IA");                   // Adri UWu(pendiente)

            switch (sc.nextInt()) {
                case 1:

                    // Programador 1: Gestión de proyectos y fases
                    System.out.println("1. Ver proyectos");

                    //Opción 1 (cargar varios proyectos en pantalla)
                    ObservableList<Proyecto> a = proyectoService.obtenerProyectosPaginado(2, 10);
                    //Opción 2 (cargar uno solo)
                    Proyecto b = proyectoService.obtenerProyectoPorId(2);


                    System.out.println("2. Crear proyecto");


                    switch (sc.nextInt()) {
                        case 1:
                            System.out.println("*Cargar proyectos...");
                            System.out.println("Seleccione un proyecto");

                            Thread.sleep(1000);

                            // Yo mostraría una tabla con todos los proyectos existentes, luego al dar doble clic
                            // se ejecute el método "obtenerProyectoPorId con la id correspondiente o algo así."

                            System.out.println("1. Ver fases");

                            System.out.println("2. Ver miembros");

                            System.out.println("3. Agregar persona");

                            System.out.println("4. Borrar persona");


                            switch (sc.nextInt()) {
                                case 1:
                                    System.out.println("*Cargar fases");
                                    System.out.println("1. Ver detalles");
                                    ObservableList<Fase> q = proyectoService.obtenerFasesDeProyecto(1);

                                    //AL VER LAS fases se llamará al método correspondiente de cada fase:
                                    faseService.verDetallesFase(1);


                                    System.out.println("2. Crear nueva fase");
                                    Fase faseSimulada = new Fase(
                                            1, // id
                                            "Análisis", // nombre
                                            "Fase de análisis de requerimientos", // descripción
                                            LocalDate.of(2025, 6, 1), // fechaInicio
                                            LocalDate.of(2025, 6, 15), // fechaFin
                                            10 // proyectoId (suponiendo que pertenece al proyecto con ID 10)
                                    );
                                    boolean w = faseService.crearFase(faseSimulada);

                                    break;
                                case 2:
                                    System.out.println("*Miembros asignados al proyecto:");
                                    // al dar doble clic se llamaría a:
                                    ObservableList<ProyectoUsuario> r = proyectoUsuarioService.obtenerUsuariosDeProyecto(1);
                                    //otra opción
                                    ObservableList<ProyectoUsuario> g = proyectoUsuarioService.obtenerUsuariosDeProyectoPaginado(1, 2, 3);


                                    break;
                                case 3:
                                    System.out.println("*Agregar persona al proyecto");
                                    // Proyecto usuario asigna un proyecto a un usuario, recibe el id del proyecto, el id del usuario y el rol.
                                    ProyectoUsuario proyectoUsuario = new ProyectoUsuario(1, 1, "cafetero");
                                    boolean c = proyectoUsuarioService.asignarUsuarioAProyecto(proyectoUsuario);

                                    break;
                                case 4:
                                    System.out.println("*Eliminar persona del proyecto");
                                    //eliminarAsignación REcibe la id del proyecto y del usuario
                                    boolean d = proyectoUsuarioService.eliminarAsignacion(1, 3);

                                    break;
                                default:
                                    System.out.println("Opción no válida en Proyecto");
                            }
                            break;

                        case 2:
                            System.out.println("*Crear nuevo proyecto...");
                            Proyecto nuevo = new Proyecto();
                            boolean fg = proyectoService.crearProyecto(nuevo);


                            break;

                        default:
                            System.out.println("Opción no válida");
                    }
                    break;

                case 2:
                    // Programador 2: Actividades, bandeja y seguimiento
                    System.out.println("1. Ver tus actividades próximas");
                    System.out.println("2. Ver actividades por fase");
                    System.out.println("3. Bandeja de entrada");

                    switch (sc.nextInt()) {
                        case 1:
                            System.out.println("*Cargar tu siguiente actividad...");
                            break;
                        case 2:
                            System.out.println("*Selecciona proyecto y fase");
                            Thread.sleep(1000);
                            System.out.println("1. Ver actividades");
                            System.out.println("2. Crear nueva actividad");

                            switch (sc.nextInt()) {
                                case 1:
                                    System.out.println("*Actividades:");
                                    System.out.println("1. Ver persona asignada");
                                    System.out.println("2. Ver información");
                                    System.out.println("3. Ver plazo");
                                    System.out.println("4. Ver estado");
                                    break;
                                case 2:
                                    System.out.println("*Crear nueva actividad...");
                                    break;
                                default:
                                    System.out.println("Opción inválida");
                            }
                            break;
                        case 3:
                            System.out.println("*Tareas atrasadas");
                            System.out.println("*Plazos próximos (1 día, 1 hora)");
                            System.out.println("*Entregas recientes de otros miembros si eres dueño");
                            break;
                        default:
                            System.out.println("Opción no válida");
                    }
                    break;

                case 3:
                    // adri UWu Chat IA
                    System.out.println("*Habla con IA para organizar proyectos y actividades");
                    break;

                default:
                    System.out.println("Opción no reconocida");
            }
        }
    }
}
