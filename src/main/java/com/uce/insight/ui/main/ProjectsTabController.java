package com.uce.insight.ui.main;

import com.uce.insight.modelo.Proyecto;
import com.uce.insight.modelo.ProyectoUsuario;
import com.uce.insight.modelo.Usuario;
import com.uce.insight.services.ProyectoService;
import com.uce.insight.services.ProyectoUsuarioService;
import com.uce.insight.ui.IconFlyweight;
import com.uce.insight.ui.StageFacade;
import com.uce.insight.ui.StageFactory;
import com.uce.insight.ui.project.CreateProjectController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectsTabController {

    @FXML
    private FlowPane projectsFlowPane;

    private StackPane contentArea; // Este se asigna desde MainController

    private Usuario currentUser;
    private final ProyectoUsuarioService proyectoUsuarioService = new ProyectoUsuarioService();
    private final ProyectoService proyectoService = new ProyectoService(); // Si tienes uno

    public void setCurrentUser(Usuario user) {
        this.currentUser = user;

    }

    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
        cargarProyectosDeUsuario();
    }


    public void cargarProyectosDeUsuario() {
        projectsFlowPane.getChildren().clear();

        ObservableList<ProyectoUsuario> relaciones = proyectoUsuarioService.obtenerProyectosDeUsuario(currentUser.getId());

        for (ProyectoUsuario relacion : relaciones) {
            Proyecto proyecto = proyectoService.obtenerProyectoPorId(relacion.getProyectoId());
            if (proyecto != null) {
                loadProject(proyecto); // Este método ya deberías tenerlo
            }
        }
    }

    public void loadProject(Proyecto proyecto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/main/project.fxml"));
            AnchorPane projectCard = loader.load();

            ProjectController controller = loader.getController();
            controller.setProjectData(proyecto);  // Ahora pasas el proyecto completo
            controller.setContentArea(contentArea);

            projectsFlowPane.getChildren().add(projectCard);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCreateProject(ActionEvent event) {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/project/CreateProjectView.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Crear proyecto");
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);

            IconFlyweight.applyIcon(stage);

            CreateProjectController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
