package com.example.helbthermo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Parser pour récupérer les temperatures d'un fichier
public class TemperatureDataParser {

    // Attributs de la classe
    private ArrayList<Double> temperatures = new ArrayList<>();
    private int currentIndex = 0;
    private int maxIndex = 0;

    // Constructeur de la classe
    public TemperatureDataParser(String fileName) {
        parse(fileName);
        maxIndex = temperatures.size();
    }

    // Méthode pour récupérer la prochaine température récupéré
    public double getNextTemperature() {
        if (hasNextTemperature()) {
            currentIndex++;
        }
        return temperatures.get(currentIndex);
    }

    // Méthode pour vérifier si nous sommes à la fin du la liste de température
    private boolean hasNextTemperature() {
        return currentIndex < maxIndex - 1;
    }

    // Méthode pour vérifier si la ligne est bien un double entre 0 et 40
    private boolean isLineCorrect(String line) {
        if (line.matches("\\d+(\\.\\d+)?")) {
            double temperature = Double.parseDouble(line);
            return temperature >= 0.0 && temperature <= 40.0;
        }
        return false;
    }

    // Méthode qui parse le fichier
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

    // Getter
    public int getMaxIndex() {return maxIndex;}
    public int getCurrentIndex() {return currentIndex;}
}
