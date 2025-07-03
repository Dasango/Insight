package com.uce.insight.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

// Modelo para representar la tabla EstadoTarea con JavaFX y Lombok
@Getter
@Setter
public class EstadoTarea {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();

    public EstadoTarea() {}

    public EstadoTarea(int id, String nombre) {
        this.id.set(id);
        this.nombre.set(nombre);
    }

    // Getters/setters estándar para JDBC
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
}
