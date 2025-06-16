package com.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Usuario {
    // JavaFX Properties para facilitar el binding con la GUI
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private ObjectProperty<LocalDateTime> creadoEn = new SimpleObjectProperty<>();

    // Constructor vacío
    public Usuario() {}

    // Constructor completo
    public Usuario(int id, String nombre, String email, LocalDateTime creadoEn) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.email.set(email);
        this.creadoEn.set(creadoEn);
    }

    // Métodos de acceso tradicionales si son necesarios (útiles para JPA o JDBC)
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }

    public LocalDateTime getCreadoEn() { return creadoEn.get(); }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn.set(creadoEn); }

/*
    //Con paginación
    public ObservableList<ProyectoUsuario> verProyectos(int limit, int offset) {
        ProyectoUsuarioService dao = new ProyectoUsuarioService();
        return dao.obtenerProyectosDeUsuarioPaginado(this.getId(), limit, offset);
    }


    //SI no quieres usar paginación:
    public ObservableList<ProyectoUsuario> verProyectos() {
        ProyectoUsuarioService dao = new ProyectoUsuarioService();
        return dao.obtenerProyectosDeUsuario(this.getId());
    }
*/
}
