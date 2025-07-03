package com.uce.insight.ui;

import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class StageFacade {

    public static void showWindow(URL fxmlLocation, String title, boolean maximized, boolean resizable) {
        try {
            Stage stage = StageFactory.createStage(fxmlLocation, title, maximized, resizable);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
