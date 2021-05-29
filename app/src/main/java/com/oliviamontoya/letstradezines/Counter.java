package com.oliviamontoya.letstradezines;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by Olivia on 6/25/17.
 */

public class Counter {

    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    private int counter;

    public Counter()
    {
        this.counter = 0;
    }

    public void increment()
    {
        this.counter++;
    }

    public int getCount() {
        return this.counter;
    }

    public void setCount(int counter) {
        this.counter = counter;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
}
