package com.example.helbthermo;

// Modele representant une cellule source de chaleur
public class HeatSourceCell extends Cell {
    // Attributs de HeatSourceCell
    private final double heatTemperature;
    private boolean isActivated;

    // Constructeur
    public HeatSourceCell(int x, int y, double heatTemperature) {
        super(x, y);
        setTemperature(heatTemperature);
        this.isActivated = true;
        this.heatTemperature = heatTemperature;
    }

    // Récriture de getTemperature pour renvoyer la heatTemperature à la place de temperature lorsque la cell est activée
    @Override
    public double getTemperature() {
        if (isActivated) {
            return heatTemperature;
        } else {
            return super.getTemperature();
        }
    }

    // Getter & Setter
    public boolean isActivated() {return isActivated;}
    public void activate() {isActivated = true;}
    public void deactivate() {isActivated = false;}
}
