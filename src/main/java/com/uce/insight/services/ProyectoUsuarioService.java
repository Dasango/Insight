package com.uce.insight.services;

import com.uce.insight.database_connection.DbConnection;
import com.uce.insight.modelo.ProyectoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProyectoUsuarioService {

    private static final Logger LOGGER = Logger.getLogger(ProyectoUsuarioService.class.getName());
    private final Connection connection;

    public ProyectoUsuarioService() {
        this.connection = DbConnection.getInstance().getConnection();
        if (this.connection == null) {
            throw new IllegalStateException("No se pudo establecer la conexión a la base de datos.");
        }
    }

    public boolean asignarUsuarioAProyecto(ProyectoUsuario pu) {
        if (!existeProyecto(pu.getProyectoId())) {
            LOGGER.warning("El proyecto con ID " + pu.getProyectoId() + " no existe.");
            return false;
        }

        if (!existeUsuario(pu.getUsuarioId())) {
            LOGGER.warning("El usuario con ID " + pu.getUsuarioId() + " no existe.");
            return false;
        }

        String sql = "INSERT INTO proyectousuario (proyecto_id, usuario_id, rol) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pu.getProyectoId());
            stmt.setInt(2, pu.getUsuarioId());
            stmt.setString(3, pu.getRol());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al asignar usuario a proyecto", e);
            return false;
        }
    }

    public ProyectoUsuario obtenerAsignacion(int proyectoId, int usuarioId) {
        String sql = "SELECT * FROM proyectousuario WHERE proyecto_id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            stmt.setInt(2, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProyectoUsuario(
                        rs.getInt("proyecto_id"),
                        rs.getInt("usuario_id"),
                        rs.getString("rol")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener la asignación", e);
        }
        return null;
    }

    public boolean actualizarRol(ProyectoUsuario pu) {
        if (!existeProyecto(pu.getProyectoId()) || !existeUsuario(pu.getUsuarioId())) {
            LOGGER.warning("No se puede actualizar el rol: proyecto o usuario no existente.");
            return false;
        }

        String sql = "UPDATE proyectousuario SET rol = ? WHERE proyecto_id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pu.getRol());
            stmt.setInt(2, pu.getProyectoId());
            stmt.setInt(3, pu.getUsuarioId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar el rol del usuario en el proyecto", e);
            return false;
        }
    }

    public boolean eliminarAsignacion(int proyectoId, int usuarioId) {
        if (!existeAsignacion(proyectoId, usuarioId)) {
            LOGGER.warning("No se encontró asignación para eliminar (proyectoId=" + proyectoId + ", usuarioId=" + usuarioId + ").");
            return false;
        }

        String sql = "DELETE FROM proyectousuario WHERE proyecto_id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            stmt.setInt(2, usuarioId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar asignación de usuario al proyecto", e);
            return false;
        }
    }

    public ObservableList<ProyectoUsuario> obtenerUsuariosDeProyecto(int proyectoId) {
        ObservableList<ProyectoUsuario> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM proyectousuario WHERE proyecto_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new ProyectoUsuario(
                        rs.getInt("proyecto_id"),
                        rs.getInt("usuario_id"),
                        rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuarios del proyecto", e);
        }
        return lista;
    }

    public ObservableList<ProyectoUsuario> obtenerUsuariosDeProyectoPaginado(int proyectoId, int limit, int offset) {
        ObservableList<ProyectoUsuario> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM proyectousuario WHERE proyecto_id = ? ORDER BY usuario_id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new ProyectoUsuario(
                        rs.getInt("proyecto_id"),
                        rs.getInt("usuario_id"),
                        rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuarios del proyecto paginados", e);
        }
        return lista;
    }

    public ObservableList<ProyectoUsuario> obtenerProyectosDeUsuarioPaginado(int usuarioId, int limit, int offset) {
        ObservableList<ProyectoUsuario> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM proyectousuario WHERE usuario_id = ? ORDER BY proyecto_id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new ProyectoUsuario(
                        rs.getInt("proyecto_id"),
                        rs.getInt("usuario_id"),
                        rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener proyectos del usuario paginados", e);
        }
        return lista;
    }

    public ObservableList<ProyectoUsuario> obtenerProyectosDeUsuario(int usuarioId) {
        ObservableList<ProyectoUsuario> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM proyectousuario WHERE usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new ProyectoUsuario(
                        rs.getInt("proyecto_id"),
                        rs.getInt("usuario_id"),
                        rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener proyectos del usuario", e);
        }
        return lista;
    }

    private boolean existeProyecto(int proyectoId) {
        String sql = "SELECT 1 FROM proyecto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar existencia de proyecto", e);
            return false;
        }
    }

    private boolean existeUsuario(int usuarioId) {
        String sql = "SELECT 1 FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar existencia de usuario", e);
            return false;
        }
    }

    private boolean existeAsignacion(int proyectoId, int usuarioId) {
        String sql = "SELECT 1 FROM proyectousuario WHERE proyecto_id = ? AND usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            stmt.setInt(2, usuarioId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar existencia de asignación", e);
            return false;
        }
    }
}
