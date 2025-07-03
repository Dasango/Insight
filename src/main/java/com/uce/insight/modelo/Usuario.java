package com.uce.insight.modelo;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class Usuario {
    // Propiedades JavaFX
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty clave = new SimpleStringProperty();

    // Constructor vacío
    public Usuario() {}

    // Constructor con campos básicos (sin id, que es autogenerado normalmente)
    public Usuario(String nombre, String email, String clave) {
        this.nombre.set(nombre);
        this.email.set(email);
        this.clave.set(clave);
    }
    public Usuario(int id, String nombre, String email, String clave) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.email.set(email);
        this.clave.set(clave);
    }


    // --- ID ---
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // --- Nombre ---
    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    // --- Email ---
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // --- Clave ---
    public String getClave() {
        return clave.get();
    }

    public void setClave(String clave) {
        this.clave.set(clave);
    }

    public StringProperty claveProperty() {
        return clave;
    }
}
