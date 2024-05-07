package com.example.helbthermo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

// TODO : CELL_SIZE dans la classe Cell pour initCenterGridPane & initLeftHBox

public class ThermoView {
    private final int HEIGHT = 720;
    private final int WIDTH = 1280;
    private final int SPACING = 20;
    private final int LABEL_SIZE = 150;
    private final int MAX_GRIDPANE_SIZE = 12;
    private final int MIN_GRIDPANE_SIZE = 3;

    private int rowCell, colCell;
    private HashMap<String, Button> buttonCellMap;

    private Stage stage;
    private Scene scene;
    private HELBThermo helbThermo;

    public ThermoView(Stage stage) {
        this.stage = stage;
        this.buttonCellMap = new HashMap<>();
    }

    public void initView(int rowCell, int colCell, HELBThermo helbThermo) {
        this.rowCell = rowCell;
        this.colCell = colCell;
        this.helbThermo = helbThermo;

        BorderPane root = new BorderPane();
        root.setPrefSize(WIDTH, HEIGHT);

        // Top
        initTopHBox(root);

        // Left
        initLeftHBox(root);

        // Center
        initCenterGridPane(root, rowCell, colCell);

        // Right
        VBox rightBox = new VBox();
        rightBox.setPrefWidth(4 * SPACING);
        root.setRight(rightBox);

        // Bottom
        VBox bottomBox = new VBox();
        bottomBox.setPrefHeight(4 * SPACING);
        root.setBottom(bottomBox);

        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("HELBThermo");
        stage.show();
    }

    private void initCenterGridPane(BorderPane root, int rowCell, int colCell) {
        GridPane centerGrid = new GridPane();
        centerGrid.setAlignment(Pos.CENTER);
        centerGrid.setHgap(SPACING / 4);
        centerGrid.setVgap(SPACING / 4);

        if (colCell >= MIN_GRIDPANE_SIZE && rowCell >= MIN_GRIDPANE_SIZE
                && colCell <= MAX_GRIDPANE_SIZE && rowCell <= MAX_GRIDPANE_SIZE) {

            for (int i = 0; i < rowCell; i++) {
                for (int j = 0; j < colCell; j++) {
                    Button button = new Button();
                    button.setPrefSize(Cell.SIZE, Cell.SIZE);
                    buttonCellMap.put("" + i + j, button);
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
        leftBox.setPrefWidth(10 * SPACING);
        leftBox.setSpacing(SPACING);

        // Ajout des marges à gauche et à droite
        BorderPane.setMargin(leftBox, new Insets(0, SPACING, 0, 2 * SPACING));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        Button playButton = new Button();
        playButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/play.png"))));
        HBox.setMargin(playButton, new Insets(0, 0, 0, SPACING / 2)); // Marge à droite de 10 pixels

        Button pauseButton = new Button();
        pauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/pause.png"))));
        HBox.setMargin(pauseButton, new Insets(0, 0, 0, SPACING / 2)); // Marge à droite de 10 pixels

        Button resetButton = new Button();
        resetButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/reset.png"))));
        HBox.setMargin(resetButton, new Insets(0, 0, 0, SPACING / 2)); // Marge à droite de 10 pixels

        buttonBox.getChildren().addAll(playButton, pauseButton, resetButton);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(SPACING * 30);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        VBox buttonVBox = new VBox(); // Utiliser une VBox pour aligner verticalement les boutons
        buttonVBox.setAlignment(Pos.CENTER); // Centrer le contenu
        buttonVBox.setSpacing(SPACING); // Espacement vertical entre les boutons

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
        HBox topBox = new HBox(SPACING);
        topBox.setSpacing(SPACING);
        topBox.setPrefHeight(5 * SPACING);
        topBox.setPrefWidth(10 * SPACING);
        topBox.setAlignment(Pos.CENTER);

        Button timeBox = new Button("Temps : ");
        timeBox.setPrefWidth(LABEL_SIZE);
        timeBox.setAlignment(Pos.CENTER);

        Button priceBox = new Button("€ : ");
        priceBox.setPrefWidth(LABEL_SIZE);
        priceBox.setAlignment(Pos.CENTER);

        Button extTempBox = new Button("T° ext. : ");
        extTempBox.setPrefWidth(LABEL_SIZE);
        extTempBox.setAlignment(Pos.CENTER);

        Button avgTempBox = new Button("T° moy. : ");
        avgTempBox.setPrefWidth(LABEL_SIZE);
        avgTempBox.setAlignment(Pos.CENTER);

        MenuButton modeMenu = new MenuButton("Chauffe Mode");
        modeMenu.getItems().addAll(new MenuItem("Mode Manuel"), new MenuItem("Mode Successif"), new MenuItem("Mode Target"));
        modeMenu.setPrefSize(LABEL_SIZE, SPACING);
        modeMenu.setAlignment(Pos.CENTER);

        topBox.getChildren().addAll(timeBox, priceBox, extTempBox, avgTempBox, modeMenu);

        root.setTop(topBox);
    }

    public void setupHeatSourceCell(HeatSourceCell heatSourceCell) {
        String key = ""+heatSourceCell.getX() + heatSourceCell.getY();
        Button cell = buttonCellMap.get(key);
        cell.setText(String.valueOf(heatSourceCell.getTemperature()));
        cell.setStyle("-fx-background-color: #ff0000; ");
    }

    public void setupDeadCell(DeadCell deadCell) {
        String key = ""+deadCell.getX() + deadCell.getY();
        Button cell = buttonCellMap.get(key);
        cell.setStyle("-fx-background-color: #000000; ");
    }

    public Button getButton(String cellId) { return buttonCellMap.get(cellId); }
}
