package com.uce.cosasJona;

import java.sql.*;
import java.util.Scanner;

public class UsuarioService {

    public boolean crearUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nombre, email, clave) VALUES (?, ?, ?)";

        try (Connection conn = DbConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.nombre());
            stmt.setString(2, usuario.email());
            stmt.setString(3, usuario.clave());
            stmt.executeUpdate();

            System.out.println("✅ Usuario creado exitosamente");
            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key")) {
                System.out.println("⚠️ Ya existe un usuario con ese correo.");
            } else {
                System.out.println("❌ Error al crear usuario: " + e.getMessage());
            }
            return false;
        }
    }

    public Usuario login(String email, String clave) {
        String sql = "SELECT * FROM Usuario WHERE email = ?";

        try (Connection conn = DbConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String claveCorrecta = rs.getString("clave");
                if (clave.equals(claveCorrecta)) {
                    System.out.println("✅ Bienvenido, " + rs.getString("nombre"));
                    return new Usuario(rs.getString("nombre"), email, clave);
                } else {
                    System.out.println("❌ Contraseña incorrecta.");
                    return null;
                }
            } else {
                System.out.println("⚠️ Usuario no encontrado.");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al iniciar sesión: " + e.getMessage());
            return null;
        }
    }
}


