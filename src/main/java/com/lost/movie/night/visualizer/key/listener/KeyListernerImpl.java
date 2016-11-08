/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lost.movie.night.visualizer.key.listener;

import com.lost.movie.night.rules.Rule;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author James
 */
public class KeyListernerImpl implements NativeKeyListener, EventHandler<KeyEvent> {

    private final ObservableList<Rule> theRules;

    final Logger logger = LogManager.getLogger(KeyListernerImpl.class);

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
            if (StringUtils.equals(rule.getbind(), "" + nke.getKeyChar())) {
                System.out.println("Triggered: " + rule.toString());
                rule.addCnt();
            }
        }
    }

    @Override
    public void handle(KeyEvent event) {      
        logger.debug("Key typed: " + event.getCharacter());
        for (Rule rule : theRules) {
            if (StringUtils.equals(rule.getbind(), event.getCharacter())) {
                System.out.println("Triggered: " + rule.toString());
                rule.addCnt();
            }
        }
    }

}
