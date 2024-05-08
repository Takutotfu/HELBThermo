package com.example.helbthermo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ThermoController {

    public static final int ROW_CELL = 5;
    public static int COLUMN_CELL = 4;

    private final int TIMER_DURATION = 1;

    private final ThermoView view;

    private final List<Cell> cells;
    private boolean isSimulationStarted = false;

    private Timeline timeline;

    private int timer = 0;

    public ThermoController(ThermoView view) {
        this.view = view;
        this.cells = new ArrayList<>();
        this.timeline = new Timeline();

        initialization();
        view.initView();
        setCellButtonsActions();
        setLeftButtonsActions();
    }

    private void initialization() {
        for (int i = 0; i < ThermoController.COLUMN_CELL; i++) {
            for (int j = 0; j < ThermoController.ROW_CELL; j++) {
                cells.add(new Cell(i, j));
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
            timeline.pause();
        });

        view.getResetButton().setOnAction(event -> {

        });
    }

    private void startSimulation() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(TIMER_DURATION), actionEvent -> {

            Button timeBox = view.getTimeBox();
            Button priceBox = view.getPriceBox();
            Button extTempBox = view.getExtTempBox();
            Button avgTempBox = view.getAvgTempBox();

            timeBox.setText("Temps : " + timer);
            timer++;
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setCellButtonsActions() {
        for (Cell cell : cells) {
            String key = "" + cell.getX() + cell.getY();
            view.getCellButton(key).setOnAction(e -> {
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

    public Cell getCell(int row, int col) {
        return cells.get(row * ThermoController.COLUMN_CELL + col);
    }

    public List<Cell> getCells() {
        return cells;
    }

}
