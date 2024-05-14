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

public class ThermoView implements Observer {
    private final int height = 720;
    private final int width = 1280;
    private final int spacing = 20;
    private final int labelSize = 150;
    private final int maxGridpaneSize = 12;
    private final int minGridpaneSize = 3;
    private final int rgbMaxValue = 255;

    private final double cellSize = 100.0;

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
                    button.setPrefSize(cellSize, cellSize);
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

    private void createHeatCell(String cellId, double temp) {
        if (!buttonHeatCellMap.containsKey(cellId)) {
            Button cell = buttonCellMap.get(cellId);
            cell.setText("s" + (buttonHeatCellMap.size() + 1) + " : " + temp);
            cell.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
            Button button = new Button(buttonCellMap.get(cellId).getText());
            button.setStyle(buttonCellMap.get(cellId).getStyle());
            button.setPrefSize(cellSize*1.5, cellSize*1.5);
            buttonHeatCellMap.put(cellId, button);
            heatCellsBox.getChildren().add(button);
        }
    }

    private void reOrderHeatCell() {
        if (!buttonHeatCellMap.isEmpty()) {
            int cpt = 0;
            for (String id : buttonHeatCellMap.keySet()) {
                cpt++;
                String text = buttonHeatCellMap.get(id).getText();
                double temperature = Double.parseDouble(text.substring(text.indexOf(':')+1));

                buttonHeatCellMap.get(id).setText("s" + cpt + " : " + temperature);
                buttonCellMap.get(id).setText("s" + cpt + " : " + temperature);
            }
        }
    }

    private void updateHeatCell(String cellId, double temperature) {
        Button cell = buttonCellMap.get(cellId);
        String text = cell.getText().substring(0, cell.getText().indexOf(':')+1);
        cell.setText(text + " " + new DecimalFormat("#.##").format(temperature));
        System.out.println(text + " " + new DecimalFormat("#.##").format(temperature));
    }

    private void removeHeatCell(String cellId) {
        if (buttonHeatCellMap.containsKey(cellId)) {
            heatCellsBox.getChildren().remove(buttonHeatCellMap.get(cellId));
            buttonHeatCellMap.remove(cellId);
            reOrderHeatCell();
        }
    }

    public void enableHeatSourceCell(String cellId, double temp) {
        Button buttonHeatCell = buttonHeatCellMap.get(cellId);
        Button cell = buttonCellMap.get(cellId);

        buttonHeatCell.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
        cell.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
        cell.setText(buttonHeatCell.getText());
    }

    public void disableHeatSourceCell(String cellId) {
        Button buttonHeatCell = buttonHeatCellMap.get(cellId);
        Button cell = buttonCellMap.get(cellId);

        buttonHeatCell.setStyle("-fx-background-color: #4c4c4c; ");
        cell.setStyle("-fx-background-color: #4c4c4c; ");
    }

    private void createDeadCell(String key) {
        Button cellButton = buttonCellMap.get(key);
        cellButton.setText("");
        cellButton.setStyle("-fx-background-color: #000000; ");
    }

    public void resetView() {
        timeBox.setText("Temps : 0sec");
        priceBox.setText("€ : 0€");
    }

    private void updateCell(String cellId, double temp) {
        Button cell = buttonCellMap.get(cellId);
        cell.setText(new DecimalFormat("#.##").format(temp));
        cell.setStyle("-fx-background-color: " + toRGBCode(getColorFromIntensity(temp)));
    }

    private Color getColorFromIntensity(double intensity) {
        intensity = intensity / 100.0;
        double redIntensity = ((1 - intensity) * rgbMaxValue);
        return Color.rgb(rgbMaxValue, (int) redIntensity, (int) redIntensity);
    }

    private String toRGBCode(Color color) {
        int r = (int) (color.getRed() * rgbMaxValue);
        int g = (int) (color.getGreen() * rgbMaxValue);
        int b = (int) (color.getBlue() * rgbMaxValue);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    @Override
    public void update(Object o) {
        if (o instanceof HeatSourceCell) {
            HeatSourceCell heatSourceCell = (HeatSourceCell) o;
            if (!heatSourceCell.isActivated()) {
                updateHeatCell(heatSourceCell.getId(), heatSourceCell.getTemperature());
            } else {
                createHeatCell(heatSourceCell.getId(), heatSourceCell.getTemperature());
                reOrderHeatCell();
            }

            System.out.println("Reorder heat cell");
        } else if (o instanceof DeadCell) {
            DeadCell deadCell = (DeadCell) o;
            removeHeatCell(deadCell.getId());
            createDeadCell(deadCell.getId());
        } else {
            Cell cell = (Cell) o;
            removeHeatCell(cell.getId());
            updateCell(cell.getId(), cell.getTemperature());
        }
    }

    // setter
    public void setTimeBox(String text) {timeBox.setText(text);}
    public void setPriceBox(String text) {priceBox.setText(text);}
    public void setExtTempBox(String text) {extTempBox.setText(text);}
    public void setAvgTempBox(String text) {avgTempBox.setText(text);}

    // getter
    public Button getPlayButton() {return playButton;}
    public Button getPauseButton() {return pauseButton;}
    public Button getResetButton() {return resetButton;}

    public Stage getStage() {return stage;}

    public Button getCellButton(String cellId) {return buttonCellMap.get(cellId);}
    public Button getHeatCellButton(String cellId) {return buttonHeatCellMap.get(cellId);}
}
