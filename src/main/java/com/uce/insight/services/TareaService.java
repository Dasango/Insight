package com.uce.insight.services;

import com.uce.insight.database_connection.DbConnection;
import com.uce.insight.modelo.Tarea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class TareaService {

    private final Connection connection;

    public TareaService() {
        this.connection = DbConnection.getInstance().getConnection();
    }

    // Validación de ID (positivo)
    private boolean esIdValido(int id) {
        return id > 0;
    }

    // Validación de título (no vacío ni nulo)
    private boolean esTituloValido(String titulo) {
        return titulo != null && !titulo.trim().isEmpty();
    }

    // Validación de descripción (no nula)
    private boolean esDescripcionValida(String descripcion) {
        return descripcion != null && !descripcion.trim().isEmpty();
    }

    // Validación de fecha (no nula y no futura)
    private boolean esFechaValida(LocalDate fecha) {
        return fecha != null && fecha.isAfter(LocalDate.now().minusDays(1));
    }

    // Crear tarea
    public boolean crearTarea(Tarea tarea) {
        if (!esTituloValido(tarea.getTitulo())) {
            System.out.println("El título de la tarea no es válido.");
            return false;
        }
        if (!esDescripcionValida(tarea.getDescripcion())) {
            System.out.println("La descripción de la tarea no es válida.");
            return false;
        }
        if (!esFechaValida(tarea.getFechaEntrega())) {
            System.out.println("La fecha de entrega no es válida.");
            return false;
        }
        if (!esIdValido(tarea.getFaseId())) {
            System.out.println("El ID de fase no es válido.");
            return false;
        }
        if (tarea.getEstadoId() != null && tarea.getEstadoId() <= 0) {
            System.out.println("El ID de estado no es válido.");
            return false;
        }

        String sql = "INSERT INTO tarea (titulo, descripcion, fecha_entrega, fase_id, estado_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tarea.getTitulo());
            stmt.setString(2, tarea.getDescripcion());
            stmt.setDate(3, tarea.getFechaEntrega() != null ? Date.valueOf(tarea.getFechaEntrega()) : null);
            stmt.setInt(4, tarea.getFaseId());
            if (tarea.getEstadoId() != null && tarea.getEstadoId() > 0) {
                stmt.setInt(5, tarea.getEstadoId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Leer tarea por ID
    public Tarea obtenerTareaPorId(int id) {
        if (!esIdValido(id)) {
            System.out.println("El ID de tarea no es válido.");
            return null;
        }

        String sql = "SELECT * FROM tarea WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTarea(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Leer todas las tareas (paginado)
    public ObservableList<Tarea> obtenerTareasPaginado(int offset, int limit) {
        ObservableList<Tarea> tareas = FXCollections.observableArrayList();
        String sql = "SELECT * FROM tarea ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tareas.add(mapResultSetToTarea(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }

    // Actualizar tarea
    public boolean actualizarTarea(Tarea tarea) {
        if (!esIdValido(tarea.getId())) {
            System.out.println("El ID de tarea no es válido.");
            return false;
        }
        if (!esTituloValido(tarea.getTitulo())) {
            System.out.println("El título de la tarea no es válido.");
            return false;
        }
        if (!esDescripcionValida(tarea.getDescripcion())) {
            System.out.println("La descripción de la tarea no es válida.");
            return false;
        }
        if (!esFechaValida(tarea.getFechaEntrega())) {
            System.out.println("La fecha de entrega no es válida.");
            return false;
        }
        if (!esIdValido(tarea.getFaseId())) {
            System.out.println("El ID de fase no es válido.");
            return false;
        }
        if (tarea.getEstadoId() != null && tarea.getEstadoId() <= 0) {
            System.out.println("El ID de estado no es válido.");
            return false;
        }

        String sql = "UPDATE tarea SET titulo = ?, descripcion = ?, fecha_entrega = ?, fase_id = ?, estado_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tarea.getTitulo());
            stmt.setString(2, tarea.getDescripcion());
            stmt.setDate(3, tarea.getFechaEntrega() != null ? Date.valueOf(tarea.getFechaEntrega()) : null);
            stmt.setInt(4, tarea.getFaseId());
            if (tarea.getEstadoId() != null && tarea.getEstadoId() > 0) {
                stmt.setInt(5, tarea.getEstadoId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, tarea.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar tarea
    public boolean eliminarTarea(int id) {
        if (!esIdValido(id)) {
            System.out.println("El ID de tarea no es válido.");
            return false;
        }

        String sql = "DELETE FROM tarea WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Contar tareas (útil para paginación)
    public int contarTareas() {
        String sql = "SELECT COUNT(*) FROM tarea";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Mapear ResultSet a Tarea
    private Tarea mapResultSetToTarea(ResultSet rs) throws SQLException {
        LocalDate fechaEntrega = rs.getDate("fecha_entrega") != null
                ? rs.getDate("fecha_entrega").toLocalDate()
                : null;
        int estadoId = rs.getInt("estado_id");
        return new Tarea(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                fechaEntrega,
                rs.getInt("fase_id"),
                rs.wasNull() ? null : estadoId
        );
    }
}
