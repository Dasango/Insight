package com.uce.insight.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Proyecto {

    // Atributos JavaFX
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private StringProperty descripcion = new SimpleStringProperty();
    private ObjectProperty<LocalDateTime> fechaCreacion = new SimpleObjectProperty<>();
    private IntegerProperty creadoPor = new SimpleIntegerProperty();

    // Relaciones
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Fase> fases = new ArrayList<>();

    public Proyecto() {
        this(0, "", "", LocalDateTime.now(), 0);
    }

    public Proyecto(int id, String nombre, String descripcion, LocalDateTime fechaCreacion, int creadoPor) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.descripcion.set(descripcion);
        this.fechaCreacion.set(fechaCreacion != null ? fechaCreacion : LocalDateTime.now());
        this.creadoPor.set(creadoPor);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }

    public LocalDateTime getFechaCreacion() { return fechaCreacion.get(); }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion.set(fechaCreacion); }

    public int getCreadoPor() { return creadoPor.get(); }
    public void setCreadoPor(int creadoPor) { this.creadoPor.set(creadoPor); }
/*
    // ✅ Métod actualizado para retornar un ObservableList (ideal para TableView)
    public ObservableList<Fase> verFases() {
        if (fases.isEmpty()) {
            System.out.println("No hay fases asociadas al proyecto.");
        } else {
            System.out.println("Fases del Proyecto " + getNombre() + ":");
            for (Fase fase : fases) {
                System.out.println(fase.getNombre() + " - " + fase.getDescripcion());
            }
        }
        return FXCollections.observableArrayList(fases);
    }

    // ✅ Nuevo método con validación de existencia y solo ID
    public void agregarUsuario(int usuarioId) {
        UsuarioService usuarioDao = new UsuarioService();
        Usuario usuario = usuarioDao.obtenerUsuarioPorId(usuarioId);
        if (usuario == null) {
            System.out.println("El usuario con ID " + usuarioId + " no existe.");
            return;
        }

        if (!usuarios.contains(usuario)) {
            usuarios.add(usuario);
            ProyectoUsuarioService dao = new ProyectoUsuarioService();
            ProyectoUsuario proyectoUsuario = new ProyectoUsuario(this.getId(), usuarioId, "colaborador");
            boolean asignado = dao.asignarUsuarioAProyecto(proyectoUsuario);
            if (asignado) {
                System.out.println("Usuario " + usuario.getNombre() + " agregado al proyecto.");
            } else {
                System.out.println("Error al agregar usuario al proyecto.");
            }
        } else {
            System.out.println("El usuario ya está asignado al proyecto.");
        }
    }

    // ✅ Nuevo método con validación de existencia y solo ID
    public void eliminarUsuario(int usuarioId) {
        UsuarioService usuarioDao = new UsuarioService();
        Usuario usuario = usuarioDao.obtenerUsuarioPorId(usuarioId);
        if (usuario == null) {
            System.out.println("El usuario con ID " + usuarioId + " no existe.");
            return;
        }

        if (usuarios.contains(usuario)) {
            usuarios.remove(usuario);
            ProyectoUsuarioService dao = new ProyectoUsuarioService();
            boolean eliminado = dao.eliminarAsignacion(this.getId(), usuarioId);
            if (eliminado) {
                System.out.println("Usuario " + usuario.getNombre() + " eliminado del proyecto.");
            } else {
                System.out.println("Error al eliminar usuario del proyecto.");
            }
        } else {
            System.out.println("El usuario no está asignado al proyecto.");
        }
    }

    // ✅ Método para obtener los usuarios del proyecto como ObservableList
    public ObservableList<Usuario> verUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios asignados al proyecto.");
        } else {
            System.out.println("Usuarios del Proyecto " + getNombre() + ":");
            for (Usuario usuario : usuarios) {
                System.out.println(usuario.getNombre() + " - " + usuario.getEmail());
            }
        }
        return FXCollections.observableArrayList(usuarios);
    }
*/

    @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", descripcion=" + descripcion +
                ", fechaCreacion=" + fechaCreacion +
                ", creadoPor=" + creadoPor +
                ", usuarios=" + usuarios +
                ", fases=" + fases +
                '}';
    }
}
