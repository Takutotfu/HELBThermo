package com.example.helbthermo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CellFactory {
    // Attributs de configuration
    private final int maxDeadCellsNbr = 3;

    // Attributs de la classe
    private final ThermoView view;

    private List<HeatSourceCell> heatSourceCells = new ArrayList<>();

    // Constructeur de la classe
    public CellFactory(ThermoView view) {
        this.view = view;
    }

    // Méthode pour créer les cellules de l'applications
    public HashMap<String, Cell> createCells() throws Exception {
        HashMap<String, Cell> cellsMap = new HashMap<>();
        Cell newCell;
        int centreX = ThermoController.ROW_CELL / 2;
        int centreY = ThermoController.COLUMN_CELL / 2;

        for (int i = 0; i < ThermoController.ROW_CELL; i++) {
            for (int j = 0; j < ThermoController.COLUMN_CELL; j++) {
                newCell = create(Cell.class.getName(), i, j, 0.0);
                if ((i == 0 && j == 0)
                        || (i == 0 && j == ThermoController.COLUMN_CELL - 1)
                        || (i == ThermoController.ROW_CELL - 1 && j == 0)
                        || (i == ThermoController.ROW_CELL - 1 && j == ThermoController.COLUMN_CELL - 1)
                        || (ThermoController.ROW_CELL % 2 != 0 && ThermoController.COLUMN_CELL % 2 != 0
                        && i == centreX && j == centreY)) {
                    newCell = create(HeatSourceCell.class.getName(), i, j, ThermoController.ORIGINAL_HEAT_SOURCE_TEMPERATURE);
                    heatSourceCells.add((HeatSourceCell) newCell);
                }
                cellsMap.put("" + i + j, newCell);
            }
        }

        createDeadCells(cellsMap);

        return cellsMap;
    }

    // Méthodes pour créer les cellules mortes de facon aléatoires avec certains facteurs impactant
    private void createDeadCells(HashMap<String, Cell> cellsMap) throws Exception {
        Random random = new Random();
        int centreX = ThermoController.ROW_CELL / 2;
        int centreY = ThermoController.COLUMN_CELL / 2;
        int deadCellsNbr = 0;

        for (int i = 0; i < ThermoController.ROW_CELL; i++) {
            for (int j = 0; j < ThermoController.COLUMN_CELL; j++) {
                Cell cell = cellsMap.get("" + i + j);

                if (cell instanceof HeatSourceCell) continue; // On exclue les sources de chaleur
                if (deadCellsNbr > maxDeadCellsNbr - 1) break; // On arrête une fois le nombre atteint

                // On calcule la distance par rapport au centre
                double distanceFromCenter = Math.sqrt(Math.pow(centreX - i, 2) + Math.pow(centreY - j, 2));

                // On calcule la distance de la source de chaleur la plus proche
                double minDistanceFromHeatSource = Double.MAX_VALUE;
                for (HeatSourceCell heatSourceCell : heatSourceCells) {
                    double distance = Math.sqrt(Math.pow(heatSourceCell.getX() - i, 2) + Math.pow(heatSourceCell.getY() - j, 2));
                    if (distance < minDistanceFromHeatSource) {
                        minDistanceFromHeatSource = distance;
                    }
                }

                // Calcule de la probabilité d'être une cellule morte en fonction de la distance du centre
                // ainsi que de la distance de la source de chaleur la plus proche
                double probability = (1 - (distanceFromCenter / Math.max(centreX, centreY))) * (1 - (minDistanceFromHeatSource / Math.max(ThermoController.ROW_CELL, ThermoController.COLUMN_CELL)));
                if (random.nextDouble() < probability) {
                    cellsMap.put("" + i + j, create(DeadCell.class.getName(), i, j, 0.0));
                    deadCellsNbr++;
                }
            }
        }
    }

    // méthode pour créer une cellule en fonction de son type
    public Cell create(String cellType, int x, int y, double temperature) throws Exception {
        String key = "" + x + y;

        if (cellType.equals(Cell.class.getName())) {
            Cell cell = new Cell(x, y);
            cell.attach(view);

            cell.notifyObserver();

            return cell;
        } else if (cellType.equals(DeadCell.class.getName())) {
            DeadCell deadCell = new DeadCell(x, y);
            deadCell.attach(view);

            deadCell.notifyObserver();

            return deadCell;
        } else if (cellType.equals(HeatSourceCell.class.getName())) {
            HeatSourceCell heatSourceCell = new HeatSourceCell(x, y, temperature);
            heatSourceCell.attach(view);

            heatSourceCell.notifyObserver();

            // setAction on HeatCellButton
            view.getHeatCellButton(key).setOnAction(e -> {
                if (!heatSourceCell.isActivated()) {
                    heatSourceCell.activate();
                } else {
                    heatSourceCell.deactivate();
                }
                heatSourceCell.notifyObserver();
            });

            return heatSourceCell;
        } else {
            throw new Exception("Invalid cell type");
        }
    }
}
