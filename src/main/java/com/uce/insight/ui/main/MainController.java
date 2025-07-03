package com.uce.insight.ui.main;

import com.uce.insight.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnMisProyectos;
    @FXML private Button btnMisProyectos1;
    @FXML private Button btnMisProyectos2;

    @FXML
    private Label usernameTag;
    private Usuario currentUser;

    private Node assistantTabNode;
    private Node projectsTabNode;
    private Node notificationsTabNode;

    private ProjectsTabController projectsTabController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAssistantTab();
        // Simple navegación sin cambiar clases CSS
        btnMisProyectos.setOnAction(event -> loadProjectsTab());
        btnMisProyectos1.setOnAction(event -> loadNotificationsTab());
        btnMisProyectos2.setOnAction(event -> loadAssistantTab());
    }

    public void setCurrentUser(Usuario user) {
        this.currentUser = user;
        if (usernameTag != null) {
            usernameTag.setText(user.getNombre()); // o user.getName()
        }
    }


    private void loadAssistantTab() {
        try {
            if (assistantTabNode == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/main/assistantTab.fxml"));
                assistantTabNode = loader.load();
            }
            contentArea.getChildren().setAll(assistantTabNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProjectsTab() {
        try {
            if (projectsTabNode == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/main/ProjectsTab.fxml"));
                projectsTabNode = loader.load();
                projectsTabController = loader.getController(); // Guardar referencia
                projectsTabController.setCurrentUser(currentUser);
                projectsTabController.setContentArea(contentArea);
            } else if(ProjectObserver.hayCambios==true) {
                projectsTabController.cargarProyectosDeUsuario(); // Llamar al método en el controlador
                System.out.println("recarga los proyectos");
                ProjectObserver.noHayCambios();
            }
            contentArea.getChildren().setAll(projectsTabNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNotificationsTab() {
        try {
            if (notificationsTabNode == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/main/notificationsTab.fxml"));
                notificationsTabNode = loader.load();
            }
            contentArea.getChildren().setAll(notificationsTabNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
