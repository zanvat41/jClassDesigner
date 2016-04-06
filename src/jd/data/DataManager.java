package jd.data;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import jd.gui.Workspace;
import saf.components.AppDataComponent;
import saf.AppTemplate;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class DataManager implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES
    
    // THESE ARE THE PANES TO DRAW
    ObservableList<Node> panes;
    
    // THE NAMES OF THE PANES
    ArrayList<String> names;
    
    // THE PACKAGES OF THE CLASSES / INTERFACES
    ArrayList<String> packages;
    
    // THE BACKGROUND COLOR
    Color backgroundColor;
    
    // AND NOW THE EDITING DATA

    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Shape newShape;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    Node selectedItem;

    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;


    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    // THIS IS THE WORKSPACE
    Workspace ws;
    
    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
        
	// NO SHAPE STARTS OUT AS SELECTED
	newShape = null;
	selectedItem = null;

        // INITIALIZE THE LISTS
        names = new ArrayList();
        packages = new ArrayList();
        
	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	currentBorderWidth = 1;
	
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
    
    /*public ObservableList<Node> getShapes() {
	return shapes;
    }

    public Color getBackgroundColor() {
	return backgroundColor;
    }
    
    public Color getCurrentFillColor() {
	return currentFillColor;
    }

    public Color getCurrentOutlineColor() {
	return currentOutlineColor;
    }

    public double getCurrentBorderWidth() {
	return currentBorderWidth;
    }
    
    public void setShapes(ObservableList<Node> initShapes) {
	shapes = initShapes;
    }
    
    public void setBackgroundColor(Color initBackgroundColor) {
	backgroundColor = initBackgroundColor;
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
	Background background = new Background(fill);
	//canvas.setBackground(background);
    }

    public void setCurrentFillColor(Color initColor) {
	currentFillColor = initColor;
	if (selectedShape != null)
	    selectedShape.setFill(currentFillColor);
    }

    public void setCurrentOutlineColor(Color initColor) {
	currentOutlineColor = initColor;
	if (selectedShape != null) {
	    selectedShape.setStroke(initColor);
	}
    }

    public void setCurrentOutlineThickness(int initBorderWidth) {
	currentBorderWidth = initBorderWidth;
	if (selectedShape != null) {
	    selectedShape.setStrokeWidth(initBorderWidth);
	}
    }
    
    public void removeSelectedShape() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    selectedShape = null;
	}
    }
    
    public void moveSelectedShapeToBack() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    if (shapes.isEmpty()) {
		shapes.add(selectedShape);
	    }
	    else {
		ArrayList<Node> temp = new ArrayList();
		temp.add(selectedShape);
		for (Node node : shapes)
		    temp.add(node);
		shapes.clear();
		for (Node node : temp)
		    shapes.add(node);
	    }
	}
    }
    
    public void moveSelectedShapeToFront() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    shapes.add(selectedShape);
	}
    }*/
 
    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void reset() {
	//setState(SELECTING_SHAPE);
	newShape = null;
	selectedItem = null;

	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	
	//shapes.clear();
	//((Workspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }

    /*public void selectSizedShape() {
	if (selectedShape != null)
	    unhighlightShape(selectedShape);
	selectedShape = newShape;
	highlightShape(selectedShape);
	newShape = null;
	//if (state == SIZING_SHAPE) {
	    //state = ((Draggable)selectedShape).getStartingState();
	//}
    }
    
    public void unhighlightShape(Shape shape) {
	selectedShape.setEffect(null);
    }
    
    public void highlightShape(Shape shape) {
	shape.setEffect(highlightedEffect);
    }

    public void startNewRectangle(int x, int y) {
	//DraggableRectangle newRectangle = new DraggableRectangle();
	//newRectangle.start(x, y);
	//newShape = newRectangle;
	initNewShape();
    }

    public void startNewEllipse(int x, int y) {
	//DraggableEllipse newEllipse = new DraggableEllipse();
	//newEllipse.start(x, y);
	//newShape = newEllipse;
	initNewShape();
    }

    public void initNewShape() {
	// DESELECT THE SELECTED SHAPE IF THERE IS ONE
	if (selectedShape != null) {
	    unhighlightShape(selectedShape);
	    selectedShape = null;
	}

	// USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	//newShape.setFill(workspace.getFillColorPicker().getValue());
	//newShape.setStroke(workspace.getOutlineColorPicker().getValue());
	//newShape.setStrokeWidth(workspace.getOutlineThicknessSlider().getValue());
	
	// ADD THE SHAPE TO THE CANVAS
	shapes.add(newShape);
	
	// GO INTO SHAPE SIZING MODE
	//state = PoseMakerState.SIZING_SHAPE;
    }

    public Shape getNewShape() {
	return newShape;
    }

    public Shape getSelectedShape() {
	return selectedShape;
    }

    public void setSelectedShape(Shape initSelectedShape) {
	selectedShape = initSelectedShape;
    }

    public Shape selectTopShape(int x, int y) {
	Shape shape = getTopShape(x, y);
	if (shape == selectedShape)
	    return shape;
	
	if (selectedShape != null) {
	    unhighlightShape(selectedShape);
	}
	if (shape != null) {
	    highlightShape(shape);
	    Workspace workspace = (Workspace)app.getWorkspaceComponent();
	    //workspace.loadSelectedShapeSettings(shape);
	}
	selectedShape = shape;
	if (shape != null) {
	    //((Draggable)shape).start(x, y);
	}
	return shape;
    }

    public Shape getTopShape(int x, int y) {
	for (int i = shapes.size() - 1; i >= 0; i--) {
	    Shape shape = (Shape)shapes.get(i);
	    if (shape.contains(x, y)) {
		return shape;
	    }
	}
	return null;
    }

    public void addShape(Shape shapeToAdd) {
	shapes.add(shapeToAdd);
    }

    public void removeShape(Shape shapeToRemove) {
	shapes.remove(shapeToRemove);
    }*/

    public void setPanes(ObservableList<Node> initPanes) {
	panes = initPanes;
    }
    
    public ObservableList<Node> getPanes() {
	return panes;
    }
    
    public void addClassPane(VBox vb) {
        panes.add(vb);
        initialName("");
        initialPackage("");
    }    
    
    public void setSelected(Node n) {
        selectedItem = n;
        if(selectedItem != null) {
            reloadEditPane();
        }
    }
    
    public Node getSelected() {
        return selectedItem;
    }
    
    public void editName(String name) {
        int i = panes.indexOf(selectedItem);
        names.set(i, name);
        VBox selectedPane = (VBox) selectedItem;
        FlowPane namePane = (FlowPane) selectedPane.getChildren().get(0);
        Text nameText = new Text(names.get(i));
        //nameText.setLayoutX(namePane.getLayoutX());
        //nameText.setLayoutY(namePane.getLayoutY());
        namePane.getChildren().clear();
        namePane.getChildren().add(nameText);
    }

    private void initialName(String name) {
        names.add(name);
    }
    
    public ArrayList<String> getNames() {
        return names;
    }

    public void editPackage(String pname) {
        int i = panes.indexOf(selectedItem);
        packages.set(i, pname);
        //VBox selectedPane = (VBox) selectedItem;
        //FlowPane namePane = (FlowPane) selectedPane.getChildren().get(0);
        //Text nameText = new Text(packages.get(i));
        //nameText.setLayoutX(namePane.getLayoutX());
        //nameText.setLayoutY(namePane.getLayoutY());
        //namePane.getChildren().clear();
        //namePane.getChildren().add(nameText);
    }

    private void initialPackage(String pname) {
        packages.add(pname);
    }
    
    public ArrayList<String> getPackages() {
        return packages;
    }

    
    
    private void reloadEditPane() {
        int i = panes.indexOf(selectedItem);
        ws = (Workspace) app.getWorkspaceComponent();
        ws.reloadNameText(names.get(i));
        ws.reloadPackageText(packages.get(i));
    }
}
