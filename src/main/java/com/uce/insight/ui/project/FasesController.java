package com.uce.insight.ui.project;

import com.uce.insight.modelo.Fase;
import com.uce.insight.services.FaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FasesController {
    @FXML private VBox fasesContainer;
    @FXML private VBox fasesList;
    @FXML private Button addFaseBtn;

    private int proyectoId;
    private FaseService faseService = new FaseService();

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    @FXML
    private void handleAddFase() {
        Dialog<Fase> dialog = new Dialog<>();
        dialog.setTitle("Agregar Nueva Fase");
        dialog.setHeaderText("Complete los detalles de la fase");

        ButtonType addButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");
        TextArea descripcionArea = new TextArea();
        descripcionArea.setPromptText("Descripción");
        descripcionArea.setPrefRowCount(3);

        Spinner<Integer> plazoSpinner = new Spinner<>(1, 365, 7, 1);
        plazoSpinner.setEditable(true);

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombreField, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(descripcionArea, 1, 1);
        grid.add(new Label("Plazo (días):"), 0, 2);
        grid.add(plazoSpinner, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Fase nuevaFase = new Fase();
                nuevaFase.setNombre(nombreField.getText());
                nuevaFase.setDescripcion(descripcionArea.getText());
                nuevaFase.setFechaInicio(LocalDate.now());
                nuevaFase.setFechaFin(LocalDate.now().plus(plazoSpinner.getValue(), ChronoUnit.DAYS));
                nuevaFase.setProyectoId(proyectoId);
                return nuevaFase;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(fase -> {
            if (faseService.crearFase(fase)) {
                agregarFaseALista(fase);
            } else {
                mostrarAlerta("Error", "No se pudo guardar la fase", Alert.AlertType.ERROR);
            }
        });
    }

    private void agregarFaseALista(Fase fase) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uce/insight/ui/project/FaseItem.fxml"));
            HBox faseItem = loader.load();
            FaseItemController controller = loader.getController();
            controller.setFase(fase);
            controller.setFaseService(faseService);
            controller.setParentController(this);

            fasesList.getChildren().add(faseItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFases(List<Fase> fases) {
        fasesList.getChildren().clear();
        fases.forEach(this::agregarFaseALista);
    }

    public void eliminarFaseDeLista(Fase fase) {
        fasesList.getChildren().removeIf(node -> {
            if (node.getUserData() != null && node.getUserData() instanceof Fase) {
                return ((Fase) node.getUserData()).getId() == fase.getId();
            }
            return false;
        });
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}