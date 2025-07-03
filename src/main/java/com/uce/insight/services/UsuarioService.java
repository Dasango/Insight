package com.uce.insight.services;

import com.uce.insight.database_connection.DbConnection;
import com.uce.insight.modelo.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.regex.Pattern;

public class UsuarioService {

    private final Connection connection;

    public UsuarioService() {
        this.connection = DbConnection.getInstance().getConnection();
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        if (!esEmailValido(email)) {
            System.out.println("El email no es válido.");
            return null;
        }

        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean esEmailValido(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    private boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    // Crear nuevo usuario en la base de datos (ahora incluye clave)
    public boolean crearUsuario(Usuario usuario) {
        if (!esNombreValido(usuario.getNombre())) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }
        if (!esEmailValido(usuario.getEmail())) {
            System.out.println("El email no es válido.");
            return false;
        }

        String sql = "INSERT INTO usuario (nombre, email, clave) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getClave());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario obtenerUsuarioPorId(int id) {
        if (id <= 0) {
            System.out.println("El ID debe ser positivo.");
            return null;
        }

        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ObservableList<Usuario> obtenerUsuariosPaginado(int offset, int limit) {
        if (offset < 0 || limit <= 0) {
            System.out.println("El offset y el límite deben ser válidos.");
            return FXCollections.observableArrayList();
        }

        ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
        String sql = "SELECT * FROM usuario ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }



    public Usuario login(String email, String clave) throws Exception {
        String sql = "SELECT * FROM Usuario WHERE email = ?";

        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String claveCorrecta = rs.getString("clave");
                if (clave.equals(claveCorrecta)) {
                    System.out.println("✅ Bienvenido, " + rs.getString("nombre"));
                    Usuario user = new Usuario(rs.getString("nombre"), email, clave);
                    user.setId(rs.getInt("id"));
                    return user;

                } else {
                    throw new RuntimeException("❌ Contraseña incorrecta.");
                }
            } else {
                throw new RuntimeException("⚠️ Usuario no encontrado.");

            }

        } catch (SQLException e) {
            System.out.println("❌ Error al iniciar sesión: " + e.getMessage());
            throw new RuntimeException("❌ Error al iniciar sesión: " + e.getMessage());
        }
    }

    public boolean actualizarUsuario(Usuario usuario) {
        if (!esNombreValido(usuario.getNombre())) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }
        if (!esEmailValido(usuario.getEmail())) {
            System.out.println("El email no es válido.");
            return false;
        }

        String sql = "UPDATE usuario SET nombre = ?, clave = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getClave());
            stmt.setString(3, usuario.getEmail()); // usamos email como identificador único
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(int id) {
        if (id <= 0) {
            System.out.println("El ID debe ser positivo.");
            return false;
        }

        String sql = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int contarUsuarios() {
        String sql = "SELECT COUNT(*) FROM usuario";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id")); // <-- Sí lo usamos
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setClave(rs.getString("clave"));
        return usuario;
    }

}
