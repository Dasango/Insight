package com.uce.insight.services;

import com.uce.insight.database_connection.DbConnection;
import com.uce.insight.modelo.TareaUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class TareaUsuarioService {

    private final Connection connection;

    public TareaUsuarioService() {
        this.connection = DbConnection.getInstance().getConnection();
    }

    // Validación de ID (tarea y usuario) positivos
    private boolean esIdValido(int id) {
        return id > 0;
    }

    // Validación de fecha (no puede ser nula ni en el futuro)
    private boolean esFechaValida(LocalDateTime fecha) {
        return fecha != null && fecha.isBefore(LocalDateTime.now().plusMinutes(1));
    }

    // Crear relación
    public boolean asignarTareaAUsuario(TareaUsuario tu) {
        if (!esIdValido(tu.getTareaId())) {
            System.out.println("El ID de tarea debe ser positivo.");
            return false;
        }
        if (!esIdValido(tu.getUsuarioId())) {
            System.out.println("El ID de usuario debe ser positivo.");
            return false;
        }
        if (!esFechaValida(tu.getAsignadoEn())) {
            System.out.println("La fecha de asignación no es válida.");
            return false;
        }

        String sql = "INSERT INTO tarea_usuario (tarea_id, usuario_id, asignado_en) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tu.getTareaId());
            stmt.setInt(2, tu.getUsuarioId());
            stmt.setTimestamp(3, Timestamp.valueOf(tu.getAsignadoEn()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener una asignación específica
    public TareaUsuario obtenerAsignacion(int tareaId, int usuarioId) {
        if (!esIdValido(tareaId) || !esIdValido(usuarioId)) {
            System.out.println("El ID de tarea o usuario no es válido.");
            return null;
        }

        String sql = "SELECT * FROM tarea_usuario WHERE tarea_id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tareaId);
            stmt.setInt(2, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TareaUsuario(
                        rs.getInt("tarea_id"),
                        rs.getInt("usuario_id"),
                        rs.getTimestamp("asignado_en").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Eliminar asignación
    public boolean eliminarAsignacion(int tareaId, int usuarioId) {
        if (!esIdValido(tareaId) || !esIdValido(usuarioId)) {
            System.out.println("El ID de tarea o usuario no es válido.");
            return false;
        }

        String sql = "DELETE FROM tarea_usuario WHERE tarea_id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tareaId);
            stmt.setInt(2, usuarioId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todas las tareas asignadas a un usuario
    public ObservableList<TareaUsuario> obtenerTareasDeUsuario(int usuarioId) {
        if (!esIdValido(usuarioId)) {
            System.out.println("El ID de usuario no es válido.");
            return FXCollections.observableArrayList();
        }

        ObservableList<TareaUsuario> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM tarea_usuario WHERE usuario_id = ? ORDER BY asignado_en DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new TareaUsuario(
                        rs.getInt("tarea_id"),
                        rs.getInt("usuario_id"),
                        rs.getTimestamp("asignado_en").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Contar asignaciones (paginación opcional)
    public int contarAsignaciones() {
        String sql = "SELECT COUNT(*) FROM tarea_usuario";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
