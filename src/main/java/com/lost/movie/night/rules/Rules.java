package com.lost.movie.night.rules;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author James
 */
public class Rules {
    List<Rule> listOfRules;
    
    public Rules(){
        listOfRules = new ArrayList<>();
    }
    
    public void putRule(Rule rule) {
        listOfRules.add(rule);
    }
    
    public void triggerRule(int ruleNumber) {
        listOfRules.get(ruleNumber).addCnt();
    }

    @Override
    public String toString() {
        return "Rules{" + "listOfRules=" + listOfRules + '}';
    }
}
