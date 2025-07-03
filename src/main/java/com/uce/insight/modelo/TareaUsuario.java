package com.uce.insight.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TareaUsuario {

    private IntegerProperty tareaId = new SimpleIntegerProperty();
    private IntegerProperty usuarioId = new SimpleIntegerProperty();
    private ObjectProperty<LocalDateTime> asignadoEn = new SimpleObjectProperty<>();

    public TareaUsuario() {}

    public TareaUsuario(int tareaId, int usuarioId, LocalDateTime asignadoEn) {
        this.tareaId.set(tareaId);
        this.usuarioId.set(usuarioId);
        this.asignadoEn.set(asignadoEn);
    }

    public int getTareaId() { return tareaId.get(); }
    public void setTareaId(int id) { this.tareaId.set(id); }

    public int getUsuarioId() { return usuarioId.get(); }
    public void setUsuarioId(int id) { this.usuarioId.set(id); }

    public LocalDateTime getAsignadoEn() { return asignadoEn.get(); }
    public void setAsignadoEn(LocalDateTime fecha) { this.asignadoEn.set(fecha); }
}
