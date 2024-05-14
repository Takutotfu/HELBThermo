package com.example.helbthermo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TemperatureDataParser {
    private ArrayList<Double> temperatures = new ArrayList<>();
    private int currentIndex = 0;
    private int maxIndex = 0;

    public TemperatureDataParser(String fileName) {
        parse(fileName);
        maxIndex = temperatures.size();
    }

    public boolean hasNextTemperature() {
        return currentIndex < maxIndex - 1;
    }

    public double getNextTemperature() {
        if (hasNextTemperature()) {
            currentIndex++;
        }
        return temperatures.get(currentIndex);
    }

    private boolean isLineCorrect(String line) {
        if (line.matches("\\d+(\\.\\d+)?")) {
            double temperature = Double.parseDouble(line);
            return temperature >= 0.0 && temperature <= 40.0;
        }
        return false;
    }


    private void parse(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(isLineCorrect(line)){
                    double value = Double.parseDouble(line);
                    temperatures.add(value);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public int getMaxIndex() {return maxIndex;}

    public int getCurrentIndex() {return currentIndex;}
}
