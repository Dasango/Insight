package com.uce.insight.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class StageFactory {

    public static Stage createStage(URL fxmlLocation, String title, boolean maximized, boolean resizable) throws IOException {
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.setResizable(resizable);

        IconFlyweight.applyIcon(stage);
        return stage;
    }

}
