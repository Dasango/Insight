package com.uce.insight.ui.project;

import com.uce.insight.modelo.Fase;
import com.uce.insight.services.FaseService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FaseItemController {
    @FXML private Label nombreLabel;
    @FXML private Label descripcionLabel;
    @FXML private Label fechasLabel;
    @FXML private HBox faseItemContainer;

    private Fase fase;
    private FaseService faseService;
    private FasesController parentController;

    public void setFase(Fase fase) {
        this.fase = fase;
        actualizarVista();
        faseItemContainer.setUserData(fase);
    }

    public void setFaseService(FaseService faseService) {
        this.faseService = faseService;
    }

    public void setParentController(FasesController parentController) {
        this.parentController = parentController;
    }

    private void actualizarVista() {
        nombreLabel.setText(fase.getNombre());
        descripcionLabel.setText(fase.getDescripcion());
        fechasLabel.setText(String.format("Inicio: %s - Fin: %s",
                fase.getFechaInicio().toString(),
                fase.getFechaFin().toString()));
    }

    @FXML
    private void handleEliminar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro de eliminar esta fase?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (faseService.eliminarFase(fase.getId())) {
                    parentController.eliminarFaseDeLista(fase);
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("No se pudo eliminar la fase");
                    errorAlert.showAndWait();
                }
            }
        });
    }
}