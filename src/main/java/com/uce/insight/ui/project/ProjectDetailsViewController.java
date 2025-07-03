package com.uce.insight.ui.project;

import com.uce.insight.modelo.Fase;
import com.uce.insight.modelo.Proyecto;
import com.uce.insight.services.FaseService;
import com.uce.insight.ui.IconFlyweight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ProjectDetailsViewController {
    @FXML private Label pname;
    @FXML private Label Pdescription;
    @FXML private VBox fasesContainer;

    private Proyecto proyecto;
    private FaseService faseService = new FaseService();

    // Instancia única del controlador de fases
    private FasesController fasesController;
    private VBox fasesView;

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        actualizarVista();
    }

    private void actualizarVista() {
        if (proyecto != null) {
            pname.setText(proyecto.getNombre());
            Pdescription.setText(proyecto.getDescripcion());

            cargarFases();
        }
    }

    private void cargarFases() {
        try {
            // Cargar solo una vez la vista de fases
            if (fasesView == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/project/FasesView.fxml"));
                fasesView = loader.load();
                fasesController = loader.getController();
            }

            // Configurar el controlador con el proyecto actual
            fasesController.setProyectoId(proyecto.getId());

            // Cargar fases desde la base de datos
            List<Fase> fases = faseService.obtenerFasesPorProyecto(proyecto.getId());
            fasesController.setFases(fases);

            // Limpiar y añadir la vista (sin recrearla)
            fasesContainer.getChildren().clear();
            fasesContainer.getChildren().add(fasesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageProject(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/project/ManageProject.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Administrar");
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);

            IconFlyweight.applyIcon(stage);
            ManageProjectController controller = loader.getController();
            controller.setProyecto(proyecto);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}