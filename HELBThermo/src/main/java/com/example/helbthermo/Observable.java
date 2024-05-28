package com.example.helbthermo;

// Design Pattern Observer
public interface Observable {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObserver();
}

