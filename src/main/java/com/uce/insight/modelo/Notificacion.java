package com.uce.insight.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notificacion {

    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty usuarioId = new SimpleIntegerProperty();
    private StringProperty mensaje = new SimpleStringProperty();
    private BooleanProperty leida = new SimpleBooleanProperty(false);  // Valor por defecto: false
    private ObjectProperty<java.time.LocalDateTime> fechaCreada = new SimpleObjectProperty<>();

    public Notificacion() {}

    public Notificacion(int id, int usuarioId, String mensaje, boolean leida, java.time.LocalDateTime fechaCreada) {
        this.id.set(id);
        this.usuarioId.set(usuarioId);
        this.mensaje.set(mensaje);
        this.leida.set(leida);
        this.fechaCreada.set(fechaCreada);
    }

    // Métodos adicionales para acceder a las propiedades
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public int getUsuarioId() { return usuarioId.get(); }
    public void setUsuarioId(int usuarioId) { this.usuarioId.set(usuarioId); }

    public String getMensaje() { return mensaje.get(); }
    public void setMensaje(String mensaje) { this.mensaje.set(mensaje); }

    public boolean getLeida() { return leida.get(); }
    public void setLeida(boolean leida) { this.leida.set(leida); }

    public java.time.LocalDateTime getFechaCreada() { return fechaCreada.get(); }
    public void setFechaCreada(java.time.LocalDateTime fechaCreada) { this.fechaCreada.set(fechaCreada); }
}
