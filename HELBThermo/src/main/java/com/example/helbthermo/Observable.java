package com.example.helbthermo;

public interface Observable {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObserver();
}
