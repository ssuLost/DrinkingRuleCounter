package com.lost.movie.night.visualizer;

import com.lost.movie.night.rules.Rule;
import com.lost.movie.night.visualizer.key.listener.KeyListernerImpl;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class FXMLController implements Initializable {

    final Logger logger = LogManager.getLogger(FXMLController.class);

    private ObservableList<Rule> ruleList;
    private Boolean useNativeFlag = false;
    private KeyListernerImpl keyListener;

    @FXML
    private ListView<Rule> ruleListView;
    @FXML
    private Button startListening;
    @FXML
    private Button StopListening;
    @FXML
    private MenuItem addRule;
    @FXML
    private MenuItem saveRules;
    @FXML
    private MenuItem close;
    @FXML
    private Button addRuleButton;
    @FXML
    private MenuItem loadRules;
    @FXML
    private MenuItem deleteRuleContext;
    @FXML
    private TextField moveNameField;
    @FXML
    private MenuItem useNative;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Creates the rules list on startup.
        ruleList = FXCollections.observableArrayList();
        ruleListView.setItems(ruleList);

    }

    @FXML
    private void handleStartListen(ActionEvent event) {
        keyListener = new KeyListernerImpl(ruleList);
        if (useNativeFlag) {
            this.startNativeListener();
        } else {
            this.StopListening.getScene().setOnKeyTyped(keyListener);
        }
        startListening.setDisable(true);
        StopListening.setDisable(false);
    }

    @FXML
    private void handleStopListen(ActionEvent event) {
        if (useNativeFlag) {
            this.stopNativeListener();
        } else {
            this.StopListening.getScene().setOnKeyTyped(null);
        }

        startListening.setDisable(false);
        StopListening.setDisable(true);
    }

    @FXML
    private void handleAddRule(ActionEvent event
    ) {
        TextInputDialog dialog = new TextInputDialog("AddRule");
        dialog.setTitle("Add a new rule");
        dialog.setHeaderText("Enter new rule");
        dialog.setContentText("Please enter the name of the rule:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(letter -> ruleList.add(new Rule(letter, Integer.toString(ruleList.size()), this)));
    }

    @FXML
    private void handleSaveRules(ActionEvent event
    ) {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Rules");
            fileChooser.setInitialFileName(moveNameField.getText() + ".ser");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showSaveDialog(this.ruleListView.getScene().getWindow());
            if (file == null) {
                return;
            }
            OutputStream fileStream = new FileOutputStream(file);
            OutputStream buffer = new BufferedOutputStream(fileStream);
            ObjectOutput output = new ObjectOutputStream(buffer);
            logger.debug(ruleList.toArray());
            output.writeObject(ruleList.toArray());
            output.close();
        } catch (IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    @FXML
    private void handleClose(ActionEvent event
    ) {
        this.handleSaveRules(event);
        this.handleStopListen(event);
        System.exit(0);
    }

    @FXML
    private void handleLoadRules(ActionEvent event
    ) {
        //deserialize the quarks.ser file
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Rules");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showOpenDialog(this.ruleListView.getScene().getWindow());
            if (file == null) {
                return;
            }
            InputStream fileStrem = new FileInputStream(file);
            InputStream buffer = new BufferedInputStream(fileStrem);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            Object[] recoveredRules = (Object[]) input.readObject();
            ruleList.clear();
            for (Object obj : recoveredRules) {
                Rule rule = (Rule) obj;
                logger.debug("Recovered Quark: " + rule.toStringDebug());
                rule.setController(this);
                ruleList.add(rule);
            }
            ruleListView.setItems(ruleList);
            this.refresh();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    @FXML
    private void handleContextDeleteRule(ActionEvent event) {
        ruleListView.getItems().remove(ruleListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleUseNative(ActionEvent event) {
        if (useNativeFlag) {
            useNativeFlag = false;
            if (this.startListening.isDisable()) {
                this.StopListening.getScene().setOnKeyTyped(keyListener);
                this.stopNativeListener();
            }
        } else {
            useNativeFlag = true;
            if (this.startListening.isDisable()) {
                this.StopListening.getScene().setOnKeyTyped(null);
                this.startNativeListener();
            }
        }
    }

    private void startNativeListener() {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(keyListener);
            logger.debug("Successfully regged hook");
        } catch (NativeHookException ex) {
            logger.error("There was a problem registering the native hook.");
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    private void stopNativeListener() {
        try {
            GlobalScreen.removeNativeKeyListener(keyListener);
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem unregistering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    public void refresh() {
        ruleListView.refresh();
    }
}
