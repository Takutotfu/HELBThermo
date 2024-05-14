package com.example.helbthermo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class ThermoController {

    public static final int COLUMN_CELL = 7;
    public static final int ROW_CELL = 7;
    public static final int TEMP_EXT = 3; //°C
    public static final double ORIGINAL_HEAT_SOURCE_TEMPERATURE = 85.0;

    private final int timerDuration = 1;

    private final ThermoView view;
    private final CellFactory cellFactory;
    private final HashMap<String, Cell> cellsMap;

    private boolean isSimulationStarted;
    private Timeline timeline;
    private String textLog;

    public ThermoController(ThermoView view) throws Exception {
        this.timeline = new Timeline();
        this.isSimulationStarted = false;
        this.textLog = "";

        this.view = view;
        view.initView();

        this.cellFactory = new CellFactory(view);
        this.cellsMap = cellFactory.createCells();

        setCellButtonsActions();
        setLeftButtonsActions();
        setStageCloseEvent();
    }

    private void setStageCloseEvent() {
        view.getStage().setOnCloseRequest(event -> {
            createFile();
        });
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
            isSimulationStarted = false;
        });

        view.getResetButton().setOnAction(event -> {
            ThermoSystem.resetSimulation();
            view.resetView();
        });
    }

    private void startSimulation() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(timerDuration), actionEvent -> {
            view.setExtTempBox("T° ext. : " + TEMP_EXT + "°C");
            view.setTimeBox("Temps : " + ThermoSystem.timer + "sec");

            ThermoSystem.simulation(cellsMap);

            view.setPriceBox("€ : " + ThermoSystem.cost + "€");
            view.setAvgTempBox("T° moy. : " + new DecimalFormat("#.##").format(ThermoSystem.avgTemp) + "°C");

            textLog += ThermoSystem.timer + ";"
                    + ThermoSystem.cost + ";"
                    + new DecimalFormat("#.##").format(ThermoSystem.avgTemp) + ";"
                    + TEMP_EXT + "\n";
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

    // Code basé sur W3 School https://www.w3schools.com/java/java_files_create.asp
    // ##############################################################################
    private void createFile() {
        // Date formater https://www.w3schools.com/java/java_date.asp
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyy_HHmmss");
        String filename = myDateObj.format(myFormatObj) + ".log";

        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                writeToFile(filename);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred with file creation.");
            e.printStackTrace();
        }
    }

    private void writeToFile(String filename) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(textLog);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred with file writing.");
            e.printStackTrace();
        }
    }
    // ##############################################################################
}
