package com.uce.insight.services;

import com.uce.insight.database_connection.DbConnection;
import com.uce.insight.modelo.Notificacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class NotificacionService {

    private final Connection connection;

    public NotificacionService() {
        this.connection = DbConnection.getInstance().getConnection();
    }

    // Validaciones

    // Validar ID (positivo)
    private boolean esIdValido(int id) {
        return id > 0;
    }

    // Validar mensaje (no vacío ni nulo)
    private boolean esMensajeValido(String mensaje) {
        return mensaje != null && !mensaje.trim().isEmpty();
    }

    // Validar fecha (no nula)
    private boolean esFechaValida(LocalDateTime fecha) {
        return fecha != null && fecha.isBefore(LocalDateTime.now().plusSeconds(1));  // Permite fechas actuales y pasadas
    }

    // Verificar si el usuario existe
    private boolean existeUsuario(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar la existencia del usuario: " + e.getMessage());
        }
        return false;
    }

    // Crear notificación
    public boolean crearNotificacion(Notificacion notificacion) {
        if (!esIdValido(notificacion.getUsuarioId())) {
            System.out.println("El ID de usuario no es válido.");
            return false;
        }
        if (!existeUsuario(notificacion.getUsuarioId())) {
            System.out.println("El usuario con ID " + notificacion.getUsuarioId() + " no existe.");
            return false;
        }
        if (!esMensajeValido(notificacion.getMensaje())) {
            System.out.println("El mensaje no es válido.");
            return false;
        }
        if (!esFechaValida(notificacion.getFechaCreada())) {
            System.out.println("La fecha de creación no es válida.");
            return false;
        }

        String sql = "INSERT INTO notificacion (usuario_id, mensaje, leida, fecha_creada) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notificacion.getUsuarioId());
            stmt.setString(2, notificacion.getMensaje());
            stmt.setBoolean(3, notificacion.getLeida());
            stmt.setTimestamp(4, Timestamp.valueOf(notificacion.getFechaCreada()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear la notificación: " + e.getMessage());
            return false;
        }
    }

    // Obtener notificación por ID
    public Notificacion obtenerNotificacionPorId(int id) {
        if (!esIdValido(id)) {
            System.out.println("El ID de la notificación no es válido.");
            return null;
        }

        String sql = "SELECT * FROM notificacion WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToNotificacion(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la notificación: " + e.getMessage());
        }
        return null;
    }

    // Marcar una notificación como leída
    public boolean marcarNotificacionComoLeida(int id) {
        if (!esIdValido(id)) {
            System.out.println("El ID de la notificación no es válido.");
            return false;
        }

        String sql = "UPDATE notificacion SET leida = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar la notificación como leída: " + e.getMessage());
            return false;
        }
    }

    // Eliminar notificación
    public boolean eliminarNotificacion(int id) {
        if (!esIdValido(id)) {
            System.out.println("El ID de la notificación no es válido.");
            return false;
        }

        String sql = "DELETE FROM notificacion WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la notificación: " + e.getMessage());
            return false;
        }
    }

    // Obtener todas las notificaciones de un usuario
    public ObservableList<Notificacion> obtenerNotificacionesPorUsuario(int usuarioId) {
        ObservableList<Notificacion> lista = FXCollections.observableArrayList();
        if (!esIdValido(usuarioId)) {
            System.out.println("El ID de usuario no es válido.");
            return lista;
        }
        if (!existeUsuario(usuarioId)) {
            System.out.println("El usuario con ID " + usuarioId + " no existe.");
            return lista;
        }

        String sql = "SELECT * FROM notificacion WHERE usuario_id = ? ORDER BY fecha_creada DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToNotificacion(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las notificaciones: " + e.getMessage());
        }
        return lista;
    }

    // Mapear ResultSet a objeto Notificacion
    private Notificacion mapResultSetToNotificacion(ResultSet rs) throws SQLException {
        return new Notificacion(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getString("mensaje"),
                rs.getBoolean("leida"),
                rs.getTimestamp("fecha_creada").toLocalDateTime()
        );
    }
}
