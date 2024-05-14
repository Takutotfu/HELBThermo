package com.example.helbthermo;

import java.util.HashMap;

public class ThermoSystem {

    private static final int maxCellNeighbors = 9;

    private static int outOfRangeCellNbr = 0;
    private static int deadCellNbr = 0;

    public static int timer = 0;
    public static int cost = 0;
    public static double avgTemp = 0;

    public static void simulation(HashMap<String, Cell> cells) {
        double newTemp;

        timer++;
        avgTemp = 0;
        cost = 0;

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
                HeatSourceCell heatSourceCell = (HeatSourceCell) cell;
                if (heatSourceCell.isActivated()) {
                    newTemp = heatSourceCell.getHeatTemperature();
                } else {
                    cell.notifyObserver();

                }
                cost += (int) (timer * (cell.getTemperature() * cell.getTemperature()));
            }

            cell.setTemperature(newTemp);

            if (!(cell instanceof HeatSourceCell)) {
                cell.notifyObserver();

            }

            avgTemp += newTemp;
        }

        avgTemp /= ThermoController.COLUMN_CELL * ThermoController.ROW_CELL;
    }

    private static double getTemperature(HashMap<String, Cell> cells, int x, int y) {
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

    public static void resetSimulation() {
        timer = 0;
        cost = 0;
    }

}
