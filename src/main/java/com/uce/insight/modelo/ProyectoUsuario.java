package com.uce.insight.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProyectoUsuario {

    private IntegerProperty proyectoId = new SimpleIntegerProperty();
    private IntegerProperty usuarioId = new SimpleIntegerProperty();
    private StringProperty rol = new SimpleStringProperty("colaborador"); // Valor por defecto

    public ProyectoUsuario() {}

    public ProyectoUsuario(int proyectoId, int usuarioId, String rol) {
        this.proyectoId.set(proyectoId);
        this.usuarioId.set(usuarioId);
        this.rol.set(rol);
    }

    public int getProyectoId() { return proyectoId.get(); }
    public void setProyectoId(int id) { this.proyectoId.set(id); }

    public int getUsuarioId() { return usuarioId.get(); }
    public void setUsuarioId(int id) { this.usuarioId.set(id); }

    public String getRol() { return rol.get(); }
    public void setRol(String rol) { this.rol.set(rol); }
}
