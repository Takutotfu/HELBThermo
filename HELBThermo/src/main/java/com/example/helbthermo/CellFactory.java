package com.example.helbthermo;

import java.util.HashMap;

public class CellFactory {
    private final ThermoView view;

    public CellFactory(ThermoView view) {
        this.view = view;
    }

    public HashMap<String, Cell> createCells() throws Exception {
        HashMap<String, Cell> cellsMap = new HashMap<>();

        for (int i = 0; i < ThermoController.ROW_CELL; i++) {
            for (int j = 0; j < ThermoController.COLUMN_CELL; j++) {
                if ((i == 0 && j == 0)
                        || (i == 0 && j == ThermoController.COLUMN_CELL - 1)
                        || (i == ThermoController.ROW_CELL - 1 && j == 0)
                        || (i == ThermoController.ROW_CELL - 1 && j == ThermoController.COLUMN_CELL - 1)
                        || (ThermoController.ROW_CELL % 2 != 0 && ThermoController.COLUMN_CELL % 2 != 0
                        && i == ThermoController.ROW_CELL / 2 && j == ThermoController.COLUMN_CELL / 2)) {
                    cellsMap.put("" + i + j, create(HeatSourceCell.class.getName(), i, j, ThermoController.ORIGINAL_HEAT_SOURCE_TEMPERATURE));
                } else {
                    cellsMap.put("" + i + j, create(Cell.class.getName(), i, j, 0.0));
                }
            }
        }
        return cellsMap;
    }

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
