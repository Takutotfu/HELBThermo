package com.example.helbthermo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Classe pour gérer l'affichage d'une fenetre popup de confirmation
public class ConfirmModificationView {

    // Attributs de configuration
    private static final int WIDTH = 300;
    private static final int SPACING = 10;

    // Attributs de la classe
    private static boolean answer = false;

    // Méthodes pour afficher la fenetre
    public static boolean display() {
        answer = false;

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Cell modification confirmation");
        window.setMinWidth(WIDTH);
        window.setResizable(false);

        Label label = new Label();
        label.setText("Are you sure ?");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e ->{
            answer = true;
            window.close();
        });

        noButton.setOnAction(e ->{
            answer = false;
            window.close();
        });

        HBox hLayout = new HBox();
        hLayout.getChildren().addAll(yesButton, noButton);
        hLayout.setAlignment(Pos.CENTER);
        hLayout.setSpacing(SPACING);

        VBox vLayout = new VBox(label, hLayout);
        vLayout.setAlignment(Pos.CENTER);
        vLayout.setSpacing(SPACING);

        Scene scene = new Scene(vLayout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
