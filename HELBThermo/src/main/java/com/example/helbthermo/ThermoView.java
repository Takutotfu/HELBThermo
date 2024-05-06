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

    public void initView() {
        BorderPane root = new BorderPane();
        root.setPrefSize(1080, 720);

        // Top
        initTopHBox(root);

        // Left
        initLeftHBox(root);

        // Center
        initCenterGridPane(root);

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

    private void initCenterGridPane(BorderPane root) {
        GridPane centerGrid = new GridPane();
        centerGrid.setHgap(5);
        centerGrid.setVgap(5);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                Pane cell = new Pane();
                cell.setPrefSize(200, 200);
                cell.setStyle("-fx-background-color: white;");
                Label label = new Label("Cell " + (i * 5 + j + 1));
                cell.getChildren().add(label);
                centerGrid.add(cell, j, i);
            }
        }

        root.setCenter(centerGrid);
    }

    private void initLeftHBox(BorderPane root) {
        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPrefHeight(565);
        leftBox.setPrefWidth(163);
        leftBox.setSpacing(20);

        // Ajout des marges à gauche et à droite
        BorderPane.setMargin(leftBox, new Insets(0, 30, 0, 30));

        HBox buttonBox = new HBox();

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

        ListView<String> listView = new ListView<>(); // Placeholder for list of items

        leftBox.getChildren().addAll(buttonBox, listView);

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
