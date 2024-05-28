package com.example.helbthermo;

import java.util.HashMap;

// Représente le mode de chauffe target
public class ThermoSystemTarget extends ThermoSystemManual implements SimulationSystem {

    // Attributs de la classe
    private final double targetTemperature = 20.0;
    private double avgTemp;

    // Méthode qui gere le comportement de la simulation du system
    @Override
    public void simulation(HashMap<String, Cell> cells) {
        super.simulation(cells);
        int deadCells = 0;

        for (Cell c : cells.values()) {
            if (c instanceof DeadCell) {
                deadCells++;
            }
            avgTemp += c.getTemperature();
        }

        avgTemp /= cells.size() - deadCells;

        for (Cell c : cells.values()) {
            if (c instanceof HeatSourceCell) {
                HeatSourceCell heatSourceCell = (HeatSourceCell) c;
                if (avgTemp < targetTemperature) {
                    heatSourceCell.activate();
                } else {
                    heatSourceCell.deactivate();
                }
                heatSourceCell.notifyObserver();
            }
        }
    }

}
