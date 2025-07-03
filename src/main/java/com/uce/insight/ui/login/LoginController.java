package com.uce.insight.ui.login;

import com.uce.insight.modelo.Usuario;
import com.uce.insight.services.UsuarioService;
import com.uce.insight.ui.IconFlyweight;
import com.uce.insight.ui.StageFacade;
import com.uce.insight.ui.main.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.net.URL;

public class LoginController{

    public Rectangle frostedRect;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField password;

    UsuarioService servicio = new UsuarioService();


    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String email = emailField.getText(); // Usa nombres claros para evitar confusiones
        String pass = password.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            showAlert(AlertType.WARNING, "Campos vacíos", "Por favor ingresa usuario y contraseña.");
            return;
        }

        try {
            // ← OBTENER el usuario desde el login
            Usuario user = servicio.login(email, pass);

            // Carga la nueva ventana (Main)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/main/main.fxml"));
            Parent root = loader.load();

            // Obtén el controlador y pásale el nombre del usuario
            MainController mainController = loader.getController();
            mainController.setCurrentUser(user); // Si es un record
            // o mainController.setCurrentUser(user.getName()); si es clase POJO

            // Muestra la nueva ventana
            Stage mainStage = new Stage();
            mainStage.setTitle("Panel Principal");
            mainStage.setScene(new Scene(root));
            mainStage.setMaximized(true);
            mainStage.setResizable(true);

            IconFlyweight.applyIcon(mainStage);
            mainStage.show();


            // Cierra la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Login Fallido", "Usuario o contraseña incorrectos.");
        }
    }




    @FXML
    private void handleCreateUserButtonAction(ActionEvent event) {

        URL fxmlLocation = getClass().getResource("/com/uce/insight/ui/login/register.fxml");

        if (fxmlLocation == null) {
            System.out.println("FXML no encontrado: /com/uce/insight/ui/login/register.fxml");
            return;
        }

        StageFacade.showWindow(fxmlLocation, "Registrar nuevo usuario", false, false);

    }


    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
