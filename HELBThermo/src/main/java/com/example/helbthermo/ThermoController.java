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

    // Attributs de configuration
    public static final int COLUMN_CELL = 5;
    public static final int ROW_CELL = 4;
    public static final double ORIGINAL_HEAT_SOURCE_TEMPERATURE = 85.0;

    // Attribut public
    public static double tempExt; //°C

    // Attributs private final
    private final String simulationDataFileName = "simul.data";
    private final String logFolderName = "logs/";
    private final int timerDuration = 1;

    private final ThermoView view;
    private final CellFactory cellFactory;
    private final TemperatureDataParser parser;
    private final HashMap<String, Cell> cellsMap;

    // Attributs de la classe
    private int timer = 0;
    private int cost = 0;
    private boolean isSimulationStarted;
    private Timeline timeline;
    private String textLog;
    private SimulationSystem systemMode;

    // Constructeur de la classe
    public ThermoController(ThermoView view) throws Exception {
        // Instanciation du parser
        this.parser = new TemperatureDataParser(simulationDataFileName);
        tempExt = parser.getNextTemperature();

        // Instanciation de la timeline et initialisation de variables
        this.timeline = new Timeline();
        this.isSimulationStarted = false;
        this.textLog = "";

        // Instanciation de la Strategy
        this.systemMode = new ThermoSystemManual();

        // Initialisation de la vue
        this.view = view;
        view.initView();

        // Instanciation de la Factory et création des cellules instancié dans la cellsMap
        this.cellFactory = new CellFactory(view);
        this.cellsMap = cellFactory.createCells();

        // Gestion des setOnActions de tout les boutons de l'application
        setCellButtonsActions();
        setLeftButtonsActions();
        setModeMenuActions();
        setStageCloseEvent();
    }

    private void setModeMenuActions() {
        // MenuButtons du mode de chauffe
        view.getManualMode().setOnAction(e -> {
            view.getModeMenu().setText("Mode Manuel");
            systemMode = new ThermoSystemManual();
        });

        view.getTargetMode().setOnAction(e -> {
           view.getModeMenu().setText("Mode Target");
           systemMode = new ThermoSystemTarget();
        });
    }

    private void setStageCloseEvent() {
        // Gestion de la fermeture de l'application
        view.getStage().setOnCloseRequest(e -> createFile());
    }

    private void setLeftButtonsActions() {
        // Boutons play, pause et reset
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
        // Méthodes qui gère le lancement de la simulation
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
        // Toutes les cellules du tableaux
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
        // Calcule la temperature moyenne du systeme
        int deadCells = 0;
        double avg = 0.0;
        for (Cell cell : cellsMap.values()) {
            if (cell instanceof DeadCell) {
                deadCells++;
            }
            avg += cell.getTemperature();
        }
        avg /= cellsMap.size() - deadCells;
        return avg;
    }

    private int getCalculatedCost(){
        // Calcule le coût du systeme
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
