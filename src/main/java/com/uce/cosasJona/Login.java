package com.uce.cosasJona;

import java.sql.*;
import java.util.Scanner;

public class Login {

    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {
        try {
            while (true) {
                System.out.println("Opciones:");
                System.out.println("1. Ingresar");
                System.out.println("2. Crear usuario");
                System.out.print("Selecciona una opción: ");
                String opcion = sc.nextLine();

                switch (opcion) {
                    case "1":
                        login();
                        return; // salir después de intento de login
                    case "2":
                        crearUsuario();
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }

                Thread.sleep(1000); // pausa visual
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            DbConfig.getInstance().close();
            sc.close();
        }
    }

    private void login() {
        int intentos = 3;

        while (intentos > 0) {
            System.out.print("Nombre de usuario: ");
            String nombre = sc.nextLine();

            System.out.print("Contraseña: ");
            String clave = sc.nextLine();

            if (verificarCredenciales(nombre, clave)) {
                System.out.println("¡Ingreso exitoso!");
                return;
            } else {
                intentos--;
                System.out.println("Credenciales incorrectas. Intentos restantes: " + intentos);
            }
        }

        System.out.println("Demasiados intentos. El programa se cerrará.");
    }

    private boolean verificarCredenciales(String nombre, String clave) {
        String sql = "SELECT * FROM Usuario WHERE nombre = ? AND clave = ?";

        try (Connection conn = DbConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, clave);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error al verificar credenciales: " + e.getMessage());
            return false;
        }
    }

    private void crearUsuario() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Contraseña: ");
        String clave = sc.nextLine();

        if (nombre.isEmpty() || email.isEmpty() || clave.isEmpty()) {
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        String sql = "INSERT INTO Usuario (nombre, email, clave) VALUES (?, ?, ?)";

        try (Connection conn = DbConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, clave);

            stmt.executeUpdate();
            System.out.println("Usuario creado exitosamente.");

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key") || e.getMessage().contains("duplicate")) {
                System.out.println("El email ya está registrado.");
            } else {
                System.out.println("Error al crear usuario: " + e.getMessage());
            }
        }
    }
}

