package com.uce.insight.ui.project;

import com.uce.insight.modelo.Proyecto;
import com.uce.insight.modelo.ProyectoUsuario;
import com.uce.insight.modelo.Usuario;
import com.uce.insight.services.ProyectoService;
import com.uce.insight.services.ProyectoUsuarioService;
import com.uce.insight.services.UsuarioService;
import com.uce.insight.ui.main.ProjectObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

public class ManageProjectController {

    @FXML private TextField usernameMailField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button addUserButton;
    @FXML private TableView<UsuarioProyectoDTO> usersTable;
    @FXML private TableColumn<UsuarioProyectoDTO, String> userColumn;
    @FXML private TableColumn<UsuarioProyectoDTO, String> roleColumn;
    @FXML private Button deleteProjectButton;

    private Proyecto proyecto;
    private Usuario usuarioActual;

    private final UsuarioService usuarioService = new UsuarioService();
    private final ProyectoUsuarioService proyectoUsuarioService = new ProyectoUsuarioService();
    private final ObservableList<UsuarioProyectoDTO> usuariosProyecto = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar el ComboBox de roles
        roleComboBox.setItems(FXCollections.observableArrayList("dueno", "colaborador"));
        roleComboBox.getSelectionModel().selectFirst();

        // Configurar las columnas de la tabla
        userColumn.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));

        usersTable.setItems(usuariosProyecto);
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        cargarUsuariosProyecto();

        // Deshabilitar botón de eliminación si no es el creador
        if (usuarioActual != null && proyecto.getCreadoPor() != usuarioActual.getId()) {
            deleteProjectButton.setDisable(true);
        }
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    private void cargarUsuariosProyecto() {
        if (proyecto != null) {
            usuariosProyecto.clear();
            proyectoUsuarioService.obtenerUsuariosDeProyecto(proyecto.getId()).forEach(pu -> {
                Usuario usuario = usuarioService.obtenerUsuarioPorId(pu.getUsuarioId());
                if (usuario != null) {
                    usuariosProyecto.add(new UsuarioProyectoDTO(
                            usuario.getNombre() + " (" + usuario.getEmail() + ")",
                            pu.getRol(),
                            usuario.getId()
                    ));
                }
            });
        }
    }

    @FXML
    private void handleDeleteProject(ActionEvent event) {
        if (usuarioActual == null) {
            mostrarAlerta("Error", "Usuario no autenticado", "No se puede verificar el usuario actual.");
            return;
        }

        if (proyecto.getCreadoPor() != usuarioActual.getId()) {
            mostrarAlerta("Acceso denegado", "No autorizado", "Solo el creador del proyecto puede eliminarlo.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("Eliminar proyecto");
        alert.setContentText("¿Estás seguro de que deseas eliminar este proyecto permanentemente? " +
                "Se eliminarán todas las fases y relaciones asociadas. Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ProyectoService proyectoService = new ProyectoService();
            boolean eliminado = proyectoService.eliminarProyecto(proyecto.getId(), usuarioActual);

            if (eliminado) {
                ((Stage) deleteProjectButton.getScene().getWindow()).close();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Proyecto eliminado");
                successAlert.setHeaderText(null);
                successAlert.setContentText("El proyecto y todos sus elementos asociados fueron eliminados correctamente.");
                successAlert.showAndWait();

                ProjectObserver.hayCambiosEnProyectos();
            } else {
                mostrarAlerta("Error", "Error al eliminar", "No se pudo eliminar el proyecto y sus dependencias.");
            }
        }
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        String email = usernameMailField.getText().trim();
        String rol = roleComboBox.getValue();

        if (email.isEmpty()) {
            mostrarAlerta("Error", "Campo vacío", "Por favor ingresa el correo del usuario.");
            return;
        }

        if (rol == null || rol.isEmpty()) {
            mostrarAlerta("Error", "Rol no seleccionado", "Por favor selecciona un rol para el usuario.");
            return;
        }

        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email);
        if (usuario == null) {
            mostrarAlerta("Usuario no encontrado", "Error", "No existe un usuario con el correo: " + email);
            return;
        }

        boolean usuarioYaEnProyecto = usuariosProyecto.stream()
                .anyMatch(up -> up.getIdUsuario() == usuario.getId());

        if (usuarioYaEnProyecto) {
            mostrarAlerta("Usuario existente", "Error", "Este usuario ya está asignado al proyecto.");
            return;
        }

        ProyectoUsuario proyectoUsuario = new ProyectoUsuario(
                proyecto.getId(),
                usuario.getId(),
                rol
        );

        if (proyectoUsuarioService.asignarUsuarioAProyecto(proyectoUsuario)) {
            usuariosProyecto.add(new UsuarioProyectoDTO(
                    usuario.getNombre() + " (" + usuario.getEmail() + ")",
                    rol,
                    usuario.getId()
            ));

            usernameMailField.clear();
            mostrarAlerta("Éxito", "Usuario añadido", "El usuario ha sido añadido al proyecto correctamente.");
        } else {
            mostrarAlerta("Error", "Error al añadir usuario", "No se pudo añadir el usuario al proyecto.");
        }
    }

    private void mostrarAlerta(String titulo, String encabezado, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static class UsuarioProyectoDTO {
        private final String nombreUsuario;
        private final String rol;
        private final int idUsuario;

        public UsuarioProyectoDTO(String nombreUsuario, String rol, int idUsuario) {
            this.nombreUsuario = nombreUsuario;
            this.rol = rol;
            this.idUsuario = idUsuario;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public String getRol() {
            return rol;
        }

        public int getIdUsuario() {
            return idUsuario;
        }
    }
}
