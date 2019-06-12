package com.bajera.xlog.rc;

import java.util.ArrayList;

public interface Observable {
    ArrayList<Observer> observers = new ArrayList<>();
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
