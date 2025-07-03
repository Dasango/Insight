package com.uce.insight.ui.login;

import com.uce.insight.modelo.Usuario;
import com.uce.insight.services.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;


    private UsuarioService servicio = new UsuarioService();


    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validar que el email contenga al menos un '@'
        if (!email.contains("@")) {
            showAlert(Alert.AlertType.ERROR, "⚠️ No es un correo valido");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Las contraseñas no coinciden");
            return;
        }

        Usuario u = new Usuario(username, email, password);

        boolean creado = servicio.crearUsuario(u);
        if (creado) {
            showAlert(Alert.AlertType.INFORMATION, "✅ Usuario registrado correctamente");
            closeStage(event);
        }
    }





    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


}
