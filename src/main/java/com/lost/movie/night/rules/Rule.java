package com.lost.movie.night.rules;

import com.lost.movie.night.visualizer.FXMLController;
import java.io.Serializable;

/**
 *
 * @author James
 */
public class Rule implements Serializable {
    private final String name;
    
    //Number of times the rule happened.
    private int cnt;
    
    private final String keybind;
    
    private transient FXMLController controller;
    
    public Rule(String name, String keybind, FXMLController con) {
        this.name = name;
        this.keybind = keybind;
        this.controller = con;
    }
    
    public String getbind() {
        return this.keybind;
    }
    
    public double getCnt() {
        return cnt;
    }
    
    public String getName() { return this.name; }

    /**
     * Increments the rule.
     */
    public void addCnt() {
        cnt++;
        controller.refresh();
    }
    
    public void setController(FXMLController con){
        this.controller = con;
    }

    @Override
    public String toString() {
        return this.name+": "+this.cnt;
    }
    
    public String toStringDebug() {
        return this.name+": "+this.cnt+": "+this.keybind;
    }
}
