package com.example.helbthermo;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class HELBThermo {
    private List<Cell> cells;

    public HELBThermo() {
        cells = new ArrayList<>();
        initialization();
    }

    private void initialization() {
        for (int i = 0; i < ThermoController.COLUMN_CELL; i++) {
            for (int j = 0; j < ThermoController.ROW_CELL; j++) {
                cells.add(new Cell(i, j));
            }
        }
    }

    public Cell getCell(int row, int col) {
        return cells.get(row * ThermoController.COLUMN_CELL + col);
    }

    public void changeCellToHeatSourceCell(HeatSourceCell heatSourceCell) {
        cells.remove(getCell(heatSourceCell.getX(), heatSourceCell.getY()));
        cells.add(heatSourceCell);
    }

    public void changeCellToDeadCell(DeadCell deadCell) {
        cells.remove(getCell(deadCell.getX(), deadCell.getY()));
        cells.add(deadCell);
    }

    public List<Cell> getCells() { return cells; }
}
