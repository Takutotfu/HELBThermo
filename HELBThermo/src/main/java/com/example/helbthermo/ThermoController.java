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
    public static final double ORIGINAL_HEAT_SOURCE_TEMPERATURE = 85.0;

    public static double tempExt; //°C

    private final String simulationDataFileName = "simul.data";
    private final String logFolderName = "logs/";
    private final int timerDuration = 1;

    private final ThermoView view;
    private final CellFactory cellFactory;
    private final TemperatureDataParser parser;
    private final HashMap<String, Cell> cellsMap;

    private int timer = 0;
    private int cost = 0;
    private boolean isSimulationStarted;
    private Timeline timeline;
    private String textLog;
    private SimulationSystem systemMode;

    public ThermoController(ThermoView view) throws Exception {
        this.parser = new TemperatureDataParser(simulationDataFileName);
        tempExt = parser.getNextTemperature();
        this.timeline = new Timeline();
        this.isSimulationStarted = false;
        this.textLog = "";
        this.systemMode = new ThermoSystemManual();

        this.view = view;
        view.initView();

        this.cellFactory = new CellFactory(view);
        this.cellsMap = cellFactory.createCells();

        setCellButtonsActions();
        setLeftButtonsActions();
        setModeMenuActions();
        setStageCloseEvent();
    }

    private void setModeMenuActions() {
        view.getManualMode().setOnAction(event -> {
            view.getModeMenu().setText("Mode Manuel");
            systemMode = new ThermoSystemManual();
        });

        view.getTargetMode().setOnAction(event -> {
           view.getModeMenu().setText("Mode Target");
           systemMode = new ThermoSystemTarget();
        });
    }

    private void setStageCloseEvent() {
        view.getStage().setOnCloseRequest(event -> createFile());
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
            timer = 0;
            cost = 0;
            view.resetView();
        });
    }

    private void startSimulation() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(timerDuration), actionEvent -> {
            double avg;

            timer++;
            view.setTimeBox("Temps : " + timer + "sec");

            tempExt = parser.getNextTemperature();
            view.setExtTempBox("T° ext. : " + new DecimalFormat(ThermoView.NUMBER_FORMAT).format(tempExt) + "°C");

            systemMode.simulation(cellsMap);

            avg = getAvgTemperature();

            cost = getCalculatedCost();
            view.setPriceBox("€ : " + cost + "€");
            view.setAvgTempBox("T° moy. : " + new DecimalFormat(ThermoView.NUMBER_FORMAT).format(avg) + "°C");

            textLog += timer + ";"
                    + cost + ";"
                    + new DecimalFormat(ThermoView.NUMBER_FORMAT).format(avg) + ";"
                    + tempExt + "\n";
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
                    cellsMap.replace(cell.getId(), cellFactory.create(cellType[CellView.TYPE_INDEX], cell.getX(), cell.getY(), Double.parseDouble(cellType[CellView.TEMP_INDEX])));
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

    private double getAvgTemperature() {
        double avg = 0.0;
        for (Cell cell : cellsMap.values()) {
            avg += cell.getTemperature();
        }
        avg /= cellsMap.size();
        return avg;
    }

    private int getCalculatedCost(){
        int newCost = cost;
        for (Cell cell : cellsMap.values()) {
            if (cell instanceof HeatSourceCell) {
                if (((HeatSourceCell) cell).isActivated()) {
                    newCost += (int) (cell.getTemperature() * cell.getTemperature());
                }
            }
        }
        return newCost;
    }

    // Code basé sur W3 School https://www.w3schools.com/java/java_files_create.asp
    // ##############################################################################
    private void createFile() {
        // Date formater https://www.w3schools.com/java/java_date.asp
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyy_HHmmss");
        String filename = logFolderName + myDateObj.format(myFormatObj) + ".log";

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
