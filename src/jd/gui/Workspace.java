package jd.gui;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import static jd.PropertyType.ELLIPSE_ICON;
import static jd.PropertyType.ELLIPSE_TOOLTIP;
import static jd.PropertyType.MOVE_TO_BACK_ICON;
import static jd.PropertyType.MOVE_TO_BACK_TOOLTIP;
import static jd.PropertyType.MOVE_TO_FRONT_ICON;
import static jd.PropertyType.MOVE_TO_FRONT_TOOLTIP;
import static jd.PropertyType.RECTANGLE_ICON;
import static jd.PropertyType.RECTANGLE_TOOLTIP;
import static jd.PropertyType.REMOVE_ICON;
import static jd.PropertyType.REMOVE_TOOLTIP;
import static jd.PropertyType.SELECTION_TOOL_ICON;
import static jd.PropertyType.SELECTION_TOOL_TOOLTIP;
import static jd.PropertyType.SNAPSHOT_ICON;
import static jd.PropertyType.SNAPSHOT_TOOLTIP;
import jd.controller.CanvasController;
import jd.controller.PoseEditController;
import jd.data.DataManager;
import static jd.data.DataManager.BLACK_HEX;
import static jd.data.DataManager.WHITE_HEX;
import saf.ui.AppYesNoCancelDialogSingleton;
import saf.ui.AppMessageDialogSingleton;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;

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
    
    // SECOND ROW
    HBox row2Box;
    Button moveToBackButton;
    Button moveToFrontButton;

    // THIRD ROW
    VBox row3Box;
    Label backgroundColorLabel;
    ColorPicker backgroundColorPicker;

    // FORTH ROW
    VBox row4Box;
    Label fillColorLabel;
    ColorPicker fillColorPicker;
    
    // FIFTH ROW
    VBox row5Box;
    Label outlineColorLabel;
    ColorPicker outlineColorPicker;
        
    // SIXTH ROW
    VBox row6Box;
    Label outlineThicknessLabel;
    Slider outlineThicknessSlider;
    
    // SEVENTH ROW
    HBox row7Box;
    Button snapshotButton;
    
    // THIS IS WHERE WE'LL RENDER OUR DRAWING
    Pane canvas;
    
    // HERE ARE THE CONTROLLERS
    CanvasController canvasController;
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
	selectionToolButton = gui.initChildButton(row1Box, SELECTION_TOOL_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), true);
	removeButton = gui.initChildButton(row1Box, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), true);
	rectButton = gui.initChildButton(row1Box, RECTANGLE_ICON.toString(), RECTANGLE_TOOLTIP.toString(), false);
	ellipseButton = gui.initChildButton(row1Box, ELLIPSE_ICON.toString(), ELLIPSE_TOOLTIP.toString(), false);

	// ROW 2
	row2Box = new HBox();
	moveToBackButton = gui.initChildButton(row2Box, MOVE_TO_BACK_ICON.toString(), MOVE_TO_BACK_TOOLTIP.toString(), true);
	moveToFrontButton = gui.initChildButton(row2Box, MOVE_TO_FRONT_ICON.toString(), MOVE_TO_FRONT_TOOLTIP.toString(), true);

	// ROW 3
	row3Box = new VBox();
	backgroundColorLabel = new Label("Background Color");
	backgroundColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
	row3Box.getChildren().add(backgroundColorLabel);
	row3Box.getChildren().add(backgroundColorPicker);

	// ROW 4
	row4Box = new VBox();
	fillColorLabel = new Label("Fill Color");
	fillColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
	row4Box.getChildren().add(fillColorLabel);
	row4Box.getChildren().add(fillColorPicker);
	
	// ROW 5
	row5Box = new VBox();
	outlineColorLabel = new Label("Outline Color");
	outlineColorPicker = new ColorPicker(Color.valueOf(BLACK_HEX));
	row5Box.getChildren().add(outlineColorLabel);
	row5Box.getChildren().add(outlineColorPicker);
	
	// ROW 6
	row6Box = new VBox();
	outlineThicknessLabel = new Label("Outline Thickness");
	outlineThicknessSlider = new Slider(0, 10, 1);
	row6Box.getChildren().add(outlineThicknessLabel);
	row6Box.getChildren().add(outlineThicknessSlider);
	
	// ROW 7
	row7Box = new HBox();
	snapshotButton = gui.initChildButton(row7Box, SNAPSHOT_ICON.toString(), SNAPSHOT_TOOLTIP.toString(), false);
	
	// NOW ORGANIZE THE EDIT TOOLBAR
	editToolbar.getChildren().add(row1Box);
	editToolbar.getChildren().add(row2Box);
	editToolbar.getChildren().add(row3Box);
	editToolbar.getChildren().add(row4Box);
	editToolbar.getChildren().add(row5Box);
	editToolbar.getChildren().add(row6Box);
	editToolbar.getChildren().add(row7Box);
	
	// WE'LL RENDER OUR STUFF HERE IN THE CANVAS
	canvas = new Pane();
	debugText = new Text();
	canvas.getChildren().add(debugText);
	debugText.setX(100);
	debugText.setY(100);
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	DataManager data = (DataManager)app.getDataComponent();
	data.setShapes(canvas.getChildren());

	// AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
	((BorderPane)workspace).setLeft(editToolbar);
	((BorderPane)workspace).setCenter(canvas);
    }
    
    public void setDebugText(String text) {
	debugText.setText(text);
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
	fillColorPicker.getStyleClass().add(CLASS_BUTTON);
	outlineColorPicker.getStyleClass().add(CLASS_BUTTON);
	backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);
	
	editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
	row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row2Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	row3Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	backgroundColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	
	row4Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	fillColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row5Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	outlineColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row6Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
	outlineThicknessLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row7Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
	DataManager dataManager = (DataManager)app.getDataComponent();
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
    
    public void loadSelectedShapeSettings(Shape shape) {
	if (shape != null) {
	    Color fillColor = (Color)shape.getFill();
	    Color strokeColor = (Color)shape.getStroke();
	    double lineThickness = shape.getStrokeWidth();
	    fillColorPicker.setValue(fillColor);
	    outlineColorPicker.setValue(strokeColor);
	    outlineThicknessSlider.setValue(lineThickness);	    
	}
    }
}
