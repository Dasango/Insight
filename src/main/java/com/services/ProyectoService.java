package com.services;

import com.database_connection.DbConnection;
import com.modelo.Fase;
import com.modelo.Proyecto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProyectoService {

    private static final Logger LOGGER = Logger.getLogger(ProyectoService.class.getName());
    private final Connection connection;

    public ProyectoService() {
        this.connection = DbConnection.getInstance().getConnection();
        if (this.connection == null) {
            throw new IllegalStateException("No se pudo establecer conexión con la base de datos.");
        }
    }

    // Verificar si el proyecto existe por ID
    private boolean existeProyecto(int id) {
        String sql = "SELECT COUNT(*) FROM proyecto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar la existencia del proyecto", e);
        }
        return false;
    }

    public boolean crearProyecto(Proyecto proyecto) {
        String sql = "INSERT INTO proyecto (nombre, descripcion, creado_por) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, proyecto.getNombre());
            stmt.setString(2, proyecto.getDescripcion());
            stmt.setInt(3, proyecto.getCreadoPor());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear el proyecto", e);
            return false;
        }
    }

    public Proyecto obtenerProyectoPorId(int id) {
        if (id <= 0) {
            LOGGER.warning("ID inválido para buscar proyecto: " + id);
            return null;
        }

        String sql = "SELECT * FROM proyecto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProyecto(rs);
                } else {
                    LOGGER.warning("No se encontró un proyecto con ID: " + id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener el proyecto por ID", e);
        }
        return null;
    }

    public ObservableList<Proyecto> obtenerProyectosPaginado(int offset, int limit) {
        ObservableList<Proyecto> proyectos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM proyecto ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    proyectos.add(mapResultSetToProyecto(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener proyectos paginados", e);
        }
        return proyectos;
    }

    public boolean actualizarProyecto(Proyecto proyecto) {
        if (proyecto == null || proyecto.getId() <= 0) {
            LOGGER.warning("Proyecto nulo o con ID inválido para actualizar.");
            return false;
        }

        if (!existeProyecto(proyecto.getId())) {
            LOGGER.warning("No se encontró proyecto para actualizar con ID: " + proyecto.getId());
            return false;
        }

        String sql = "UPDATE proyecto SET nombre = ?, descripcion = ?, creado_por = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, proyecto.getNombre());
            stmt.setString(2, proyecto.getDescripcion());
            stmt.setInt(3, proyecto.getCreadoPor());
            stmt.setInt(4, proyecto.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar el proyecto", e);
            return false;
        }
    }

    public boolean eliminarProyecto(int id) {
        if (id <= 0) {
            LOGGER.warning("ID inválido para eliminar proyecto: " + id);
            return false;
        }

        if (!existeProyecto(id)) {
            LOGGER.warning("No se encontró proyecto para eliminar con ID: " + id);
            return false;
        }

        String sql = "DELETE FROM proyecto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar el proyecto", e);
            return false;
        }
    }

    public int contarProyectos() {
        String sql = "SELECT COUNT(*) FROM proyecto";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al contar los proyectos", e);
        }
        return 0;
    }

    private Proyecto mapResultSetToProyecto(ResultSet rs) throws SQLException {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(rs.getInt("id"));
        proyecto.setNombre(rs.getString("nombre"));
        proyecto.setDescripcion(rs.getString("descripcion"));

        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) {
            proyecto.setFechaCreacion(ts.toLocalDateTime());
        }

        proyecto.setCreadoPor(rs.getInt("creado_por"));
        return proyecto;
    }

    public ObservableList<Fase> obtenerFasesDeProyecto(int proyectoId) {
        ObservableList<Fase> fases = FXCollections.observableArrayList();

        if (proyectoId <= 0) {
            LOGGER.warning("ID de proyecto inválido al buscar fases: " + proyectoId);
            return fases;
        }

        String sql = "SELECT * FROM fase WHERE proyecto_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fase fase = new Fase(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha_inicio").toLocalDate(),
                            rs.getDate("fecha_fin").toLocalDate(),
                            rs.getInt("proyecto_id")
                    );
                    fases.add(fase);
                }
                if (fases.isEmpty()) {
                    LOGGER.info("No se encontraron fases para el proyecto con ID: " + proyectoId);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener las fases del proyecto", e);
        }

        return fases;
    }
}
