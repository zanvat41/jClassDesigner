package jd.gui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jd.controller.PoseEditController;
import jd.data.DataManager;
import jd.file.FileManager;
import jd.jdMet;
import jd.jdVar;
import saf.ui.AppYesNoCancelDialogSingleton;
import saf.ui.AppMessageDialogSingleton;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;


/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {

    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS Workspace'S COMPONENTS TO A STYLE SHEET THAT IT USES
    static final String CLASS_MAX_PANE = "max_pane";
    static final String CLASS_RENDER_CANVAS = "render_canvas";
    static final String CLASS_BUTTON = "button";
    static final String CLASS_EDIT_TOOLBAR = "edit_toolbar";
    static final String CLASS_EDIT_TOOLBAR_ROW = "edit_toolbar_row";
    static final String EMPTY_TEXT = "";
    static final int BUTTON_TAG_WIDTH = 75; 
    //int counter = 0;
    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;
    
    
    // FIRST ROW
    HBox row1Box;
    Button selectionToolButton;
    Button removeButton;
    Button rectButton;
    Button ellipseButton;
    TextField nameArea;
    Label nameLabel;
    
    // SECOND ROW
    HBox row2Box;
    Button moveToBackButton;
    Button moveToFrontButton;
    TextField packageArea;
    Label packageLabel;

    // THIRD ROW
    HBox row3Box;
    Label parentLabel;
    MenuButton parentChoice;
    Button external;

    // FORTH ROW
    HBox row4Box;
    Label varLabel;
    Button addVar;
    Button delVar;
    
    // FIFTH ROW
    VBox row5Box;
    // GridPane varGrid;
    TableView<jdVar> varTable;  
    TableColumn nameColumn1;
    TableColumn typeColumn1;
    TableColumn staticColumn1;
    TableColumn accessColumn1;
    
    // SIXTH ROW
    HBox row6Box;
    Label metLabel;
    Button addMet;
    Button delMet;
    Button addArgCol;
    
    // SEVENTH ROW
    HBox row7Box;
    //GridPane metGrid;
    ScrollPane metSP;
    TableView<jdMet> metTable;  
    TableColumn nameColumn2;
    TableColumn typeColumn2;
    TableColumn staticColumn2;
    TableColumn abstractColumn2;
    TableColumn accessColumn2;
    ArrayList<TableColumn> argColumn2;
    
    // THIS IS WHERE WE'LL RENDER OUR DRAWING
    ScrollPane SP;
    Pane canvas;
    GridPane GP;
    
    // HERE IS THE CONTROLLER
    PoseEditController poseEditController;    

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    
    int argSize;

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
                
        // KEEP THE CONTROLLER FOR LATER
        poseEditController = new PoseEditController(app, app.getGUI().getStage());
        
        argSize = 0;
        
	layoutGUI();
	//setupHandlers();
    }
    
    private void layoutGUI() {
        DataManager data = (DataManager)app.getDataComponent();
        
	// THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
	editToolbar = new VBox();
	
	// ROW 1
	row1Box = new HBox();
        nameLabel = new Label("Class Name: ");
        row1Box.getChildren().add(nameLabel);
        nameArea = new TextField();
        row1Box.getChildren().add(nameArea);
        

	// ROW 2
	row2Box = new HBox();
        packageLabel = new Label("Package:                ");
        row2Box.getChildren().add(packageLabel);
        packageArea = new TextField();
        row2Box.getChildren().add(packageArea);
        

	// ROW 3
	row3Box = new HBox();
        parentLabel = new Label("Parent:        ");
        row3Box.getChildren().add(parentLabel);
        parentChoice = new MenuButton("Parent Choices");
        external = new Button("+External Parent");
        // parentChoice.getItems().add(new CheckMenuItem("DOTA"));
        row3Box.getChildren().add(parentChoice);
        row3Box.getChildren().add(external);
        
        

	// ROW 4
	row4Box = new HBox();
        varLabel = new Label("Variables:  ");
        row4Box.getChildren().add(varLabel);
        addVar = new Button("+");
        delVar = new Button("-");
        row4Box.getChildren().add(addVar);
        row4Box.getChildren().add(delVar);
	
	// ROW 5
	row5Box = new VBox();
        varTable = new TableView();
        nameColumn1 = new TableColumn("Name");
        typeColumn1 = new TableColumn("Type");
        staticColumn1 = new TableColumn("Static");
        accessColumn1 = new TableColumn("Access");
        
        
        varTable.getColumns().add(nameColumn1);
        varTable.getColumns().add(typeColumn1);
        varTable.getColumns().add(staticColumn1);
        varTable.getColumns().add(accessColumn1);
        //varTable.setItems(data.getVars(BUTTON_TAG_WIDTH););
        
        nameColumn1.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        typeColumn1.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        staticColumn1.setCellValueFactory(
                new PropertyValueFactory<>("static"));
        accessColumn1.setCellValueFactory(
                new PropertyValueFactory<>("access"));
        row5Box.getChildren().add(varTable);
	
	// ROW 6
	row6Box = new HBox();
        metLabel = new Label("Methods:   ");
        row6Box.getChildren().add(metLabel);
        addMet = new Button("+");
        delMet = new Button("-");
        addArgCol = new Button("+Arg Column");
        row6Box.getChildren().add(addMet);
        row6Box.getChildren().add(delMet);
        row6Box.getChildren().add(addArgCol);
	
	// ROW 7
	row7Box = new HBox();
	//snapshotButton = gui.initChildButton(row7Box, SNAPSHOT_ICON.toString(), SNAPSHOT_TOOLTIP.toString(), false);
        metSP = new ScrollPane();
        metSP.setMaxSize(500, 800);
        metTable = new TableView();
        nameColumn2 = new TableColumn("Name");
        typeColumn2 = new TableColumn("Return");
        staticColumn2 = new TableColumn("Static");
        abstractColumn2 = new TableColumn("Abstract");
        accessColumn2 = new TableColumn("Access");
        argColumn2 = new ArrayList();
        //TableColumn arg = new TableColumn("Arg1");
        //argColumn2.add(arg);
        
        metTable.getColumns().add(nameColumn2);
        metTable.getColumns().add(typeColumn2);
        metTable.getColumns().add(staticColumn2);
        metTable.getColumns().add(abstractColumn2);
        metTable.getColumns().add(accessColumn2);
        //metTable.getColumns().add(arg);
        
        nameColumn2.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        typeColumn2.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        staticColumn2.setCellValueFactory(
                new PropertyValueFactory<>("static"));
        abstractColumn2.setCellValueFactory(
                new PropertyValueFactory<>("abstract"));
        accessColumn2.setCellValueFactory(
                new PropertyValueFactory<>("access"));
        //arg.setCellFactory(value);
        
        
        metSP.setContent(metTable);
        row7Box.getChildren().add(metSP);
	
	// NOW ORGANIZE THE EDIT TOOLBAR
	editToolbar.getChildren().add(row1Box);
	editToolbar.getChildren().add(row2Box);
	editToolbar.getChildren().add(row3Box);
	editToolbar.getChildren().add(row4Box);
	editToolbar.getChildren().add(row5Box);
	editToolbar.getChildren().add(row6Box);
	editToolbar.getChildren().add(row7Box);
        
	workspace = new BorderPane();
        
	// WE'LL RENDER OUR STUFF HERE IN THE 
        GP = new GridPane();
	canvas = new Pane();
        SP = new ScrollPane();
        setUpGrid(300, 300, false);  
        canvas.setMinSize(3000, 3000);
        canvas.getChildren().add(GP);
        //out.println(counter);
        //counter++;
        SP.setContent(canvas);
                
        
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	//DataManager data = (DataManager)app.getDataComponent();
	data.setPanes(canvas.getChildren());

	// AND NOW SETUP THE WORKSPACE
        //canvas.setMinSize(workspace.getWidth() - editToolbar.getWidth(), workspace.getHeight() - editToolbar.getHeight());
	((BorderPane)workspace).setCenter(SP);
        ((BorderPane)workspace).setRight(editToolbar);
        
        //canvas.setMinSize(workspace.getWidth() - editToolbar.getWidth(), workspace.getHeight() - editToolbar.getHeight());
        
        // THEN SET UP SOME CONTROLS
        gui.addClassButton.setOnAction(e -> {
            poseEditController.handleAddClassRequest();
        });
        
        gui.addInterfaceButton.setOnAction(e -> {
            poseEditController.handleAddInterfaceRequest();
        });
        
        gui.selectButton.setOnAction(e -> {
            poseEditController.handleSelectRequest(gui.snapBox);
        });
        
        gui.codeButton.setOnAction(e -> {
            try {
                poseEditController.handleCodeRequest();
            } catch (IOException ex) {
                PropertiesManager props = PropertiesManager.getPropertiesManager();
		AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		dialog.show("ERROR", "Save code error");
            }
        });
        
        gui.photoButton.setOnAction(e -> {
            try {
                poseEditController.handlePhotoRequest();
            } catch (IOException ex) {
                PropertiesManager props = PropertiesManager.getPropertiesManager();
		AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		dialog.show("ERROR", "Save photo error");
            }
        });
        
        
        external.setOnAction(e -> {
            poseEditController.handleAddEClassRequest();
        });
        
        nameArea.textProperty().addListener(e -> {
            // UPDATE THE TEMP SITE AS WE TYPE ATTRIBUTE VALUES
            //VBox selectedPane = null;
            if(VBox.class.isInstance(data.getSelected())) {
                //selectedPane = (VBox) data.getSelected();
                poseEditController.handleNameUpdate(nameArea.getText());
            }
            //poseEditController.handleNameUpdate(, attributeName, attributeTextField.getText());
	});
        
        packageArea.textProperty().addListener(e -> {
            // UPDATE THE TEMP SITE AS WE TYPE ATTRIBUTE VALUES
            VBox selectedPane = null;
            if(VBox.class.isInstance(data.getSelected())) {
                selectedPane = (VBox) data.getSelected();
                poseEditController.handlePackageUpdate(selectedPane, packageArea.getText());
            }
            //poseEditController.handleNameUpdate(, attributeName, attributeTextField.getText());
	});
        
        addVar.setOnAction(e -> {
            poseEditController.handleAddVarRequest();
        });
        
        delVar.setOnAction(e -> {
            poseEditController.handleRemoveVarRequest(this, varTable.getSelectionModel().getSelectedItem());
        });
        
        varTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if(varTable.getSelectionModel().getSelectedItem()!=null){
                    // OPEN UP THE SCHEDULE ITEM EDITOR
                    jdVar theVar = varTable.getSelectionModel().getSelectedItem();
                    poseEditController.handleEditVarRequest(theVar, this);
                }
            }
        });
        
        addArgCol.setOnAction(e -> {
            handleAddArg();
        });
        
        addMet.setOnAction(e -> {
            poseEditController.handleAddMetRequest(this);
        });
        
        delMet.setOnAction(e -> {
            poseEditController.handleRemoveMetRequest(this, metTable.getSelectionModel().getSelectedItem());
        });
        
        metTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if(metTable.getSelectionModel().getSelectedItem()!=null){
                    // OPEN UP THE SCHEDULE ITEM EDITOR
                    jdMet theMet = metTable.getSelectionModel().getSelectedItem();
                    poseEditController.handleEditMetRequest(theMet, this);
                }
            }
        });
        
        gui.resizeButton.setOnAction(e -> {
            poseEditController.handleResizeRequest();
        });
        
        
        gui.removeButton.setOnAction(e -> {
            poseEditController.handleRemoveRequest();
        });
        
        gui.gridBox.setOnAction(e -> {
            poseEditController.handleGridRequest(gui.gridBox);
        });
        
        gui.snapBox.setOnAction(e -> {
            poseEditController.handleSnapRequest(gui.snapBox);
        });
        
        gui.undoButton.setOnAction(e -> {
            try {
                poseEditController.handleUndoRequest();
            } catch (IOException ex) {
                PropertiesManager props = PropertiesManager.getPropertiesManager();
		AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		dialog.show("ERROR", "Operation Error");
            }
        });
        
        gui.redoButton.setOnAction(e -> {
            try {
                poseEditController.handleRedoRequest();
            } catch (IOException ex) {
                PropertiesManager props = PropertiesManager.getPropertiesManager();
		AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		dialog.show("ERROR", "Operation Error");
            }
        });
        
        gui.zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
            //GP.setScaleX(GP.getScaleX() * 2);
            //GP.setScaleY(GP.getScaleY() * 2);
            poseEditController.handleZoomIn();
            }
        });
        
        gui.zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                if(GP.getScaleX() * 1/2 >= 1 && GP.getScaleY() * 1/2 >=1) {
                    //GP.setScaleX(GP.getScaleX() * 1/2);
                    //GP.setScaleY(GP.getScaleY() * 1/2);
                    poseEditController.handleZoomOut();
                }
            }
        });
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    @Override
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
	canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
		
	editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
	row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        nameLabel.getStyleClass().add(CLASS_HEADING_LABEL);
	row2Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        packageLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
	row3Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        parentLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        external.getStyleClass().add(CLASS_FILE_BUTTON);
	
	row4Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        varLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addVar.getStyleClass().add(CLASS_FILE_BUTTON);
        delVar.getStyleClass().add(CLASS_FILE_BUTTON);
        addArgCol.getStyleClass().add(CLASS_FILE_BUTTON);
	row5Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row6Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        metLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addMet.getStyleClass().add(CLASS_FILE_BUTTON);
        delMet.getStyleClass().add(CLASS_FILE_BUTTON);
	row7Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
	DataManager dataManager = (DataManager)app.getDataComponent();
        FileManager fileManager = (FileManager) app.getFileComponent();
        if(dataManager.getPanes().isEmpty()){
            reloadNameText("");
            reloadPackageText("");
            //System.out.println(dataManager.getPanes().size());
            parentChoice.getItems().clear();
            //System.out.println(dataManager.getPanes().size());
            setVarTable(new ArrayList());
            setMetTable(new ArrayList());
            canvas.getChildren().add(GP);
        }
        resetArgCol(fileManager.getArgSize());
        setUpGrid(canvas.getWidth() / 10, canvas.getHeight() / 10, gui.gridBox.isSelected());
        if(!canvas.getChildren().contains(GP))
            canvas.getChildren().add(GP);
    }
    
    public Pane getCanvas() {
	return canvas;
    }
    
    public void reloadNameText(String name) {
        nameArea.setText(name);
    }

    public void reloadPackageText(String pname) {
        packageArea.setText(pname);
    }
    
    public void addParentChoice(String pc) {
        CheckMenuItem newPC = new CheckMenuItem(pc);
        newPC.setOnAction(e -> {
            poseEditController.handleParentChoice(newPC);
        });
        if(!pc.replace("{interface}", "").isEmpty())
            parentChoice.getItems().add(newPC);
    }
    
    public void removeParentChoice(String pc) {
        for(int i = 0; i < parentChoice.getItems().size(); i++) {
            if(parentChoice.getItems().get(i).getText().equals(pc)) {
                parentChoice.getItems().remove(i);
                i = parentChoice.getItems().size() + 1;
            }
        }        
    }
    
    public void uncheckParentChoice() {
        for(int i = 0; i < parentChoice.getItems().size(); i++) {
            CheckMenuItem cmi = (CheckMenuItem) parentChoice.getItems().get(i);
            cmi.setSelected(false);
        } 
    }

    public void checkParentChoice(String s) {
        for(int i = 0; i < parentChoice.getItems().size(); i++) {
            CheckMenuItem cmi = (CheckMenuItem) parentChoice.getItems().get(i);
            if(cmi.getText().equals(s)) {
                cmi.setSelected(true);
                i = parentChoice.getItems().size();
            }
        }     
    }
    
    public void setVarTable(ArrayList<jdVar> vars) {
        ObservableList<jdVar> varList =
        FXCollections.observableArrayList(
        );
        for(jdVar var : vars) {
            varList.add(var);
        }
        varTable.setItems(varList);
    }

    public void setMetTable(ArrayList<jdMet> mets) {
        ObservableList<jdMet> metList =
        FXCollections.observableArrayList(
        );
        for(jdMet met : mets) {
            metList.add(met);
        }
        metTable.setItems(metList);
    }
    
    private void handleAddArg() {
        argSize ++;
        String argName = "Arg";
        int size = argColumn2.size() + 1;
        argName += size;
        TableColumn arg = new TableColumn(argName);
        argColumn2.add(arg);
        metTable.getColumns().add(arg);
        
        arg.setCellValueFactory(new Callback<CellDataFeatures<jdMet, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<jdMet, String> p) {
            // p.getValue() returns the Person instance for a particular TableView row
                return p.getValue().getArgPro(size - 1);
            }
        });
    }
    
    public int getArgSize() {
        return argColumn2.size();
    }
    
    private void resetArgCol(int i) {
        int remain = i - argSize;
        for(int j = 0; j < remain; j++) {
            handleAddArg();
        }
    }
    
    public PoseEditController getPEC() {
        return poseEditController;
    }

    public void setUpGrid(double numCols, double numRows, boolean vis) {
        GP.setGridLinesVisible(vis);
        //GP.setVisible(false);
        GP.getColumnConstraints().clear();
        GP.getRowConstraints().clear();
        //System.out.println(numCols);
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            //colConst.setPercentWidth(100.0 / numCols);
            colConst.setPrefWidth(10);
            colConst.setMaxWidth(10);
            //System.out.println(100.0/numCols);
            //colConst.setPercentWidth(10);
            GP.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            //rowConst.setPercentHeight(100.0 / numRows);
            rowConst.setPrefHeight(10);
            rowConst.setMaxHeight(10);
            //rowConst.setPercentHeight(10);
            GP.getRowConstraints().add(rowConst);         
        }
        GP.setStyle("-fx-border-color: Black; -fx-background-color: transparent;");
        GP.setMinSize(numCols * 10, numRows * 10);
    }

    public void addGrid() {
        DataManager data = (DataManager)app.getDataComponent();
        data.getPanes().set(0, GP);
    }
    
    
}
