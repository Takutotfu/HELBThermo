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

public class ThermoView {
    private final int HEIGHT = 720;
    private final int WIDTH = 1280;
    private final int SPACING = 20;
    private final int LABEL_SIZE = 150;
    private final int MAX_GRIDPANE_SIZE = 12;
    private final int MIN_GRIDPANE_SIZE = 3;

    private final HashMap<String, Button> buttonCellMap;
    private final HashMap<String, Button> buttonHeatCellMap;
    private final Stage stage;

    private VBox heatCellsBox;
    private Button playButton, pauseButton, resetButton;
    private Button timeBox, priceBox, extTempBox, avgTempBox;

    public ThermoView(Stage stage) {
        this.stage = stage;
        this.buttonCellMap = new HashMap<>();
        this.buttonHeatCellMap = new HashMap<>();
    }

    public void initView() {
        BorderPane root = new BorderPane();
        root.setPrefSize(WIDTH, HEIGHT);

        // Top
        initTopHBox(root);

        // Left
        initLeftHBox(root);

        // Center
        initCenterGridPane(root);

        // Right
        VBox rightBox = new VBox();
        rightBox.setPrefWidth(4 * SPACING);
        root.setRight(rightBox);

        // Bottom
        VBox bottomBox = new VBox();
        bottomBox.setPrefHeight(4 * SPACING);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("HELBThermo");
        stage.show();
    }

    private void initCenterGridPane(BorderPane root) {
        GridPane centerGrid = new GridPane();
        centerGrid.setAlignment(Pos.CENTER);
        centerGrid.setHgap(SPACING / 4.0);
        centerGrid.setVgap(SPACING / 4.0);

        if (ThermoController.COLUMN_CELL >= MIN_GRIDPANE_SIZE && ThermoController.COLUMN_CELL <= MAX_GRIDPANE_SIZE) {

            for (int i = 0; i < ThermoController.COLUMN_CELL; i++) {
                for (int j = 0; j < ThermoController.ROW_CELL; j++) {
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

        playButton = new Button();
        playButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/play.png"))));
        HBox.setMargin(playButton, new Insets(0, 0, 0, SPACING / 2)); // Marge à droite de 10 pixels

        pauseButton = new Button();
        pauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/pause.png"))));
        HBox.setMargin(pauseButton, new Insets(0, 0, 0, SPACING / 2)); // Marge à droite de 10 pixels

        resetButton = new Button();
        resetButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/reset.png"))));
        HBox.setMargin(resetButton, new Insets(0, 0, 0, SPACING / 2)); // Marge à droite de 10 pixels

        buttonBox.getChildren().addAll(playButton, pauseButton, resetButton);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(SPACING * 30);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        heatCellsBox = new VBox(); // Utiliser une VBox pour aligner verticalement les boutons
        heatCellsBox.setAlignment(Pos.CENTER); // Centrer le contenu
        heatCellsBox.setSpacing(SPACING); // Espacement vertical entre les boutons

        scrollPane.setContent(heatCellsBox);

        leftBox.getChildren().addAll(buttonBox, scrollPane);

        root.setLeft(leftBox);
    }

    private void initTopHBox(BorderPane root) {
        HBox topBox = new HBox(SPACING);
        topBox.setSpacing(SPACING);
        topBox.setPrefHeight(5 * SPACING);
        topBox.setPrefWidth(10 * SPACING);
        topBox.setAlignment(Pos.CENTER);

        timeBox = new Button("Temps : ");
        timeBox.setPrefWidth(LABEL_SIZE);
        timeBox.setAlignment(Pos.CENTER);

        priceBox = new Button("€ : ");
        priceBox.setPrefWidth(LABEL_SIZE);
        priceBox.setAlignment(Pos.CENTER);

        extTempBox = new Button("T° ext. : ");
        extTempBox.setPrefWidth(LABEL_SIZE);
        extTempBox.setAlignment(Pos.CENTER);

        avgTempBox = new Button("T° moy. : ");
        avgTempBox.setPrefWidth(LABEL_SIZE);
        avgTempBox.setAlignment(Pos.CENTER);

        MenuButton modeMenu = new MenuButton("Chauffe Mode");
        modeMenu.getItems().addAll(new MenuItem("Mode Manuel"), new MenuItem("Mode Successif"), new MenuItem("Mode Target"));
        modeMenu.setPrefSize(LABEL_SIZE, SPACING);
        modeMenu.setAlignment(Pos.CENTER);

        topBox.getChildren().addAll(timeBox, priceBox, extTempBox, avgTempBox, modeMenu);

        root.setTop(topBox);
    }

    public void addHeatCellInBox(String cellId) {
        if (!buttonHeatCellMap.containsKey(cellId)) {
            Button cell = new Button(getCellButton(cellId).getText());
            cell.setStyle("-fx-background-color: #ff0000; ");
            cell.setAlignment(Pos.CENTER);
            cell.setPrefSize(Cell.SIZE * 1.5, Cell.SIZE * 1.5);

            buttonHeatCellMap.put(cellId, cell);
            heatCellsBox.getChildren().add(cell);
        }
    }

    public void setupHeatSourceCell(HeatSourceCell heatSourceCell) {
        String key = "" + heatSourceCell.getX() + heatSourceCell.getY();
        Button cell = buttonCellMap.get(key);
        cell.setText(String.valueOf(heatSourceCell.getTemperature()));
        cell.setStyle("-fx-background-color: #ff0000; ");
    }

    public void setupDeadCell(DeadCell deadCell) {
        String key = "" + deadCell.getX() + deadCell.getY();
        Button cell = buttonCellMap.get(key);
        cell.setText("");
        cell.setStyle("-fx-background-color: #000000; ");
    }

    public void resetCell(Cell cell) {
        String key = "" + cell.getX() + cell.getY();
        Button newCell = buttonCellMap.get(key);
        newCell.setText("");
        newCell.setStyle("");
        if (buttonHeatCellMap.containsKey(key)) {
            heatCellsBox.getChildren().remove(buttonHeatCellMap.get(key));
            buttonHeatCellMap.remove(key);
        }
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getAvgTempBox() {
        return avgTempBox;
    }

    public Button getExtTempBox() {
        return extTempBox;
    }

    public Button getPriceBox() {
        return priceBox;
    }

    public Button getTimeBox() {
        return timeBox;
    }


    public Button getCellButton(String cellId) {
        return buttonCellMap.get(cellId);
    }
}
