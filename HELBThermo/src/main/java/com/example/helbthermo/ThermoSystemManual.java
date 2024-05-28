package com.example.helbthermo;

import java.util.HashMap;

// Représente le mode de chauffe manuel
public class ThermoSystemManual implements SimulationSystem {

    // Attributs de la classe
    private final int maxCellNeighbors = 9;

    private int outOfRangeCellNbr = 0;
    private int deadCellNbr = 0;

    // Méthode qui gere le comportement de la simulation du system
    @Override
    public void simulation(HashMap<String, Cell> cells) {
        double newTemp;

        for (Cell cell : cells.values()) {
            int x = cell.getX();
            int y = cell.getY();
            outOfRangeCellNbr = 0;
            deadCellNbr = 0;

            newTemp = cell.getTemperature();
            newTemp += getTemperature(cells, x - 1, y); // Top
            newTemp += getTemperature(cells, x + 1, y); // Bottom
            newTemp += getTemperature(cells, x, y - 1); // Left
            newTemp += getTemperature(cells, x, y + 1); // Right
            newTemp += getTemperature(cells, x - 1, y - 1); // Top Left
            newTemp += getTemperature(cells, x - 1, y + 1); // Top right
            newTemp += getTemperature(cells, x + 1, y - 1); // Bottom left
            newTemp += getTemperature(cells, x + 1, y + 1); // Bottom Right
            newTemp += (ThermoController.tempExt * outOfRangeCellNbr); // Ext Cell
            newTemp /= maxCellNeighbors - deadCellNbr; // DeadCells

            if (cell instanceof HeatSourceCell) {
                if (((HeatSourceCell) cell).isActivated()) newTemp = cell.getTemperature();
            }

            cell.setTemperature(newTemp);
            cell.notifyObserver();
        }
    }

    // Méthode qui calcule la nouvelle température de chaque cellules
    private double getTemperature(HashMap<String, Cell> cells, int x, int y) {
        String cellKey = x + "" + y;
        Cell neighborCell = cells.get(cellKey);
        if (neighborCell != null) {
            if (neighborCell instanceof DeadCell) {
                deadCellNbr++;
            }
            return neighborCell.getTemperature();
        } else {
            outOfRangeCellNbr++;
            return ThermoController.tempExt;
        }
    }

}
