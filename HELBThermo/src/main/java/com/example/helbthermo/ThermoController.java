package com.example.helbthermo;

import java.util.ArrayList;
import java.util.List;

public class ThermoController {

    public static final int ROW_CELL = 5;
    public static int COLUMN_CELL = 4;

    private final ThermoView view;
    private final List<Cell> cells;

    public ThermoController(ThermoView view) {
        this.view = view;
        this.cells = new ArrayList<>();

        initialization();
        view.initView(ROW_CELL, COLUMN_CELL);
        setCellButtonsActions();

    }

    private void initialization() {
        for (int i = 0; i < ThermoController.COLUMN_CELL; i++) {
            for (int j = 0; j < ThermoController.ROW_CELL; j++) {
                //System.out.println(i + ";" + j);
                cells.add(new Cell(i, j));
            }
        }
    }

    public void changeCellToHeatSourceCell(HeatSourceCell heatSourceCell) {
        cells.remove(getCell(heatSourceCell.getX(), heatSourceCell.getY()));
        cells.add(heatSourceCell);
    }

    public void changeCellToDeadCell(DeadCell deadCell) {
        cells.remove(getCell(deadCell.getX(), deadCell.getY()));
        cells.add(deadCell);
    }

    public void resetCell(Cell cell) {
        cells.remove(getCell(cell.getX(), cell.getY()));
        cells.add(cell);
    }

    private void setCellButtonsActions() {
        for (Cell cell : cells) {
            String key = "" + cell.getX() + cell.getY();
            view.getButton(key).setOnAction(e -> {
                Cell newCell = CellView.display(cell);
                if (newCell instanceof HeatSourceCell) {
                    HeatSourceCell heatSourceCell = (HeatSourceCell) newCell;
                    changeCellToHeatSourceCell(heatSourceCell);
                    view.setupHeatSourceCell(heatSourceCell);
                    view.addHeatCellInBox(key);
                } else if (newCell instanceof DeadCell) {
                    DeadCell deadCell = (DeadCell) newCell;
                    changeCellToDeadCell(deadCell);
                    view.setupDeadCell(deadCell);
                } else {
                    view.resetCell(newCell);
                    resetCell(newCell);
                }
                setCellButtonsActions();
            });
        }
    }

    public Cell getCell(int row, int col) { return cells.get(row * ThermoController.COLUMN_CELL + col); }

    public List<Cell> getCells() {return cells;}

}
