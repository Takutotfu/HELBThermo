package com.example.helbthermo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ThermoView {
    private Stage stage;
    private Scene scene;

    public ThermoView(Stage stage) {
        this.stage = stage;
    }

    public void initView(int rowCell, int colCell) {
        BorderPane root = new BorderPane();
        root.setPrefSize(1080, 720);

        // Top
        initTopHBox(root);

        // Left
        initLeftHBox(root);

        // Center
        initCenterGridPane(root, rowCell, colCell);

        // Right
        VBox rightBox = new VBox();
        rightBox.setPrefWidth(86);
        root.setRight(rightBox);

        // Bottom
        VBox bottomBox = new VBox();
        bottomBox.setPrefHeight(55);
        root.setBottom(bottomBox);

        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("HELBThermo");
        stage.show();
    }

    private void initCenterGridPane(BorderPane root, int rowCell, int colCell) {
        GridPane centerGrid = new GridPane();
        centerGrid.setAlignment(Pos.CENTER);
        centerGrid.setHgap(5);
        centerGrid.setVgap(5);

        if (colCell >= 3 && rowCell >= 3 && colCell <= 12 && rowCell <= 12) {
            for (int i = 0; i < rowCell; i++) {
                for (int j = 0; j < colCell; j++) {
                    Button button = new Button();
                    button.setPrefSize(50.0, 50.0);
                    centerGrid.add(button, j, i);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cell pane is oversize");

            alert.showAndWait();
        }

        root.setCenter(centerGrid);
    }

    private void initLeftHBox(BorderPane root) {
        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPrefHeight(565);
        leftBox.setPrefWidth(200);
        leftBox.setSpacing(20);

        // Ajout des marges à gauche et à droite
        BorderPane.setMargin(leftBox, new Insets(0, 30, 0, 30));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        Button playButton = new Button();
        playButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/play.png"))));
        HBox.setMargin(playButton, new Insets(0, 0, 0, 10)); // Marge à droite de 10 pixels

        Button pauseButton = new Button();
        pauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/pause.png"))));
        HBox.setMargin(pauseButton, new Insets(0, 0, 0, 10)); // Marge à droite de 10 pixels

        Button resetButton = new Button();
        resetButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/reset.png"))));
        HBox.setMargin(resetButton, new Insets(0, 0, 0, 10)); // Marge à droite de 10 pixels

        buttonBox.getChildren().addAll(playButton, pauseButton, resetButton);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(400.0);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        VBox buttonVBox = new VBox(); // Utiliser une VBox pour aligner verticalement les boutons
        buttonVBox.setAlignment(Pos.CENTER); // Centrer le contenu
        buttonVBox.setSpacing(20); // Espacement vertical entre les boutons

        for (int i = 0; i < 6; i++) {
            Button button = new Button();
            button.setAlignment(Pos.CENTER);
            button.setPrefSize(100.0, 100.0);
            buttonVBox.getChildren().add(button); // Ajout du bouton à la VBox
        }

        scrollPane.setContent(buttonVBox);

        leftBox.getChildren().addAll(buttonBox, scrollPane);

        root.setLeft(leftBox);
    }



    private void initTopHBox(BorderPane root) {
        HBox topBox = new HBox(20);
        topBox.setSpacing(10);
        topBox.setPrefHeight(100);
        topBox.setPrefWidth(200);
        topBox.setAlignment(Pos.CENTER);

        VBox timeBox = new VBox(new Label("Temps : "), new Label()); // Placeholder for time display
        timeBox.setAlignment(Pos.CENTER);

        VBox priceBox = new VBox(new Label("€ : "), new Label()); // Placeholder for price display
        priceBox.setAlignment(Pos.CENTER);

        VBox extTempBox = new VBox(new Label("T° ext. : "), new Label()); // Placeholder for external temperature display
        extTempBox.setAlignment(Pos.CENTER);

        VBox avgTempBox = new VBox(new Label("T° moy. : "), new Label()); // Placeholder for average temperature display
        avgTempBox.setAlignment(Pos.CENTER);

        MenuButton modeMenu = new MenuButton("Chauffe Mode");
        modeMenu.getItems().addAll(new MenuItem("Item 1"), new MenuItem("Item 2"), new MenuItem("Item 3"));
        modeMenu.setAlignment(Pos.CENTER);

        topBox.getChildren().addAll(timeBox, priceBox, extTempBox, avgTempBox, modeMenu);

        root.setTop(topBox);
    }
}
