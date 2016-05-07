package jd.gui;

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
import jd.jdVar;
import static saf.components.AppStyleArbiter.CLASS_HEADING_LABEL;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;

/**
 *
 * @author Zhe Lin
 */
public class varDialog  extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    //ScheduleItem scheduleItem;
    jdVar var;
    
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
    //Access
    Label accessLabel;
    ComboBox accBox;
    //Buttons
    Button completeButton;
    Button cancelButton;
    
    // THIS IS FOR KEEPING TRACK OF WHICH BUTTON THE USER PRESSED
    String selection;
    
    // CONSTANTS FOR OUR UI
    public static final String COMPLETE = "Complete";
    public static final String CANCEL = "Cancel";
    public static final String NAME_PROMPT = "Name: ";
    public static final String TYPE_PROMPT = "Type";
    public static final String STATIC_PROMPT = "Static";
    public static final String ACCESS_PROMPT = "Access";
    public static final String VAR_HEADING = "Variable Details";
    public static final String ADD_VAR_TITLE = "Add New Variable";
    public static final String EDIT_VAR_TITLE = "Edit Variable";
    /**
     * Initializes this dialog so that it can be used for either adding
     * new schedule items or editing existing ones.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    public varDialog(Stage primaryStage) {       
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        // FIRST OUR CONTAINER
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // PUT THE HEADING IN THE GRID, NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(VAR_HEADING);
        headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
    
        // NOW THE NAME 
        nameLabel = new Label(NAME_PROMPT);
        nameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        nameTextField = new TextField();
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            var.setName(newValue);
        });
        
        // AND THE Type
        typeLabel = new Label(TYPE_PROMPT);
        typeLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        typeTextField = new TextField();
        typeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            var.setType(newValue);
        });
        
        // AND THE STATIC
        staticLabel = new Label(STATIC_PROMPT);
        staticLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        staticBox = new CheckBox();
        staticBox.setOnAction(e -> {
            handleStaticChange();
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
                var.setAccess(newValue);
            }    
        });
        
        
        // AND FINALLY, THE BUTTONS
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);
        
        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            varDialog.this.selection = sourceButton.getText();
            varDialog.this.hide();
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);

        // NOW LET'S ARRANGE THEM ALL AT ONCE
        gridPane.add(headingLabel, 0, 0, 2, 1);
        gridPane.add(nameLabel, 0, 1, 1, 1);
        gridPane.add(nameTextField, 1, 1, 1, 1);
        gridPane.add(typeLabel, 0, 2, 1, 1);
        gridPane.add(typeTextField, 1, 2, 1, 1);
        gridPane.add(staticLabel, 0, 3, 1, 1);
        gridPane.add(staticBox, 1, 3, 1, 1);
        gridPane.add(accessLabel, 0, 4, 1, 1);
        gridPane.add(accBox, 1, 4, 1, 1);
        gridPane.add(completeButton, 0, 5, 1, 1);
        gridPane.add(cancelButton, 1, 5, 1, 1);

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
    
    public jdVar getVar() { 
        return var;
    }
    
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param message Message to appear inside the dialog.
     */
    public jdVar showAddVarDialog() {
        // SET THE DIALOG TITLE
        setTitle(ADD_VAR_TITLE);
        
        // RESET THE SCHEDULE ITEM OBJECT WITH DEFAULT VALUES
        var = new jdVar();
        
        // LOAD THE UI STUFF
        nameTextField.setText(var.getName());
        typeTextField.setText(var.getType());
        staticBox.setSelected(var.getStatic());
        accBox.setPromptText(var.getAccess());
        accBox.setValue(var.getAccess());
        
        // AND OPEN IT UP
        this.showAndWait();
        
        return var;
    }
    
    public void loadGUIData() {
        // LOAD THE UI STUFF
        nameTextField.setText(var.getName());
        typeTextField.setText(var.getType());
        staticBox.setSelected(var.getStatic());
        accBox.setPromptText(var.getAccess());
        accBox.setValue(var.getAccess());
        
    }
    
    public boolean wasCompleteSelected() {
        if(selection == null)
            return false;
        return selection.equals(COMPLETE);
    }
    
    public void showEditVarDialog(jdVar itemToEdit) {
        // SET THE DIALOG TITLE
        setTitle(EDIT_VAR_TITLE);
        
        if(itemToEdit != null) {
            // LOAD THE VAR INTO OUR LOCAL OBJECT
            var = new jdVar();
            var.setName(itemToEdit.getName());
            var.setType(itemToEdit.getType());
            var.setStatic(itemToEdit.getStatic());
            var.setAccess(itemToEdit.getAccess());
        }
        // AND THEN INTO OUR GUI
        loadGUIData();
               
        // AND OPEN IT UP
        this.showAndWait();
    }

    private void handleStaticChange() {
        var.setStatic(staticBox.isSelected());
    }

}
