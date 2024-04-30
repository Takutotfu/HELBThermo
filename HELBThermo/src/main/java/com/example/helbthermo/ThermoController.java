package com.example.helbthermo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class ThermoController {

    @FXML
    private TextField currentTempField;

    @FXML
    private Slider targetTempSlider;

    @FXML
    private Label targetTempLabel;

    private double currentTemp = 20.0; // Température actuelle (exemple)
    private double targetTemp = 20.0; // Température cible (exemple)

    public void initialize() {
        // Initialiser les valeurs des champs
        currentTempField.setText(currentTemp + "°C");
        targetTempSlider.setValue(targetTemp);
        targetTempLabel.setText(targetTemp + "°C");

        // Ajouter un écouteur sur le curseur pour mettre à jour la température cible
        targetTempSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            targetTemp = newValue.doubleValue();
            targetTempLabel.setText(String.format("%.1f°C", targetTemp));
        });
    }

    @FXML
    public void increaseTemp() {
        currentTemp++;
        currentTempField.setText(currentTemp + "°C");
    }

    @FXML
    public void decreaseTemp() {
        currentTemp--;
        currentTempField.setText(currentTemp + "°C");
    }
}
