package com.uce.insight.ui.main;

import com.uce.insight.modelo.Proyecto;
import com.uce.insight.ui.project.ProjectDetailsViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ProjectController {

    @FXML
    private Label projectNameLabel;
    @FXML
    private Label activityLabel;

    private StackPane contentArea;
    private Proyecto proyecto;

    // Instancia singleton del details view
    private static Node detailsViewInstance = null;
    private static ProjectDetailsViewController detailsController = null;

    public void setProjectData(Proyecto proyecto) {
        this.proyecto = proyecto;
        projectNameLabel.setText(proyecto.getNombre());
        activityLabel.setText(proyecto.getDescripcion());
    }

    @FXML
    private void handleClick() {
        try {
            // Cargar solo una vez el details view
            if (detailsViewInstance == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/project/ProjectDetailsView.fxml"));
                detailsViewInstance = loader.load();
                detailsController = loader.getController();
            }

            // Actualizar el proyecto en el controlador existente
            detailsController.setProyecto(this.proyecto);

            // Mostrar la vista (que ya está cargada)
            contentArea.getChildren().setAll(detailsViewInstance);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
}