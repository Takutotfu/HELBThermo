package com.example.helbthermo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.DecimalFormat;

// TODO : Comment code
public class CellView {
    public static final int TYPE_INDEX = 0;
    public static final int TEMP_INDEX = 1;

    private static final int HEIGHT = 300;
    private static final int WIDTH = 230;
    private static final int SPACING = 20;
    private static final double MAX_TEMP = 100.0;
    private static final double MIN_TEMP = 0.0;

    private static boolean isModificationCanceled = false;
    private static boolean answer = true;

    private static boolean isDeadCellCheck = false;
    private static boolean isHeatSourceCheck = false;
    private static String[] typeAndTemperatureReturn;

    public static String[] display(Cell cell) {
        String temperatureCell = new DecimalFormat(ThermoView.NUMBER_FORMAT).format(cell.getTemperature());
        typeAndTemperatureReturn = new String[]{cell.getClass().getName(), temperatureCell};

        if (cell instanceof DeadCell) {
            isDeadCellCheck = true;
            isHeatSourceCheck = false;
        }
        if (cell instanceof HeatSourceCell) {
            isHeatSourceCheck = true;
            isDeadCellCheck = false;
        }

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); //focus sur la fenetre
        window.setTitle("Cell settings (" + cell.getX() + ";" + cell.getY() + ")");
        window.setMinWidth(WIDTH);
        window.setMinHeight(HEIGHT);
        window.setResizable(false);

        BorderPane root = new BorderPane();
        root.setPrefSize(WIDTH, HEIGHT);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        CheckBox deadCellCheckBox = new CheckBox(" : Set Dead Cell");
        deadCellCheckBox.setSelected(isDeadCellCheck);
        deadCellCheckBox.setAlignment(Pos.CENTER);

        CheckBox heatSourceCheckBox = new CheckBox(" : Set Heat Source");
        heatSourceCheckBox.setSelected(isHeatSourceCheck);
        heatSourceCheckBox.setAlignment(Pos.CENTER);

        TextField temperatureInput = new TextField();
        temperatureInput.setText(temperatureCell);
        temperatureInput.setPrefWidth(45);
        temperatureInput.setAlignment(Pos.CENTER_RIGHT);


        Label temperatureLabel = new Label(" : Set Temperature");
        temperatureLabel.setAlignment(Pos.CENTER_LEFT);

        deadCellCheckBox.setOnAction(e -> {
            isDeadCellCheck = true;
            isHeatSourceCheck = false;
            heatSourceCheckBox.setSelected(false);
            temperatureInput.setDisable(true);
        });

        heatSourceCheckBox.setOnAction(e -> {
            isHeatSourceCheck = true;
            isDeadCellCheck = false;
            deadCellCheckBox.setSelected(false);
            temperatureInput.setDisable(false);
        });

        HBox temperatureBox = new HBox();
        temperatureBox.getChildren().addAll(temperatureInput, temperatureLabel);
        temperatureBox.setAlignment(Pos.CENTER);

        Button setButton = new Button("Enter");
        setButton.setAlignment(Pos.CENTER);

        setButton.setOnAction(e -> {
            isModificationCanceled = ConfirmModificationView.display();
            if (isModificationCanceled) {
                if (heatSourceCheckBox.isSelected()) {
                    if (!temperatureInput.getText().isEmpty()) {
                        if (temperatureInput.getText().matches("\\d+(\\.\\d+)?")) {
                            double temperature = Double.parseDouble(temperatureInput.getText());
                            if (temperature <= MAX_TEMP && temperature >= MIN_TEMP) {
                                typeAndTemperatureReturn[TYPE_INDEX] = HeatSourceCell.class.getName();
                                typeAndTemperatureReturn[TEMP_INDEX] = String.valueOf(temperature);
                                answer = true;
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("temperature must be between 0.0 and 100.0");
                                alert.showAndWait();
                                answer = false;
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("temperature must be between 0.0 and 100.0");
                            alert.showAndWait();
                            answer = false;
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("temperature is not set");
                        alert.showAndWait();
                        answer = false;
                    }
                } else if (!heatSourceCheckBox.isSelected() && deadCellCheckBox.isSelected()) {
                    typeAndTemperatureReturn[TYPE_INDEX] = DeadCell.class.getName();
                    answer = true;
                } else if (!(deadCellCheckBox.isSelected() && heatSourceCheckBox.isSelected())) {
                    typeAndTemperatureReturn[TYPE_INDEX] = Cell.class.getName();
                    answer = true;
                }

                if (answer) {
                    window.close();
                }
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(deadCellCheckBox, heatSourceCheckBox, temperatureBox, setButton);
        BorderPane.setMargin(vBox, new Insets(SPACING, SPACING, SPACING, SPACING));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(SPACING);

        root.setCenter(vBox);

        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();

        return typeAndTemperatureReturn;
    }
}
