package jd.gui;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jd.jdMet;
import static saf.components.AppStyleArbiter.CLASS_HEADING_LABEL;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;

/**
 *
 * @author Zhe Lin
 */
public class metDialog  extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    //ScheduleItem scheduleItem;
    jdMet met;
    
    // GUI CONTROLS FOR OUR DIALOG
    GridPane gridPane;
    Scene dialogScene;
    Label headingLabel;
    //Name
    Label nameLabel;
    TextField nameTextField;
    //Type
    Label typeLabel;
    TextField typeTextField;
    //Static
    Label staticLabel;
    CheckBox staticBox;
    //Abstract
    Label abstractLabel;
    CheckBox abstractBox;
    //Access
    Label accessLabel;
    ComboBox accBox;
    //Arguments
    ArrayList<Label> argLabels;
    ArrayList<TextField> argTFs;    
    //Buttons
    Button completeButton;
    Button cancelButton;
    
    // THIS IS FOR KEEPING TRACK OF WHICH BUTTON THE USER PRESSED
    String selection;
    
    int argSize;
    
    // CONSTANTS FOR OUR UI
    public static final String COMPLETE = "Complete";
    public static final String CANCEL = "Cancel";
    public static final String NAME_PROMPT = "Name: ";
    public static final String TYPE_PROMPT = "Return Type";
    public static final String STATIC_PROMPT = "Static";
    public static final String ABSTRACT_PROMPT = "Abstract";
    public static final String ACCESS_PROMPT = "Access";
    public static final String MET_HEADING = "Method Details";
    public static final String ADD_MET_TITLE = "Add New Method";
    public static final String EDIT_MET_TITLE = "Edit Method";
    /**
     * Initializes this dialog so that it can be used for either adding
     * new schedule items or editing existing ones.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    public metDialog(Stage primaryStage, int argSize) {       
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        this.argSize = argSize;
        
        // FIRST OUR CONTAINER
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // PUT THE HEADING IN THE GRID, NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(MET_HEADING);
        headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
    
        // NOW THE NAME 
        nameLabel = new Label(NAME_PROMPT);
        nameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        nameTextField = new TextField();
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            met.setName(newValue);
        });
        
        // AND THE TYPE
        typeLabel = new Label(TYPE_PROMPT);
        typeLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        typeTextField = new TextField();
        typeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            met.setType(newValue);
        });
        
        // AND THE STATIC
        staticLabel = new Label(STATIC_PROMPT);
        staticLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        staticBox = new CheckBox();
        staticBox.setOnAction(e -> {
            handleStaticChange();
        });
        
        // AND THE ABSTRACT
        abstractLabel = new Label(ABSTRACT_PROMPT);
        abstractLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        abstractBox = new CheckBox();
        abstractBox.setOnAction(e -> {
            handleAbstractChange();
        });
        
        // AND THE ACCESS
        accessLabel = new Label(ACCESS_PROMPT);
        accessLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        ObservableList<String> options = 
            FXCollections.observableArrayList(
            "public",
            "protected",
            "private"
        );
        accBox = new ComboBox(options);
        accBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String oldValue, String newValue) {                
                met.setAccess(newValue);
            }    
        });
        
        // AND THE ARGUMENTS
        argLabels = new ArrayList();
        argTFs = new ArrayList();
        for(int i = 0; i < argSize; i++) {
            String lb = "Arg";
            int lbi = i + 1;
            lb += lbi;
            final int theI = i;
            Label argLabel = new Label(lb);
            argLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
            TextField argTF = new TextField();
            argTF.textProperty().addListener((observable, oldValue, newValue) -> {
                met.setArg(newValue, theI);
            });   
            argLabels.add(argLabel);
            argTFs.add(argTF);
        }
        
        
        // AND FINALLY, THE BUTTONS
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);
        
        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            metDialog.this.selection = sourceButton.getText();
            metDialog.this.hide();
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);

        // NOW LET'S ARRANGE THEM ALL AT ONCE
        gridPane.add(headingLabel, 0, 0, 2, 1);
        gridPane.add(completeButton, 0, 1, 1, 1);
        gridPane.add(cancelButton, 1, 1, 1, 1);
        gridPane.add(nameLabel, 0, 2, 1, 1);
        gridPane.add(nameTextField, 1, 2, 1, 1);
        gridPane.add(typeLabel, 0, 3, 1, 1);
        gridPane.add(typeTextField, 1, 3, 1, 1);
        gridPane.add(staticLabel, 0, 4, 1, 1);
        gridPane.add(staticBox, 1, 4, 1, 1);
        gridPane.add(abstractLabel, 0, 5, 1, 1);
        gridPane.add(abstractBox, 1, 5, 1, 1);
        gridPane.add(accessLabel, 0, 6, 1, 1);
        gridPane.add(accBox, 1, 6, 1, 1);
        for(int i = 0; i < argSize; i++) {
            //System.out.println(argSize);
            gridPane.add(argLabels.get(i), 0, i + 7, 1, 1);
            gridPane.add(argTFs.get(i), 1, i + 7, 1, 1);
        }
        
        
        // AND PUT THE GRID PANE IN THE WINDOW
        dialogScene = new Scene(gridPane);
        //dialogScene.getStylesheets().add(PRIMARY_STYLE_SHEET);
        this.setScene(dialogScene);
    }
    
    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either YES, NO, or CANCEL, depending on which
     * button the user selected when this dialog was presented.
     */
    
    public String getSelection() {
        return selection;
    }
    
    public jdMet getMet() { 
        return met;
    }
    
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param message Message to appear inside the dialog.
     */
    public jdMet showAddMetDialog() {
        // SET THE DIALOG TITLE
        setTitle(ADD_MET_TITLE);
        
        // RESET THE METHOD WITH DEFAULT VALUES
        met = new jdMet();
        
        // LOAD THE UI STUFF
        nameTextField.setText(met.getName());
        typeTextField.setText(met.getType());
        staticBox.setSelected(met.getStatic());
        accBox.setPromptText(met.getAccess());
        accBox.setValue(met.getAccess());
        for(int i = 0; i < met.getArgs().size(); i++) {
            argTFs.get(i).setText((String) met.getArgs().get(i));
        }
        
        // AND OPEN IT UP
        this.showAndWait();
        
        return met;
    }
    
    public void loadGUIData() {
        // LOAD THE UI STUFF
        nameTextField.setText(met.getName());
        typeTextField.setText(met.getType());
        staticBox.setSelected(met.getStatic());
        abstractBox.setSelected(met.getAbstract());
        accBox.setPromptText(met.getAccess());
        accBox.setValue(met.getAccess());
        for(int i = 0; i < met.getArgs().size() && i < argTFs.size(); i++) {
            argTFs.get(i).setText((String) met.getArgs().get(i));
        }
    }
    
    public boolean wasCompleteSelected() {
        if(selection == null)
            return false;
        return selection.equals(COMPLETE);
    }
    
    public void showEditMetDialog(jdMet itemToEdit) {
        // SET THE DIALOG TITLE
        setTitle(EDIT_MET_TITLE);
        
        if(itemToEdit != null) {
            // LOAD THE MET INTO OUR LOCAL OBJECT
            met = new jdMet();
            met.setName(itemToEdit.getName());
            met.setType(itemToEdit.getType());
            met.setStatic(itemToEdit.getStatic());
            met.setAbstract(itemToEdit.getAbstract());
            met.setAccess(itemToEdit.getAccess());
            met.setArgs(itemToEdit.getArgs());
        }
        // AND THEN INTO OUR GUI
        loadGUIData();
               
        // AND OPEN IT UP
        this.showAndWait();
    }

    private void handleStaticChange() {
        met.setStatic(staticBox.isSelected());
    }

    private void handleAbstractChange() {
        met.setAbstract(abstractBox.isSelected());
    }
        
}
