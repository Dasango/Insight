package com.uce.cosasJona;

import java.util.Scanner;

public class MainJona {

    public static void main(String[] args) throws InterruptedException {
        UsuarioService servicio = new UsuarioService();
        Scanner scanner = new Scanner(System.in);
        Usuario u = new Usuario("","","");
        while (true) {
            System.out.println("\nOpciones:");
            System.out.println("1. Ingresar");
            System.out.println("2. Crear user");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    servicio.login("","");
                    break;
                case "2":
                    servicio.crearUsuario(u);
                    break;
                case "0":
                    System.out.println("👋 Saliendo...");
                    DbConfig.getInstance().close();
                    return;
                default:
                    System.out.println("❌ Opción inválida");
            }

            Thread.sleep(1000);
        }
    }
}


