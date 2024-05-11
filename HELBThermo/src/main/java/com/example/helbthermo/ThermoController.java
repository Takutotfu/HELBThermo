package com.example.helbthermo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ThermoController {

    public static final int COLUMN_CELL = 12;
    public static final int ROW_CELL = 10;

    private final int timerDuration = 1;

    private final ThermoView view;
    private final CellFactory cellFactory;

    private boolean isSimulationStarted = false;

    private Timeline timeline;

    public ThermoController(ThermoView view) throws Exception {
        this.view = view;
        this.timeline = new Timeline();
        this.cellFactory = new CellFactory(view);

        view.initView();
        cellFactory.createCells();
        setCellButtonsActions();
        setLeftButtonsActions();
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
                isSimulationStarted = false;
            }
        });

        view.getResetButton().setOnAction(event -> {
            timeline.stop();
            Thermo.resetSimulation();
            view.resetView();

            try {
                cellFactory.resetCells();
                isSimulationStarted = false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    private void startSimulation() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(timerDuration), actionEvent -> {
            Thermo.simulation(view, cellFactory.getCellsMap());
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setCellButtonsActions() {
        for (Cell cell : cellFactory.getCellsMap().values()) {
            view.getCellButton(cell.getId()).setOnAction(e -> {

                timeline.pause();
                Cell newCell = CellView.display(cell);

                if (newCell instanceof HeatSourceCell) {
                    try {
                        cellFactory.changeCellType(newCell, "HeatSourceCell");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (newCell instanceof DeadCell) {
                    try {
                        cellFactory.changeCellType(newCell, "DeadCell");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    try {
                        cellFactory.changeCellType(newCell, "Cell");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                setCellButtonsActions();
                if (isSimulationStarted) {
                    timeline.play();
                }
            });
        }
    }

}
