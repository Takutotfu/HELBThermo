package com.example.helbthermo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.HashMap;

public class ThermoController {

    public static final int COLUMN_CELL = 5;
    public static final int ROW_CELL = 4;


    private final int timerDuration = 1;

    private final ThermoView view;

    // TODO: cells into factory
    private final HashMap<String, Cell> cells;
    private boolean isSimulationStarted = false;
    private boolean isSimulationPaused = !isSimulationStarted;

    private Timeline timeline;

    private int timer = 0;

    public ThermoController(ThermoView view) {
        this.view = view;
        this.cells = new HashMap<>();
        this.timeline = new Timeline();

        initialization();
        view.initView();
        setCellButtonsActions();
        setLeftButtonsActions();
    }

    private void initialization() {
        for (int i = 0; i < ThermoController.ROW_CELL; i++) {
            for (int j = 0; j < ThermoController.COLUMN_CELL; j++) {
                cells.put("" + i + j, new Cell(i, j));
            }
        }
    }

    private void setLeftButtonsActions() {
        view.getPlayButton().setOnAction(event -> {
            if (!isSimulationStarted) {
                startSimulation();
                isSimulationStarted = true;
            } else {
                timeline.play();
            }
        });

        view.getPauseButton().setOnAction(event -> {
            if (isSimulationStarted) {
                timeline.pause();
            }
        });

        view.getResetButton().setOnAction(event -> {
            timeline.stop();
            Thermo.resetSimulation(view);
            cells.clear();
            initialization();
        });
    }

    private void startSimulation() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(timerDuration), actionEvent -> {
            Thermo.simulation(view, cells);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setCellButtonsActions() {
        for (Cell cell : cells.values()) {
            String key = "" + cell.getX() + cell.getY();
            view.getCellButton(key).setOnAction(e -> {
                Cell newCell = CellView.display(cell);
                if (newCell instanceof HeatSourceCell) {
                    HeatSourceCell heatSourceCell = (HeatSourceCell) newCell;
                    changeCellToHeatSourceCell(heatSourceCell);
                    view.setupHeatSourceCell(key, newCell.getTemperature());
                    view.addHeatCellInBox(key);
                    setHeatCellActions(key);
                } else if (newCell instanceof DeadCell) {
                    DeadCell deadCell = (DeadCell) newCell;
                    changeCellToDeadCell(deadCell);
                    view.setupDeadCell(deadCell);
                } else {
                    view.resetCell(key);
                    resetCell(newCell);
                }
                setCellButtonsActions();
            });
        }
    }

    private void setHeatCellActions(String key) {
        view.getHeatCellButton(key).setOnAction(e -> {
            HeatSourceCell cell = (HeatSourceCell) cells.get(key);
            if (cell != null) {
                if (!cell.isActivated()) {
                    cell.setActivated(true);
                    view.setupHeatSourceCell(key, cell.getTemperature());
                    view.addHeatCellInBox(key);
                } else {
                    cell.setActivated(false);
                    view.disableHeatSourceCell(key);
                }
            }
        });
    }

    public void changeCellToHeatSourceCell(HeatSourceCell heatSourceCell) {
        String key = ""+heatSourceCell.getX() + heatSourceCell.getY();
        cells.remove(key);
        cells.put(key, heatSourceCell);
    }

    public void changeCellToDeadCell(DeadCell deadCell) {
        String key = ""+deadCell.getX() + deadCell.getY();
        cells.remove(key);
        cells.put(key,deadCell);
    }

    public void resetCell(Cell cell) {
        String key = ""+cell.getX() + cell.getY();
        cells.remove(key);
        cells.put(key, cell);
    }

    public HashMap<String, Cell> getCells() {
        return cells;
    }

}
