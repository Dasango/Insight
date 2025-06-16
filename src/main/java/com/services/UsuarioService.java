package com.data_access_object;

import com.modelo.Usuario;
import com.database_connection.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.regex.Pattern;

public class UsuarioService {

    // Conexión obtenida del singleton DbConnection
    private final Connection connection;

    public UsuarioService() {
        this.connection = DbConnection.getInstance().getConnection();
    }

    // Validación de correo electrónico
    private boolean esEmailValido(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    // Validación de nombre (no vacío)
    private boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    // Crear nuevo usuario en la base de datos
    public boolean crearUsuario(Usuario usuario) {
        if (!esNombreValido(usuario.getNombre())) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }
        if (!esEmailValido(usuario.getEmail())) {
            System.out.println("El email no es válido.");
            return false;
        }

        String sql = "INSERT INTO usuario (nombre, email) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener usuario por ID
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

    // Obtener lista de usuarios paginada
    public ObservableList<Usuario> obtenerUsuariosPaginado(int offset, int limit) {
        if (offset < 0 || limit <= 0) {
            System.out.println("El offset y el límite deben ser valores válidos.");
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

    // Actualizar datos de un usuario existente
    public boolean actualizarUsuario(Usuario usuario) {
        if (!esNombreValido(usuario.getNombre())) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }
        if (!esEmailValido(usuario.getEmail())) {
            System.out.println("El email no es válido.");
            return false;
        }

        String sql = "UPDATE usuario SET nombre = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar un usuario por su ID
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

    // Contar total de usuarios
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

    // Mapear el resultado de la base de datos a un objeto Usuario
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
        return usuario;
    }
}
