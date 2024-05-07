package com.example.helbthermo;

import java.util.HashMap;

public class ThermoController {

    public static final int ROW_CELL = 3;
    public static int COLUMN_CELL = 3;

    private ThermoView view;
    private HELBThermo helbThermo;

    public ThermoController(ThermoView view) {
        this.view = view;
        this.helbThermo = new HELBThermo();
        view.initView(ROW_CELL, COLUMN_CELL, helbThermo);
        setActions();
    }

    private void setActions() {
        for (Cell cell : helbThermo.getCells()) {
            String key = "" + cell.getX() + cell.getY();
            view.getButton(key).setOnAction(e -> {
               Cell newCell = CellView.display(cell);
               if (newCell instanceof HeatSourceCell) {
                   HeatSourceCell heatSourceCell = (HeatSourceCell) newCell;
                   helbThermo.changeCellToHeatSourceCell(heatSourceCell);
                   view.setupHeatSourceCell(heatSourceCell);
               } else if (newCell instanceof DeadCell) {
                   DeadCell deadCell = (DeadCell) newCell;
                   helbThermo.changeCellToDeadCell(deadCell);
                   view.setupDeadCell(deadCell);
               }
            });
        }
    }
}
