package com.uce.insight;

import com.uce.insight.ui.IconFlyweight;
import com.uce.insight.ui.StageFacade;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        URL fxml = HelloApplication.class.getResource("/com/uce/insight/ui/login/login.fxml");
        if (fxml == null) {
            throw new IOException("FXML file not found.");
        }
        StageFacade.showWindow(fxml, "InSight", false, false);
    }


    public static void main(String[] args) {
        launch();
    }
}