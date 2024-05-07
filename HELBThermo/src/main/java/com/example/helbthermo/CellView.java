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

public class CellView {
    private static final int HEIGHT = 300;
    private static final int WIDTH = 230;
    private static final int SPACING = 20;

    private static boolean isModificationCanceled = false;

    private static boolean isDeadCellCheck = false;
    private static boolean isHeatSourceCheck = false;
    private static String temperatureCell;

    private static Cell newCell;

    public static Cell display(Cell cell) {
        // TODO : DeadCell
        if (cell instanceof DeadCell) {
            isDeadCellCheck = true;
        }
        if (cell instanceof HeatSourceCell) {
            isHeatSourceCheck = true;
        }
        temperatureCell = String.valueOf(cell.getTemperature());
        newCell = cell;

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); //focus sur la fenetre
        window.setTitle("Cell settings ("+cell.getX() + ";" + cell.getY() + ")");
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

        HBox temperatureBox = new HBox();
        temperatureBox.getChildren().addAll(temperatureInput, temperatureLabel);
        temperatureBox.setAlignment(Pos.CENTER);

        Button setButton = new Button("Enter");
        setButton.setAlignment(Pos.CENTER);

        setButton.setOnAction(e -> {
            // TODO : Enregistrer les valeurs modifiées
            isModificationCanceled = ConfirmModificationView.display();
            if (isModificationCanceled) {
                if (heatSourceCheckBox.isSelected()) {
                    if (!temperatureInput.getText().isEmpty())
                        newCell = new HeatSourceCell(cell.getX(), cell.getY(), Integer.parseInt(temperatureInput.getText()));
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("temperature is not set");
                        alert.showAndWait();
                    }
                } else if (!heatSourceCheckBox.isSelected() && deadCellCheckBox.isSelected()) {
                    newCell = new DeadCell(cell.getX(), cell.getY());
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

        return newCell;
    }
}
