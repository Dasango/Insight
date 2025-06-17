package com.uce;


import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws InterruptedException {

        //Login  -- JONA
        System.out.println("Opciones:");
        System.out.println("1. Ingresar");
        System.out.println("2. Crear user");
        Thread.sleep(1000);

        while(true){
            //Opciones principales
            System.out.println("Opciones");
            System.out.println("1. Ver projectos");
            System.out.println("2. Ver bandeja de entrada"); //JONA
            System.out.println("3. Chat AI"); //todo adrian
            switch (sc.nextInt()){
                case 1:
                    //Opciones de los proyectos
                    System.out.println("Carga proyectos");
                    Thread.sleep(1000);
                    System.out.println("Opciones");
                    System.out.println("1. Ver proyectos ");
                    System.out.println("2. agregar proyecto");
                    switch (sc.nextInt()){
                        case 1:
                            //Opciones rapidas dentro de cada proyecto
                            System.out.println("Carga proyectos");
                            System.out.println("Seleccione un proyecto (selecciona un proyecto)");
                            Thread.sleep(1000);
                            System.out.println("Opciones del proyecto selecionado");
                            System.out.println("1. Asignados");
                            System.out.println("2. Tu Siguiente Actividad");
                            System.out.println("3. Opciones Avanzadas");
                            switch (sc.nextInt()){
                                case 1:
                                    //Opcion ver miembros de un proyecto
                                    System.out.println("*Cargar miembros asignados al proyecto");

                                case 2:
                                    //Opcion cargar tu siguiente actividad con su plazo
                                    System.out.println("*Carga tu siguiente actividad");
                                case 3:
                                    //Opciones avanzadados dentro de cada proyecto
                                    System.out.println("Si eres dueño:");
                                    System.out.println("1. Agregar fase");
                                    System.out.println("2. Agregar personas");
                                    System.out.println("3. Borrar personas");
                                    System.out.println("Si eres dueño o no");
                                    System.out.println("4.Ver fases");
                                    System.out.println("5.Ver usuarios");

                                    switch (sc.nextInt()){
                                        case 1:
                                            //Agregar fase
                                            System.out.println("*Agrega fase");

                                        case 2:
                                            //Agregar Persona
                                            System.out.println("*Agrega persona");
                                        case 3:
                                            //Borrar Persona
                                            System.out.println("*Borrar persona");
                                        case 4:
                                            // Ver fases
                                            System.out.println("*Cargar fases");
                                            System.out.println("Seleccione una fase");
                                            Thread.sleep(1000);
                                            System.out.println("1. Ver Actividades");
                                            System.out.println("4. Ver estado (hecha/no hecha)");


                                            switch (sc.nextInt()) {
                                                case 1:
                                                    // Ver Actividades
                                                    System.out.println("*Cargar Actividades");
                                                    System.out.println("Seleccione una actividad");
                                                    Thread.sleep(1000);
                                                    System.out.println("1. Ver persona asignada");
                                                    System.out.println("2. Ver info tarea");
                                                    System.out.println("3. Ver plazo");
                                                    System.out.println("4. Ver estado (hecha/no hecha)");

                                                    switch (sc.nextInt()) {
                                                        case 1:
                                                            // Ver persona asignada
                                                            System.out.println("*Persona asignada a la actividad:");
                                                            break;
                                                        case 2:
                                                            // Ver info tarea
                                                            System.out.println("*Información de la tarea:");
                                                            break;
                                                        case 3:
                                                            // Ver plazo
                                                            System.out.println("*Plazo de entrega: ");
                                                            break;
                                                        case 4:
                                                            // Ver estado (hecha/no hecha)
                                                            System.out.println("*Estado:");
                                                            break;
                                                        default:
                                                            System.out.println("Opción de actividad no válida.");
                                                    }
                                                    break;
                                                default:
                                                    System.out.println("Fase no válida.");
                                            }

                                        case 5:
                                            //Ver Usuarios
                                            System.out.println("Carga usuarios");
                                        case 6:
                                            //Borrar face
                                            System.out.println("borra la fase");


                                    }
                            }
                        case 2:
                            //Opcion de agregar in proyecto
                            System.out.println("Opciones");
                            System.out.println("1. Agregar proyecto");
                            Thread.sleep(1000);

                    }
                case 2:
                    System.out.println("Cargar tu asignaciones");
                    System.out.println("*Cargar tu plazos ( 1dia antes y 1 hora antes)");
                    System.out.println("*Cargar tareas atrasados");
                    System.out.println("*Cargar entrega de otros miembres de los proyectos, si soy dueño");
                case 3:
                    System.out.println("*habla con ia");
                    //Chat AI para crear proyectos, tarea para hacer al final
            }
        }

    }
}