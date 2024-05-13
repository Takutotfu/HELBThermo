package com.example.helbthermo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.HashMap;

public class ThermoController {

    public static final int COLUMN_CELL = 7;
    public static final int ROW_CELL = 7;
    public static final double ORIGINAL_HEAT_SOURCE_TEMPERATURE = 85.0;

    private final int timerDuration = 1;

    private final ThermoView view;
    private final CellFactory cellFactory;

    private boolean isSimulationStarted;
    private Timeline timeline;
    private HashMap<String, Cell> cellsMap;

    public ThermoController(ThermoView view) throws Exception {
        this.timeline = new Timeline();
        this.isSimulationStarted = false;

        this.view = view;
        view.initView();

        this.cellFactory = new CellFactory(view);
        this.cellsMap = cellFactory.createCells();

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
            timeline.pause();
        });

        view.getResetButton().setOnAction(event -> {
            Thermo.resetSimulation();
            view.resetView();
        });
    }

    private void startSimulation() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(timerDuration), actionEvent -> {
            view.setExtTempBox("T° ext. : " + Thermo.TEMP_EXT + "°C");
            Thermo.simulation(view, cellsMap);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setCellButtonsActions() {
        for (Cell cell : cellsMap.values()) {
            view.getCellButton(cell.getId()).setOnAction(e -> {
                timeline.pause();
                String[] cellType = CellView.display(cell);

                try {
                    cellsMap.replace(cell.getId(), cellFactory.create(cellType[0], cell.getX(), cell.getY(), Double.parseDouble(cellType[1])));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                setCellButtonsActions();

                if (isSimulationStarted) {
                    timeline.play();
                }
            });
        }
    }

}
