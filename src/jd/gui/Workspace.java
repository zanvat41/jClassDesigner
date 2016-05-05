package jd.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jd.controller.PoseEditController;
import jd.data.DataManager;
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
    static final String CLASS_COLOR_CHOOSER_PANE = "color_chooser_pane";
    static final String CLASS_COLOR_CHOOSER_CONTROL = "color_chooser_control";
    static final String EMPTY_TEXT = "";
    static final int BUTTON_TAG_WIDTH = 75;

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
    Label backgroundColorLabel;
    ColorPicker backgroundColorPicker;
    Label parentLabel;
    MenuButton parentChoice;

    // FORTH ROW
    HBox row4Box;
    Label fillColorLabel;
    ColorPicker fillColorPicker;
    Label varLabel;
    Button addVar;
    Button delVar;
    
    // FIFTH ROW
    VBox row5Box;
    Label outlineColorLabel;
    ColorPicker outlineColorPicker;
    GridPane varGrid;
        
    // SIXTH ROW
    HBox row6Box;
    Label outlineThicknessLabel;
    Slider outlineThicknessSlider;
    Label metLabel;
    Button addMet;
    Button delMet;
    
    // SEVENTH ROW
    HBox row7Box;
    Button snapshotButton;
    GridPane metGrid;
    
    // THIS IS WHERE WE'LL RENDER OUR DRAWING
    ScrollPane SP;
    Pane canvas;
    
    // HERE IS THE CONTROLLER
    PoseEditController poseEditController;    

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // FOR DISPLAYING DEBUG STUFF
    Text debugText;

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
        poseEditController = new PoseEditController(app);
        
	layoutGUI();
	//setupHandlers();
    }
    
    public ColorPicker getFillColorPicker() {
	return fillColorPicker;
    }
    
    public ColorPicker getOutlineColorPicker() {
	return outlineColorPicker;
    }
    
    public ColorPicker getBackgroundColorPicker() {
	return backgroundColorPicker;
    }
    
    public Slider getOutlineThicknessSlider() {
	return outlineThicknessSlider;
    }
    
    private void layoutGUI() {
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
        parentLabel = new Label("Parent:                ");
        row3Box.getChildren().add(parentLabel);
        parentChoice = new MenuButton("Parent Choices");
        // parentChoice.getItems().add(new CheckMenuItem("DOTA"));
        row3Box.getChildren().add(parentChoice);
        
        

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
        varGrid = new GridPane();
        varGrid.setGridLinesVisible(true);
        varGrid.setHgap(10);
        varGrid.setVgap(10);
        Text nameText = new Text("Name");
        Text typeText = new Text("Type");
        Text staticText = new Text("Static");
        Text accessText = new Text("Access");
        varGrid.add(nameText, 0, 0);
        varGrid.add(typeText, 1, 0);
        varGrid.add(staticText, 2, 0);
        varGrid.add(accessText, 3, 0);
        row5Box.getChildren().add(varGrid);
	
	// ROW 6
	row6Box = new HBox();
        metLabel = new Label("Methods:   ");
        row6Box.getChildren().add(metLabel);
        addMet = new Button("+");
        delMet = new Button("-");
        row6Box.getChildren().add(addMet);
        row6Box.getChildren().add(delMet);
	
	// ROW 7
	row7Box = new HBox();
	//snapshotButton = gui.initChildButton(row7Box, SNAPSHOT_ICON.toString(), SNAPSHOT_TOOLTIP.toString(), false);
        metGrid = new GridPane();
        metGrid.setGridLinesVisible(true);
        metGrid.setHgap(10);
        metGrid.setVgap(10);
        Text nameText1 = new Text("Name");
        Text returnText1 = new Text("Return");
        Text staticText1 = new Text("Static");
        Text abstractText1 = new Text("Abstract");
        Text accessText1 = new Text("Access");
        Text argText1 = new Text("Arg1");
        metGrid.add(nameText1, 0, 0);
        metGrid.add(returnText1, 1, 0);
        metGrid.add(staticText1, 2, 0);
        metGrid.add(abstractText1, 3, 0);
        metGrid.add(accessText1, 4, 0);
        metGrid.add(argText1, 5, 0);
        row7Box.getChildren().add(metGrid);
	
	// NOW ORGANIZE THE EDIT TOOLBAR
	editToolbar.getChildren().add(row1Box);
	editToolbar.getChildren().add(row2Box);
	editToolbar.getChildren().add(row3Box);
	editToolbar.getChildren().add(row4Box);
	editToolbar.getChildren().add(row5Box);
	editToolbar.getChildren().add(row6Box);
	editToolbar.getChildren().add(row7Box);
        
	workspace = new BorderPane();
        
	// WE'LL RENDER OUR STUFF HERE IN THE CANVAS
	canvas = new Pane();
        SP = new ScrollPane();
        //System.out.println(workspace.getMinWidth());
        canvas.setMinSize(3000, 3000);
        SP.setContent(canvas);
                
	debugText = new Text();
	//canvas.getChildren().add(debugText);
	debugText.setX(100);
	debugText.setY(100);
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	DataManager data = (DataManager)app.getDataComponent();
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
            poseEditController.handleSelectRequest();
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
        
        
        
        nameArea.textProperty().addListener(e -> {
            // UPDATE THE TEMP SITE AS WE TYPE ATTRIBUTE VALUES
            VBox selectedPane = null;
            if(VBox.class.isInstance(data.getSelected())) {
                selectedPane = (VBox) data.getSelected();
                poseEditController.handleNameUpdate(selectedPane, nameArea.getText());
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
        
    }


    /*private void setupHandlers() {
	// MAKE THE EDIT CONTROLLER
	poseEditController = new PoseEditController(app);
	
	// NOW CONNECT THE BUTTONS TO THEIR HANDLERS
	selectionToolButton.setOnAction(e->{
	    poseEditController.processSelectSelectionTool();
	});
	removeButton.setOnAction(e->{
	    poseEditController.processRemoveSelectedShape();
	});
	rectButton.setOnAction(e->{
	    poseEditController.processSelectRectangleToDraw();
	});
	ellipseButton.setOnAction(e->{
	    poseEditController.processSelectEllipseToDraw();
	});
	
	moveToBackButton.setOnAction(e->{
	    poseEditController.processMoveSelectedShapeToBack();
	});
	moveToFrontButton.setOnAction(e->{
	    poseEditController.processMoveSelectedShapeToFront();
	});

	backgroundColorPicker.setOnAction(e->{
	    poseEditController.processSelectBackgroundColor();
	});
	fillColorPicker.setOnAction(e->{ 
	    poseEditController.processSelectFillColor();
	});
	outlineColorPicker.setOnAction(e->{
	    poseEditController.processSelectOutlineColor();
	});
	outlineThicknessSlider.valueProperty().addListener(e-> {
	    poseEditController.processSelectOutlineThickness();
	});
	snapshotButton.setOnAction(e->{
	    poseEditController.processSnapshot();
	});
	
	// MAKE THE CANVAS CONTROLLER	
	canvasController = new CanvasController(app);
	canvas.setOnMousePressed(e->{
	    canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processCanvasMouseRelease((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseDragged(e->{
	    canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseExited(e->{
	    canvasController.processCanvasMouseExited((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseMoved(e->{
	    canvasController.processCanvasMouseMoved((int)e.getX(), (int)e.getY());
	});
    }
    
    public Pane getCanvas() {
	return canvas;
    }
    
    public void setImage(ButtonBase button, String fileName) {
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + fileName;
        Image buttonImage = new Image(imagePath);
	
	// SET THE IMAGE IN THE BUTTON
        button.setGraphic(new ImageView(buttonImage));	
    }*/

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
	
	// COLOR PICKER STYLE
	//fillColorPicker.getStyleClass().add(CLASS_BUTTON);
	//outlineColorPicker.getStyleClass().add(CLASS_BUTTON);
	//backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);
	
	editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
	row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        nameLabel.getStyleClass().add(CLASS_HEADING_LABEL);
	row2Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        packageLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
	row3Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//backgroundColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
        parentLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
	
	row4Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        varLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addVar.getStyleClass().add(CLASS_FILE_BUTTON);
        delVar.getStyleClass().add(CLASS_FILE_BUTTON);
	//fillColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row5Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	//outlineColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row6Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        metLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addMet.getStyleClass().add(CLASS_FILE_BUTTON);
        delMet.getStyleClass().add(CLASS_FILE_BUTTON);
	//outlineThicknessLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row7Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
	DataManager dataManager = (DataManager)app.getDataComponent();
        reloadNameText("");
        reloadPackageText("");
        parentChoice = new MenuButton("Parent Choices");
	/*if (dataManager.isInState(PoseMakerState.STARTING_RECTANGLE)) {
	    selectionToolButton.setDisable(false);
	    removeButton.setDisable(true);
	    rectButton.setDisable(true);
	    ellipseButton.setDisable(false);
	}
	else if (dataManager.isInState(PoseMakerState.STARTING_ELLIPSE)) {
	    selectionToolButton.setDisable(false);
	    removeButton.setDisable(true);
	    rectButton.setDisable(false);
	    ellipseButton.setDisable(true);
	}
	else if (dataManager.isInState(PoseMakerState.SELECTING_SHAPE) 
		|| dataManager.isInState(PoseMakerState.DRAGGING_SHAPE)
		|| dataManager.isInState(PoseMakerState.DRAGGING_NOTHING)) {
	    boolean shapeIsNotSelected = dataManager.getSelectedShape() == null;
	    selectionToolButton.setDisable(true);
	    removeButton.setDisable(shapeIsNotSelected);
	    rectButton.setDisable(false);
	    ellipseButton.setDisable(false);
	    moveToFrontButton.setDisable(shapeIsNotSelected);
	    moveToBackButton.setDisable(shapeIsNotSelected);
	}
	
	removeButton.setDisable(dataManager.getSelectedShape() == null);
	backgroundColorPicker.setValue(dataManager.getBackgroundColor());*/
    }
    
    /*public void loadSelectedShapeSettings(Shape shape) {
	if (shape != null) {
	    Color fillColor = (Color)shape.getFill();
	    Color strokeColor = (Color)shape.getStroke();
	    double lineThickness = shape.getStrokeWidth();
	    fillColorPicker.setValue(fillColor);
	    outlineColorPicker.setValue(strokeColor);
	    outlineThicknessSlider.setValue(lineThickness);	    
	}
    }*/

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
        parentChoice.getItems().add(new CheckMenuItem(pc));
    }
}
