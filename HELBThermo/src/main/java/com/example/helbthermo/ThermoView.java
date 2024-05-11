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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.HashMap;

public class ThermoView {
    private final int height = 720;
    private final int width = 1280;
    private final int spacing = 20;
    private final int labelSize = 150;
    private final int maxGridpaneSize = 12;
    private final int minGridpaneSize = 3;

    private final HashMap<String, Button> buttonCellMap;
    private final HashMap<String, Button> buttonHeatCellMap;
    private final Stage stage;

    private VBox heatCellsBox;
    private Button playButton, pauseButton, resetButton;
    private Button timeBox, priceBox, extTempBox, avgTempBox;

    private int heatSourceNbr;

    public ThermoView(Stage stage) {
        this.stage = stage;
        this.buttonCellMap = new HashMap<>();
        this.buttonHeatCellMap = new HashMap<>();
        this.heatSourceNbr = 0;
    }

    public void initView() {
        BorderPane root = new BorderPane();
        root.setPrefSize(width, height);

        // Top
        initTopHBox(root);

        // Left
        initLeftHBox(root);

        // Center
        initCenterGridPane(root);

        // Right
        VBox rightBox = new VBox();
        rightBox.setPrefWidth(4 * spacing);
        root.setRight(rightBox);

        // Bottom
        VBox bottomBox = new VBox();
        bottomBox.setPrefHeight(4 * spacing);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("HELBThermo");
        stage.show();
    }

    private void initCenterGridPane(BorderPane root) {
        GridPane centerGrid = new GridPane();
        centerGrid.setAlignment(Pos.CENTER);
        centerGrid.setHgap(spacing / 4.0);
        centerGrid.setVgap(spacing / 4.0);

        if (ThermoController.COLUMN_CELL >= minGridpaneSize && ThermoController.COLUMN_CELL <= maxGridpaneSize
                && ThermoController.ROW_CELL >= minGridpaneSize && ThermoController.ROW_CELL <= maxGridpaneSize) {

            for (int i = 0; i < ThermoController.ROW_CELL; i++) {
                for (int j = 0; j < ThermoController.COLUMN_CELL; j++) {

                    Button button = new Button();
                    button.setPrefSize(Cell.SIZE, Cell.SIZE);
                    buttonCellMap.put("" + i + j, button);
                    centerGrid.add(button, j, i);

                    // Corner heat source
                    if ((i == 0 && j == 0)
                            || (i == 0 && j == ThermoController.COLUMN_CELL - 1)
                            || (i == ThermoController.ROW_CELL - 1 && j == 0)
                            || (i == ThermoController.ROW_CELL - 1 && j == ThermoController.COLUMN_CELL - 1)) {
                        setupHeatCell("" + i + j, Thermo.TEMP_EXT);
                    }
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
        leftBox.setPrefWidth(10 * spacing);
        leftBox.setSpacing(spacing);

        // Ajout des marges à gauche et à droite
        BorderPane.setMargin(leftBox, new Insets(0, spacing, 0, 2 * spacing));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        playButton = new Button();
        playButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/play.png"))));
        HBox.setMargin(playButton, new Insets(0, 0, 0, spacing / 2)); // Marge à droite de 10 pixels

        pauseButton = new Button();
        pauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/pause.png"))));
        HBox.setMargin(pauseButton, new Insets(0, 0, 0, spacing / 2)); // Marge à droite de 10 pixels

        resetButton = new Button();
        resetButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/reset.png"))));
        HBox.setMargin(resetButton, new Insets(0, 0, 0, spacing / 2)); // Marge à droite de 10 pixels

        buttonBox.getChildren().addAll(playButton, pauseButton, resetButton);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(spacing * 30);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        heatCellsBox = new VBox();
        heatCellsBox.setAlignment(Pos.CENTER);
        heatCellsBox.setSpacing(spacing);

        scrollPane.setContent(heatCellsBox);

        leftBox.getChildren().addAll(buttonBox, scrollPane);

        root.setLeft(leftBox);
    }

    private void initTopHBox(BorderPane root) {
        HBox topBox = new HBox(spacing);
        topBox.setSpacing(spacing);
        topBox.setPrefHeight(5 * spacing);
        topBox.setPrefWidth(10 * spacing);
        topBox.setAlignment(Pos.CENTER);

        timeBox = new Button("Temps : 0sec");
        timeBox.setPrefWidth(labelSize);
        timeBox.setAlignment(Pos.CENTER);

        priceBox = new Button("€ : 0€");
        priceBox.setPrefWidth(labelSize);
        priceBox.setAlignment(Pos.CENTER);

        extTempBox = new Button("T° ext. : 0°C");
        extTempBox.setPrefWidth(labelSize);
        extTempBox.setAlignment(Pos.CENTER);

        avgTempBox = new Button("T° moy. : 0°C");
        avgTempBox.setPrefWidth(labelSize);
        avgTempBox.setAlignment(Pos.CENTER);

        MenuButton modeMenu = new MenuButton("Chauffe Mode");
        modeMenu.getItems().addAll(new MenuItem("Mode Manuel"), new MenuItem("Mode Successif"), new MenuItem("Mode Target"));
        modeMenu.setPrefSize(labelSize, spacing);
        modeMenu.setAlignment(Pos.CENTER);

        topBox.getChildren().addAll(timeBox, priceBox, extTempBox, avgTempBox, modeMenu);

        root.setTop(topBox);
    }

    public void setupHeatCell(String cellId, double temp) {
        Button buttonCell = buttonCellMap.get(cellId);

        buttonCell.setText("s" + new DecimalFormat("#.##").format(temp));
        buttonCell.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));

        if (!buttonHeatCellMap.containsKey(cellId)) {
            Button heatCellButton = new Button(getCellButton(cellId).getText());
            heatCellButton.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
            heatCellButton.setAlignment(Pos.CENTER);
            heatCellButton.setPrefSize(Cell.SIZE * 1.5, Cell.SIZE * 1.5);

            buttonHeatCellMap.put(cellId, heatCellButton);
            heatCellsBox.getChildren().add(heatCellButton);
        } else {
            buttonHeatCellMap.get(cellId).setText(getCellButton(cellId).getText());
            buttonHeatCellMap.get(cellId).setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
        }
    }

    public void unableHeatSourceCell(String cellId, double temp) {
        Button buttonHeatCell = buttonHeatCellMap.get(cellId);
        Button cell = buttonCellMap.get(cellId);

        buttonHeatCell.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
        cell.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
    }

    public void disableHeatSourceCell(String cellId) {
        Button buttonHeatCell = buttonHeatCellMap.get(cellId);
        Button cell = buttonCellMap.get(cellId);

        buttonHeatCell.setStyle("-fx-background-color: #4c4c4c; ");
        cell.setStyle("-fx-background-color: #4c4c4c; ");
    }

    public void setupDeadCell(String key) {
        Button cellButton = buttonCellMap.get(key);
        cellButton.setText("");
        cellButton.setStyle("-fx-background-color: #000000; ");
    }

    public void resetCell(String key) {
        Button newCell = buttonCellMap.get(key);
        newCell.setText("");
        newCell.setStyle("");
        if (buttonHeatCellMap.containsKey(key)) {
            heatCellsBox.getChildren().remove(buttonHeatCellMap.get(key));
            buttonHeatCellMap.remove(key);
        }
    }

    public void resetView() {
        for (String key : buttonCellMap.keySet()) {
            resetCell(key);
        }

        timeBox.setText("Temps : 0sec");
        avgTempBox.setText("T° moy. : 0°C");
        priceBox.setText("€ : 0€");
    }

    public void updateCell(String cellId, double temp) {
        Button cell = buttonCellMap.get(cellId);
        cell.setText(new DecimalFormat("#.##").format(temp));
        cell.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
    }

    private Color getColorFromIntensity(double intensity) {
        intensity = intensity / 100.0;
        double redIntensity = ((1 - intensity) * 255);
        return Color.rgb(255, (int) redIntensity, (int) redIntensity);
    }

    private String toRGBCode(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public Button getPlayButton() {return playButton;}

    public Button getPauseButton() {return pauseButton;}

    public Button getResetButton() {return resetButton;}

    public Button getAvgTempBox() {return avgTempBox;}

    public Button getExtTempBox() {return extTempBox;}

    public Button getPriceBox() {return priceBox;}

    public Button getTimeBox() {return timeBox;}

    public Button getCellButton(String cellId) {return buttonCellMap.get(cellId);}

    public Button getHeatCellButton(String cellId) {return buttonHeatCellMap.get(cellId);}
}
