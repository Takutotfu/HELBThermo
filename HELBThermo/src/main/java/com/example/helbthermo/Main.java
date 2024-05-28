package com.example.helbthermo;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    // Point d'entrée de l'application
    @Override
    public void start(Stage stage) throws Exception {
        ThermoView view = new ThermoView(stage);
        ThermoController controller = new ThermoController(view);
    }

    public static void main(String[] args) {
        launch();
    }
}