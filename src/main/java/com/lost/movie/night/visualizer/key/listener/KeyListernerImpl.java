/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lost.movie.night.visualizer.key.listener;

import com.lost.movie.night.rules.Rule;
import com.lost.movie.night.rules.Rules;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.apache.commons.lang3.StringUtils;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author James
 */
public class KeyListernerImpl implements NativeKeyListener {

    private final ObservableList<Rule> theRules;

    public KeyListernerImpl(ObservableList<Rule> rules) {
        theRules = rules;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
        
        for (Rule rule : theRules) {
            if(StringUtils.equals(rule.getbind(), ""+nke.getKeyChar())){
                System.out.println("Triggered: "+rule.toString());
                rule.addCnt();
            }
        }
    }

}
