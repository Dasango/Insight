package com.uce.insight.ui.project;


import com.uce.insight.modelo.Proyecto;
import com.uce.insight.modelo.ProyectoUsuario;
import com.uce.insight.modelo.Usuario;
import com.uce.insight.services.ProyectoService;
import com.uce.insight.services.ProyectoUsuarioService;
import com.uce.insight.ui.main.ProjectObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CreateProjectController {

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionArea;

    private final ProyectoService proyectoService = new ProyectoService(); // Si tienes uno

    private Usuario currentUser;
    public void setCurrentUser(Usuario user) {
        this.currentUser = user;

    }
    @FXML
    private void handleCreateProject(ActionEvent event) {
        String name = nameField.getText();
        String description = descriptionArea.getText();

        if (name == null || name.trim().isEmpty()) {
            showAlert("El nombre del proyecto es obligatorio.");
            return;
        }

        // Obtener el ID del usuario actual
        int creadoPor = currentUser.getId();

        // Crear el objeto Proyecto
        Proyecto nuevoProyecto = new Proyecto();
        nuevoProyecto.setNombre(name.trim());
        nuevoProyecto.setDescripcion(description != null ? description.trim() : "");
        nuevoProyecto.setCreadoPor(creadoPor);

        // Crear el proyecto en la base de datos
        int proyectoId = proyectoService.crearProyecto(nuevoProyecto);
        if (proyectoId > 0) {

            // Crear la relación de propiedad
            ProyectoUsuarioService proyectoUsuarioService = new ProyectoUsuarioService();
            ProyectoUsuario relacion = new ProyectoUsuario(proyectoId, creadoPor, "dueno");

            if (proyectoUsuarioService.asignarUsuarioAProyecto(relacion)) {
                showAlert("Proyecto creado exitosamente y asignado como dueño");
                ProjectObserver.hayCambiosEnProyectos();
            } else {
                showAlert("Proyecto creado pero hubo un error al asignar la propiedad");
            }

            // Limpiar los campos
            nameField.clear();
            descriptionArea.clear();
            closeStage(event);
        } else {
            showAlert("Error al crear el proyecto. Por favor, inténtelo nuevamente.");
        }
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validación");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
