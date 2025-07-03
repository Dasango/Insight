package com.uce.insight.services;

import com.uce.insight.database_connection.DbConnection;
import com.uce.insight.modelo.EstadoTarea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class EstadoTareaService {

    private final Connection connection;

    public EstadoTareaService() {
        this.connection = DbConnection.getInstance().getConnection();
    }

    // Validar ID (positivo)
    private boolean esIdValido(int id) {
        return id > 0;
    }

    // Validar nombre (no vacío ni nulo)
    private boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    // Verificar si el estado de tarea existe por ID
    private boolean existeEstado(int id) {
        String sql = "SELECT COUNT(*) FROM estado_tarea WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar la existencia del estado: " + e.getMessage());
        }
        return false;
    }

    // Crear un nuevo estado de tarea
    public boolean crearEstado(EstadoTarea estado) {
        if (!esNombreValido(estado.getNombre())) {
            System.out.println("El nombre del estado de tarea no es válido.");
            return false;
        }

        String sql = "INSERT INTO estado_tarea (nombre) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, estado.getNombre());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear el estado de tarea: " + e.getMessage());
            return false;
        }
    }

    // Leer un estado por ID
    public EstadoTarea obtenerEstadoPorId(int id) {
        if (!esIdValido(id)) {
            System.out.println("El ID del estado de tarea no es válido.");
            return null;
        }

        String sql = "SELECT * FROM estado_tarea WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new EstadoTarea(rs.getInt("id"), rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el estado de tarea: " + e.getMessage());
        }
        return null;
    }

    // Leer todos los estados (con paginación)
    public ObservableList<EstadoTarea> obtenerEstadosPaginado(int offset, int limit) {
        ObservableList<EstadoTarea> estados = FXCollections.observableArrayList();
        String sql = "SELECT * FROM estado_tarea ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                estados.add(new EstadoTarea(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los estados de tarea: " + e.getMessage());
        }
        return estados;
    }

    // Actualizar nombre del estado
    public boolean actualizarEstado(EstadoTarea estado) {
        if (!esIdValido(estado.getId())) {
            System.out.println("El ID del estado de tarea no es válido.");
            return false;
        }
        if (!existeEstado(estado.getId())) {
            System.out.println("El estado de tarea con ID " + estado.getId() + " no existe.");
            return false;
        }
        if (!esNombreValido(estado.getNombre())) {
            System.out.println("El nombre del estado de tarea no es válido.");
            return false;
        }

        String sql = "UPDATE estado_tarea SET nombre = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, estado.getNombre());
            stmt.setInt(2, estado.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado de tarea: " + e.getMessage());
            return false;
        }
    }

    // Eliminar estado por ID
    public boolean eliminarEstado(int id) {
        if (!esIdValido(id)) {
            System.out.println("El ID del estado de tarea no es válido.");
            return false;
        }
        if (!existeEstado(id)) {
            System.out.println("El estado de tarea con ID " + id + " no existe.");
            return false;
        }

        String sql = "DELETE FROM estado_tarea WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el estado de tarea: " + e.getMessage());
            return false;
        }
    }

    // Contar todos los estados (para paginación)
    public int contarEstados() {
        String sql = "SELECT COUNT(*) FROM estado_tarea";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error al contar los estados de tarea: " + e.getMessage());
        }
        return 0;
    }
}
