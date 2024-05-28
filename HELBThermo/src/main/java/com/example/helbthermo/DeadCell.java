package com.example.helbthermo;

// Modele representant une cellule morte du system
public class DeadCell extends Cell{

    // Constructeur
    public DeadCell(int x, int y) {
        super(x, y);
        setTemperature(0);
    }

    // Securité pour ne pas changé de temperature
    @Override
    public void setTemperature(double temperature) {
        super.setTemperature(0.0);
    }
}
