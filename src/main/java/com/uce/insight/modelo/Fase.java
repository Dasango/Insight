package com.uce.insight.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Fase {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private StringProperty descripcion = new SimpleStringProperty();
    private ObjectProperty<LocalDate> fechaInicio = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> fechaFin = new SimpleObjectProperty<>();
    private IntegerProperty proyectoId = new SimpleIntegerProperty();

    // Constructor vacío para JavaFX
    public Fase() {
        this.id = new SimpleIntegerProperty(0);
        this.nombre = new SimpleStringProperty("");
        this.descripcion = new SimpleStringProperty("");
        this.fechaInicio = new SimpleObjectProperty<>(LocalDate.now());
        this.fechaFin = new SimpleObjectProperty<>(LocalDate.now());
        this.proyectoId = new SimpleIntegerProperty(0);
    }


    // Constructor completo
    public Fase(int id, String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, int proyectoId) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.descripcion.set(descripcion);
        this.fechaInicio.set(fechaInicio);
        this.fechaFin.set(fechaFin);
        this.proyectoId.set(proyectoId);
    }

    // Getters/setters para uso con JDBC
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }

    public LocalDate getFechaInicio() { return fechaInicio.get(); }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio.set(fechaInicio); }

    public LocalDate getFechaFin() { return fechaFin.get(); }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin.set(fechaFin); }

    public int getProyectoId() { return proyectoId.get(); }
    public void setProyectoId(int proyectoId) { this.proyectoId.set(proyectoId); }



/*
    // Mostrar detalles
    public void verDetalles() {
        System.out.println("----- Detalles de la Fase -----");
        System.out.println("ID: " + getId());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Descripción: " + getDescripcion());
        System.out.println("Fecha de Inicio: " + getFechaInicio());
        System.out.println("Fecha de Fin: " + getFechaFin());
        System.out.println("Proyecto ID: " + getProyectoId());
    }
*/

}
