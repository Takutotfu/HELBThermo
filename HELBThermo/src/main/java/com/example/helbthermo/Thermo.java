package com.example.helbthermo;

import java.text.DecimalFormat;
import java.util.HashMap;

// TODO : Optimize simulation (delete view, optimize function) mb add observer for information button
public class Thermo {
    public static final int TEMP_EXT = 3; //°C

    private static int timer = 0;
    private static int cost = 0;
    private static double avgTemp = 0;

    public static void simulation(ThermoView view, HashMap<String, Cell> cells) {
        cost = 0;
        avgTemp = 0;

        double newTemp;

        timer++;
        view.setTimeBox("Temps : " + timer + "sec");

        for (Cell cell : cells.values()) {
            if (!(cell instanceof DeadCell)) {
                int x = cell.getX();
                int y = cell.getY();
                newTemp = cell.getTemperature();

                String leftCellKey = "" + cell.getX() + (cell.getY() - 1);
                String rightCellKey = "" + cell.getX() + (cell.getY() + 1);
                String topCellKey = "" + (cell.getX() - 1) + cell.getY();
                String bottomCellKey = "" + (cell.getX() + 1) + cell.getY();
                String topLeftCellKey = "" + (cell.getX() - 1) + (cell.getY() - 1);
                String topRightCellKey = "" + (cell.getX() - 1) + (cell.getY() + 1);
                String bottomLeftCellKey = "" + (cell.getX() + 1) + (cell.getY() - 1);
                String bottomRightCellKey = "" + (cell.getX() + 1) + (cell.getY() + 1);

                if (x == 0 && y != 0 && y != ThermoController.COLUMN_CELL - 1) {
                    // En haut ] les 2 coins

                    newTemp = (newTemp
                            + cells.get(leftCellKey).getTemperature()
                            + cells.get(rightCellKey).getTemperature()
                            + cells.get(bottomCellKey).getTemperature()
                            + cells.get(bottomLeftCellKey).getTemperature()
                            + cells.get(bottomRightCellKey).getTemperature()
                            + (TEMP_EXT * (ThermoController.COLUMN_CELL - 2))) / 9;
                } else if (x == ThermoController.ROW_CELL - 1 && y != 0 && y != ThermoController.COLUMN_CELL - 1) {
                    // En bas ] les 2 coins

                    newTemp = (newTemp
                            + cells.get(rightCellKey).getTemperature()
                            + cells.get(leftCellKey).getTemperature()
                            + cells.get(topCellKey).getTemperature()
                            + cells.get(topRightCellKey).getTemperature()
                            + cells.get(topLeftCellKey).getTemperature()
                            + (TEMP_EXT * (ThermoController.COLUMN_CELL - 2))) / 9;
                } else if (y == ThermoController.COLUMN_CELL - 1 && x != 0 && x != ThermoController.ROW_CELL - 1) {
                    // A droite ] les 2 coins

                    newTemp = (newTemp
                            + cells.get(leftCellKey).getTemperature()
                            + cells.get(topCellKey).getTemperature()
                            + cells.get(topLeftCellKey).getTemperature()
                            + cells.get(bottomCellKey).getTemperature()
                            + cells.get(bottomLeftCellKey).getTemperature()
                            + (TEMP_EXT * (ThermoController.ROW_CELL - 2))) / 9;
                } else if (y == 0 && x != 0 && x != ThermoController.ROW_CELL - 1) {
                    // A gauche ] les 2 coins

                    newTemp = (newTemp
                            + cells.get(rightCellKey).getTemperature()
                            + cells.get(topCellKey).getTemperature()
                            + cells.get(topRightCellKey).getTemperature()
                            + cells.get(bottomCellKey).getTemperature()
                            + cells.get(bottomRightCellKey).getTemperature()
                            + (TEMP_EXT * (ThermoController.ROW_CELL - 2))) / 9;
                } else if (x == 0 && y == 0) { // Haut gauche

                    newTemp = (newTemp
                            + cells.get(rightCellKey).getTemperature()
                            + cells.get(bottomCellKey).getTemperature()
                            + cells.get(bottomRightCellKey).getTemperature()
                            + (TEMP_EXT * 5)) / 9;
                } else if (x == 0 && y == ThermoController.COLUMN_CELL - 1) { // Haut droite

                    newTemp = (newTemp
                            + cells.get(leftCellKey).getTemperature()
                            + cells.get(bottomCellKey).getTemperature()
                            + cells.get(bottomLeftCellKey).getTemperature()
                            + (TEMP_EXT * 5)) / 9;
                } else if (x == ThermoController.ROW_CELL - 1 && y == 0) { // Bas gauche

                    newTemp = (newTemp
                            + cells.get(topCellKey).getTemperature()
                            + cells.get(topRightCellKey).getTemperature()
                            + cells.get(rightCellKey).getTemperature()
                            + (TEMP_EXT * 5)) / 9;
                } else if (x == ThermoController.ROW_CELL - 1 && y == ThermoController.COLUMN_CELL - 1) { // Bas droite

                    newTemp = (newTemp
                            + cells.get(topCellKey).getTemperature()
                            + cells.get(topLeftCellKey).getTemperature()
                            + cells.get(leftCellKey).getTemperature()
                            + (TEMP_EXT * 5)) / 9;
                } else {
                    newTemp = (newTemp
                            + cells.get(topCellKey).getTemperature()
                            + cells.get(topLeftCellKey).getTemperature()
                            + cells.get(topRightCellKey).getTemperature()
                            + cells.get(bottomCellKey).getTemperature()
                            + cells.get(bottomRightCellKey).getTemperature()
                            + cells.get(bottomLeftCellKey).getTemperature()
                            + cells.get(rightCellKey).getTemperature()
                            + cells.get(leftCellKey).getTemperature()) / 9;
                }

                if (cell instanceof HeatSourceCell) {
                    HeatSourceCell heatSourceCell = (HeatSourceCell) cell;

                    if (heatSourceCell.isActivated()) {
                        newTemp = heatSourceCell.getHeatTemperature();
                    }

                    cost += (int) (timer * (cell.getTemperature()*cell.getTemperature()));
                }

                cell.setTemperature(newTemp);

                if (!(cell instanceof HeatSourceCell)) {
                    cell.notifyObserver();
                }

                avgTemp += newTemp;
            }
        }

        view.setPriceBox("€ : " + cost + "€");

        avgTemp = avgTemp / (ThermoController.COLUMN_CELL * ThermoController.ROW_CELL);
        view.setAvgTempBox("T° moy. : " + new DecimalFormat("#.##").format(avgTemp) + "°C");
    }

    public static void resetSimulation() {
        timer = 0;
        cost = 0;
    }

}
