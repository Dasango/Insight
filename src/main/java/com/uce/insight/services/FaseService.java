package com.uce.insight.services;

import com.uce.insight.database_connection.DbConnection;
import com.uce.insight.modelo.Fase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FaseService {

    private final Connection connection;

    public FaseService() {
        this.connection = DbConnection.getInstance().getConnection();
    }

    // Validar fase
    private boolean esFaseValida(Fase fase) {
        if (fase == null) {
            System.out.println("Error: La fase es nula.");
            return false;
        }
        if (fase.getNombre() == null || fase.getNombre().isBlank()) {
            System.out.println("Error: El nombre no puede estar vacío.");
            return false;
        }
        if (fase.getDescripcion() == null || fase.getDescripcion().isBlank()) {
            System.out.println("Error: La descripción no puede estar vacía.");
            return false;
        }
        if (fase.getFechaInicio() == null || fase.getFechaFin() == null) {
            System.out.println("Error: Las fechas no pueden ser nulas.");
            return false;
        }
        if (fase.getFechaInicio().isAfter(fase.getFechaFin())) {
            System.out.println("Error: La fecha de inicio no puede ser posterior a la fecha de fin.");
            return false;
        }
        if (!existeProyecto(fase.getProyectoId())) {
            System.out.println("Error: No existe un proyecto con ID " + fase.getProyectoId());
            return false;
        }
        return true;
    }

    private boolean existeProyecto(int proyectoId) {
        String sql = "SELECT 1 FROM proyecto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia del proyecto: " + e.getMessage());
            return false;
        }
    }

    public List<Fase> obtenerFasesPorProyecto(int proyectoId) {
        List<Fase> fases = new ArrayList<>();

        if (proyectoId <= 0) {
            System.out.println("Error: ID de proyecto inválido");
            return fases;
        }

        String sql = "SELECT * FROM fase WHERE proyecto_id = ? ORDER BY fecha_inicio";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fases.add(mapResultSetToFase(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener fases por proyecto: " + e.getMessage());
        }
        return fases;
    }

    public boolean crearFase(Fase fase) {
        if (!esFaseValida(fase)) return false;

        String sql = "INSERT INTO fase (nombre, descripcion, fecha_inicio, fecha_fin, proyecto_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fase.getNombre());
            stmt.setString(2, fase.getDescripcion());
            stmt.setDate(3, Date.valueOf(fase.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(fase.getFechaFin()));
            stmt.setInt(5, fase.getProyectoId());

            int filas = stmt.executeUpdate();
            if (filas == 0) {
                System.out.println("No se pudo crear la fase.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al crear fase: " + e.getMessage());
            return false;
        }
    }

    public Fase obtenerFasePorId(int id) {
        if (id <= 0) {
            System.out.println("Error: ID inválido.");
            return null;
        }

        String sql = "SELECT * FROM fase WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFase(rs);
                } else {
                    System.out.println("Advertencia: No se encontró una fase con ID " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar fase por ID: " + e.getMessage());
        }
        return null;
    }

    public void verDetallesFase(int id) {
        if (id <= 0) {
            System.out.println("Error: ID inválido.");
            return;
        }

        try {
            Fase fase = obtenerFasePorId(id);
            if (fase != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                System.out.println("Detalles de la Fase:");
                System.out.println("ID: " + fase.getId());
                System.out.println("Nombre: " + fase.getNombre());
                System.out.println("Descripción: " + fase.getDescripcion());
                System.out.println("Fecha de inicio: " + fase.getFechaInicio().format(formatter));
                System.out.println("Fecha de fin: " + fase.getFechaFin().format(formatter));
                System.out.println("Proyecto ID: " + fase.getProyectoId());
            }
        } catch (Exception e) {
            System.out.println("Error inesperado al mostrar detalles de fase: " + e.getMessage());
        }
    }

    public ObservableList<Fase> obtenerFasesPaginado(int offset, int limit) {
        ObservableList<Fase> fases = FXCollections.observableArrayList();

        if (offset < 0 || limit <= 0) {
            System.out.println("Error: Parámetros de paginación inválidos.");
            return fases;
        }

        String sql = "SELECT * FROM fase ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fases.add(mapResultSetToFase(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener fases paginadas: " + e.getMessage());
        }

        return fases;
    }

    public ObservableList<Fase> obtenerFasesPorProyectoPaginado(int proyectoId, int offset, int limit) {
        ObservableList<Fase> fases = FXCollections.observableArrayList();

        if (proyectoId <= 0 || offset < 0 || limit <= 0) {
            System.out.println("Error: Parámetros inválidos para obtener fases por proyecto.");
            return fases;
        }

        String sql = "SELECT * FROM fase WHERE proyecto_id = ? ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fases.add(mapResultSetToFase(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener fases por proyecto: " + e.getMessage());
        }

        return fases;
    }

    public int contarFasesPorProyecto(int proyectoId) {
        if (proyectoId <= 0) {
            System.out.println("Error: Proyecto ID inválido.");
            return 0;
        }

        String sql = "SELECT COUNT(*) FROM fase WHERE proyecto_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error al contar fases: " + e.getMessage());
        }

        return 0;
    }

    public boolean actualizarFase(Fase fase) {
        if (fase.getId() <= 0) {
            System.out.println("Error: ID de fase inválido.");
            return false;
        }

        if (!esFaseValida(fase)) return false;

        String sql = "UPDATE fase SET nombre = ?, descripcion = ?, fecha_inicio = ?, fecha_fin = ?, proyecto_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fase.getNombre());
            stmt.setString(2, fase.getDescripcion());
            stmt.setDate(3, Date.valueOf(fase.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(fase.getFechaFin()));
            stmt.setInt(5, fase.getProyectoId());
            stmt.setInt(6, fase.getId());

            int filas = stmt.executeUpdate();
            if (filas == 0) {
                System.out.println("No se actualizó ninguna fase. Verifica el ID.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar fase: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarFase(int id) {
        if (id <= 0) {
            System.out.println("Error: ID inválido para eliminación.");
            return false;
        }

        String sql = "DELETE FROM fase WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                System.out.println("No se eliminó ninguna fase. Verifica el ID.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar fase: " + e.getMessage());
            return false;
        }
    }

    public int contarFases() {
        String sql = "SELECT COUNT(*) FROM fase";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error al contar fases: " + e.getMessage());
        }
        return 0;
    }

    private Fase mapResultSetToFase(ResultSet rs) throws SQLException {
        return new Fase(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDate("fecha_inicio").toLocalDate(),
                rs.getDate("fecha_fin").toLocalDate(),
                rs.getInt("proyecto_id")
        );
    }
}
