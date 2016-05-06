package jd.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import jd.data.DataManager;
import jd.file.FileManager;
import jd.gui.Workspace;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import saf.controller.AppFileController;
import static saf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppStartupConstants.PATH_WORK;
import saf.ui.AppMessageDialogSingleton;

/**
 * This class responds to interactions with other UI pose editing controls.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public class PoseEditController {
    AppTemplate app;
        
    DataManager dataManager;
    
    private double bX, bY; // Starting point for drawing
    private double eX, eY; // Ending point for drawing
    private double bX1, bY1; // Starting point for selecting
    private double bX2, bY2; // Starting point for selecting and dragging
    private double eX1, eY1; // Ending point for selecting and dragging
    private double scX, scY;
    
    private Node selectedItem = null;
    private Node lastItem = null;
    
    private boolean selected = false;
    
    private boolean enabled = true;
    
    private Effect highlightedEffect;
    
    public PoseEditController(AppTemplate initApp) {
	app = initApp;
	dataManager = (DataManager)app.getDataComponent();
        
        // THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;
    }
    
    /*public void processSelectSelectionTool() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT);
	
	// CHANGE THE STATE
	dataManager.setState(PoseMakerState.SELECTING_SHAPE);	
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void processRemoveSelectedShape() {
	// REMOVE THE SELECTED SHAPE IF THERE IS ONE
	dataManager.removeSelectedShape();
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processSelectRectangleToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(PoseMakerState.STARTING_RECTANGLE);

	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void processSelectEllipseToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(PoseMakerState.STARTING_ELLIPSE);

	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void processMoveSelectedShapeToBack() {
	dataManager.moveSelectedShapeToBack();
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processMoveSelectedShapeToFront() {
	dataManager.moveSelectedShapeToFront();
	app.getGUI().updateToolbarControls(false);
    }
        
    public void processSelectFillColor() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getFillColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setCurrentFillColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    public void processSelectOutlineColor() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getOutlineColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setCurrentOutlineColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}    
    }
    
    public void processSelectBackgroundColor() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Color selectedColor = workspace.getBackgroundColorPicker().getValue();
	if (selectedColor != null) {
	    dataManager.setBackgroundColor(selectedColor);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    public void processSelectOutlineThickness() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	int outlineThickness = (int)workspace.getOutlineThicknessSlider().getValue();
	dataManager.setCurrentOutlineThickness(outlineThickness);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processSnapshot() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
	File file = new File("Pose.png");
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
	catch(IOException ioe) {
	    ioe.printStackTrace();
	}
    }*/

    
    public void handleAddClassRequest() {
        if(enabled) {                
            BorderPane jdWorkspace = (BorderPane) app.getGUI().getAppPane().getCenter();
            ScrollPane SP = (ScrollPane) jdWorkspace.getCenter();
            Pane canvas = (Pane) SP.getContent();
            
            // THEN DRAW
            canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    bX = mouseEvent.getX();
                    bY = mouseEvent.getY();
                    drawPane(bX, bY, false);
                }
            }); 
        }
    }
    
        public void handleAddInterfaceRequest() {
        if(enabled) {                
            BorderPane jdWorkspace = (BorderPane) app.getGUI().getAppPane().getCenter();
            ScrollPane SP = (ScrollPane) jdWorkspace.getCenter();
            Pane canvas = (Pane) SP.getContent();
            
            // THEN DRAW
            canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    bX = mouseEvent.getX();
                    bY = mouseEvent.getY();
                    drawPane(bX, bY, true);
                }
            }); 
        }
    }

    private void drawPane(double X, double Y, boolean isIF) {
        // MARK THE FILE AS EDITED
        AppFileController afc = app.getGUI().getAFC();
        afc.markAsEdited(app.getGUI());
        VBox vb = new VBox();
        FlowPane namePane = new FlowPane();
        namePane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        namePane.setMinSize(100, 50);
        namePane.setPrefSize(100, 50);
        //namePane.setMaxSize(150, 50);
        vb.getChildren().add(namePane);
        FlowPane varPane = new FlowPane();
        //varPane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        //varPane.setMinSize(150, 50);
        vb.getChildren().add(varPane);
        FlowPane metPane = new FlowPane();
        //metPane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        //metPane.setMinSize(150, 50);
        vb.getChildren().add(metPane);
        vb.setLayoutX(X);
        vb.setLayoutY(Y);
        //canvas.getChildren().add(vb);
        dataManager.addClassPane(vb);
        selectedItem = vb;
        if(selected){
            // Disselect the previous selected item
            lastItem.setEffect(null);
        }
        selectedItem.setEffect(highlightedEffect);
        lastItem = selectedItem;
        selected = true;
        dataManager.setSelected(selectedItem);
        if(isIF) {
            //System.out.println(dataManager.getPanes().indexOf(selectedItem));
            dataManager.editName1("{interface}");
            Workspace ws = (Workspace) app.getWorkspaceComponent();
            ws.reloadNameText("{interface}");
        }
        dataManager.setID(dataManager.getPanes().indexOf(selectedItem), true);
    }
    
    public void handleSelectRequest() {
        if(enabled) {
            // MARK THE FILE AS EDITED
            AppFileController afc = app.getGUI().getAFC();
            afc.markAsEdited(app.getGUI());
            
            BorderPane jdWorkspace = (BorderPane) app.getGUI().getAppPane().getCenter();
            ScrollPane SP = (ScrollPane) jdWorkspace.getCenter();
            Pane canvas = (Pane) SP.getContent();

        
            // THEN SELECT THE SHAPE
            canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                    public void handle(MouseEvent mouseEvent) {
                    scX = mouseEvent.getSceneX();
                    scY = mouseEvent.getSceneY();
                    bX1 = mouseEvent.getX();
                    bY1 = mouseEvent.getY();
                    select();
                    if(selected) {
                        bX2 = selectedItem.getTranslateX();
                        bY2 = selectedItem.getTranslateY();
                    }
                }
            });
        
            // DRAG AND DROP TO RELOCATE THE SHAPE
            canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (selected) {
                        double offsetX = mouseEvent.getSceneX() - scX;
                        double offsetY = mouseEvent.getSceneY() - scY;
                        double newTranslateX = bX2 + offsetX;
                        double newTranslateY = bY2 + offsetY;
                        if(selectedItem.isPressed()) {
                            selectedItem.setTranslateX(newTranslateX);
                            selectedItem.setTranslateY(newTranslateY);
                        }
                    }
                }
            });
        }
    }
    
    private void select() {
        boolean contains = false;
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ObservableList<Node> panes = dataManager.getPanes();
        for (Iterator<Node> it = panes.iterator(); it.hasNext();) {
            Node s = it.next();
            if(s.isPressed()) {
                selectedItem = s;
                contains = true;
            }
        }
        if (contains) {
            if(selected){
                // Disselect the previous selected item
                lastItem.setEffect(null);
            }
            selectedItem.setEffect(highlightedEffect);
            lastItem = selectedItem;
            selected = true;
        } else {
            if(selected) {
                lastItem.setEffect(null);
                selectedItem = null;
                lastItem = null;
                selected = false;
            }
        }
        if(selected) {
            dataManager.setSelected(selectedItem);
        } else {
            dataManager.setSelected(null);
        }
        //workspace.refreshButtons(selected);
    }

    public void handleNameUpdate(String name) {
        // MARK THE FILE AS EDITED
        AppFileController afc = app.getGUI().getAFC();
        afc.markAsEdited(app.getGUI());
        int i = dataManager.getPanes().indexOf(selectedItem);
        if(i > -1 && i < dataManager.getNames().size() && !dataManager.getName(i).equals(name))
            dataManager.editName(name);
    }

    public void handlePackageUpdate(VBox selectedPane, String text) {
        dataManager.editPackage(text);
    }

    public void handleCodeRequest() throws IOException {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(PATH_WORK));
        dc.setTitle("Choose Directory to Save Code");
        File selectedFile = dc.showDialog(app.getGUI().getWindow());
        //System.out.print(selectedFile.getPath());
        if (selectedFile != null) {
            FileManager fm = (FileManager) app.getFileComponent();
            fm.exportCode(dataManager, selectedFile.getPath());
        }
    }

    public void handlePhotoRequest() throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle("Choose Directory to Save Photo");
        fc.getExtensionFilters().addAll(
	new FileChooser.ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

	File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
        if (selectedFile != null) {
            BorderPane jdWorkspace = (BorderPane) app.getGUI().getAppPane().getCenter();
            ScrollPane SP = (ScrollPane) jdWorkspace.getCenter();
            Pane canvas = (Pane) SP.getContent();
            SnapshotParameters parameters = new SnapshotParameters();
            WritableImage wi = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), wi);
            String path = selectedFile.getPath();
            path += ".png";
            File output = new File(path);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
        }
    }
    
    
    /*private void saveCode(File selectedFile) throws FileNotFoundException {
        int paneNumber = dataManager.getPanes().size();
        PrintWriter pw = new PrintWriter(selectedFile.getPath());
        for(int i = 0; i < paneNumber; i ++) {
            FileManager fm = (FileManager) app.getFileComponent();
            fm.saveCode(dataManager, selectedFile.getPath());
        }
    }*/

    public void handleParentChoice(CheckMenuItem pc) {
        if (selectedItem != null) {
            // MARK THE FILE AS EDITED
            AppFileController afc = app.getGUI().getAFC();
            afc.markAsEdited(app.getGUI());
            int i = dataManager.getPanes().indexOf(selectedItem);
            if(pc.isSelected()) {
                dataManager.addParent(pc.getText(), i);
            } else{
                dataManager.removeParent(pc.getText(), i);
            }
        }
    }
    
}
