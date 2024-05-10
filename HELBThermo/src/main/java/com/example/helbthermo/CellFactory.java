package com.example.helbthermo;

import java.util.HashMap;

public class CellFactory {
    private final ThermoView view;

    private HashMap<String, Cell> cellsMap = new HashMap<>();


    public CellFactory(ThermoView view) {
        this.view = view;
    }

    public void createCells() throws Exception {
        for (int i = 0; i < ThermoController.ROW_CELL; i++) {
            for (int j = 0; j < ThermoController.COLUMN_CELL; j++) {
                if ((i == 0 && j == 0)
                        || (i == 0 && j == ThermoController.COLUMN_CELL - 1)
                        || (i == ThermoController.ROW_CELL - 1 && j == 0)
                        || (i == ThermoController.ROW_CELL - 1 && j == ThermoController.COLUMN_CELL - 1)) {
                    cellsMap.put("" + i + j, create("HeatSourceCell", i, j, Thermo.TEMP_EXT));
                } else {
                    cellsMap.put("" + i + j, create("Cell", i, j, 0.0));
                }
            }
        }
    }

    public Cell create(String cellType, int x, int y, double temperature) throws Exception {
        switch (cellType) {
            case "Cell":
                return new Cell(x, y);
            case "DeadCell":
                return new DeadCell(x, y);
            case "HeatSourceCell":
                HeatSourceCell heatSourceCell = new HeatSourceCell(x, y, temperature);
                String key = "" + x + y;

                view.setupHeatSourceCell(key, temperature);
                view.addHeatCellInBox(key, temperature);

                view.getHeatCellButton(key).setOnAction(e -> {
                    if (!heatSourceCell.isActivated()) {
                        heatSourceCell.setActivated(true);
                        view.unableHeatSourceCell(key, heatSourceCell.getHeatTemperature());
                    } else {
                        heatSourceCell.setActivated(false);
                        view.disableHeatSourceCell(key);
                    }
                });

                return heatSourceCell;
            default:
                throw new Exception("Invalid cell type");
        }
    }

    public void changeCellType(Cell cell, String cellType) throws Exception {
        switch (cellType) {
            case "Cell":
                cellsMap.replace(cell.getId(), create("Cell", cell.getX(), cell.getY(), cell.getTemperature()));
                break;
            case "DeadCell":
                cellsMap.replace(cell.getId(), create("DeadCell", cell.getX(), cell.getY(), cell.getTemperature()));
                break;
            case "HeatSourceCell":
                cellsMap.replace(cell.getId(), create("HeatSourceCell", cell.getX(), cell.getY(), cell.getTemperature()));
                break;
            default:
                throw new Exception("Invalid cell type");
        }
    }

    public void resetCells() throws Exception {
        cellsMap.clear();
        createCells();
    }

    public HashMap<String, Cell> getCellsMap() {
        return cellsMap;
    }

    public Cell getCell(int x, int y) {
        return cellsMap.get("" + x + y);
    }

    public Cell getCell(String key) {
        return cellsMap.get(key);
    }
}
