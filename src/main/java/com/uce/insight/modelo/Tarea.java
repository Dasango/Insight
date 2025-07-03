package com.uce.insight.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Tarea {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty titulo = new SimpleStringProperty();
    private StringProperty descripcion = new SimpleStringProperty();
    private ObjectProperty<LocalDate> fechaEntrega = new SimpleObjectProperty<>();
    private IntegerProperty faseId = new SimpleIntegerProperty();
    private IntegerProperty estadoId = new SimpleIntegerProperty(); // Puede ser null (nullable en DB)

    public Tarea() {}

    public Tarea(int id, String titulo, String descripcion, LocalDate fechaEntrega, int faseId, Integer estadoId) {
        this.id.set(id);
        this.titulo.set(titulo);
        this.descripcion.set(descripcion);
        this.fechaEntrega.set(fechaEntrega);
        this.faseId.set(faseId);
        if (estadoId != null) this.estadoId.set(estadoId);
    }

    // Métodos convencionales para JDBC
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getTitulo() { return titulo.get(); }
    public void setTitulo(String titulo) { this.titulo.set(titulo); }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }

    public LocalDate getFechaEntrega() { return fechaEntrega.get(); }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega.set(fechaEntrega); }

    public int getFaseId() { return faseId.get(); }
    public void setFaseId(int faseId) { this.faseId.set(faseId); }

    public Integer getEstadoId() { return estadoId.get(); }
    public void setEstadoId(Integer estadoId) { this.estadoId.set(estadoId != null ? estadoId : 0); }
}
