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
    private static final int HEIGHT = 300;
    private static final int WIDTH = 230;
    private static final int SPACING = 20;

    private static boolean isModificationCanceled = false;

    private static boolean isDeadCellCheck = false;
    private static boolean isHeatSourceCheck = false;
    private static String[] typeAndTemperatureReturn;

    public static String[] display(Cell cell) {
        String temperatureCell = new DecimalFormat("#.##").format(cell.getTemperature());
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
                        typeAndTemperatureReturn[0] = HeatSourceCell.class.getName();
                        typeAndTemperatureReturn[1] = temperatureInput.getText();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("temperature is not set");
                        alert.showAndWait();
                    }
                } else if (!heatSourceCheckBox.isSelected() && deadCellCheckBox.isSelected()) {
                    typeAndTemperatureReturn[0] = DeadCell.class.getName();
                } else if (!(deadCellCheckBox.isSelected() && heatSourceCheckBox.isSelected())) {
                    typeAndTemperatureReturn[0] = Cell.class.getName();
                }
                window.close();
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
