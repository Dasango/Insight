package com.uce.cosasJona;

import java.sql.*;
import java.util.Scanner;

public class UsuarioService {

    private final Scanner scanner = new Scanner(System.in);

    public void crearUsuario() {
        System.out.println("=== Crear nuevo usuario ===");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Clave: ");
        String clave = scanner.nextLine();

        String sql = "INSERT INTO Usuario (nombre, email, clave) VALUES (?, ?, ?)";

        try (Connection conn = DbConfig.getInstance().getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, clave);
            stmt.executeUpdate();

            System.out.println("✅ Usuario creado exitosamente");

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key")) {
                System.out.println("⚠️ Ya existe un usuario con ese correo.");
            } else {
                System.out.println("❌ Error al crear usuario: " + e.getMessage());
            }
        }
    }

    public void login() {
        System.out.println("=== Iniciar sesión ===");
        int intentos = 3;

        while (intentos > 0) {
            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Clave: ");
            String clave = scanner.nextLine();

            String sql = "SELECT * FROM Usuario WHERE email = ?";

            try (Connection conn = DbConfig.getInstance().getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String claveCorrecta = rs.getString("clave");
                    if (clave.equals(claveCorrecta)) {
                        System.out.println("✅ Bienvenido, " + rs.getString("nombre"));
                        return;
                    } else {
                        intentos--;
                        System.out.println("❌ Contraseña incorrecta. Intentos restantes: " + intentos);
                    }
                } else {
                    System.out.println("⚠️ Usuario no encontrado.");
                    return;
                }

            } catch (SQLException e) {
                System.out.println("❌ Error al iniciar sesión: " + e.getMessage());
                return;
            }
        }

        System.out.println("❌ Has excedido el número de intentos.");
    }
}


