package com.uce.insight.ui;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class IconFlyweight {
    // Instancia compartida
    private static Image sharedIcon;

    // Método para obtener el ícono compartido
    public static Image getIcon() {
        if (sharedIcon == null) {
            sharedIcon = new Image(IconFlyweight.class.getResourceAsStream("logo.png"));
        }
        return sharedIcon;
    }

    // Método auxiliar para aplicar el ícono a un Stage
    public static void applyIcon(Stage stage) {
        stage.getIcons().add(getIcon());
    }
}
