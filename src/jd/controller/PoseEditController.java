package jd.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckBox;
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
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jd.data.DataManager;
import jd.file.FileManager;
import jd.gui.Workspace;
import jd.gui.metDialog;
import jd.gui.varDialog;
import jd.jdMet;
import jd.jdVar;
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
import saf.ui.AppYesNoCancelDialogSingleton;

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
    
    varDialog vid;
    metDialog md;
    
    Stage psg;
    
    public PoseEditController(AppTemplate initApp, Stage psg) {
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
        this.psg = psg;
        
        vid = new varDialog(psg);
        //md = new metDialog(psg);
    }
    
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
            
                        
            canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // do nothing
                    int y = 1+1;
                }
            });
        }
    }

    public void handleAddEClassRequest() {
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
                    drawEPane(bX, bY, false);
                }
            }); 
            
            canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // do nothing
                    int y = 1+1;
                }
            }); 
        }
    }    
        
    public void drawEPane(double X, double Y, boolean isIF) {
        // MARK THE FILE AS EDITED
        AppFileController afc = app.getGUI().getAFC();
        afc.markAsEdited(app.getGUI());
        VBox vb = new VBox();
        vb.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        vb.setPrefSize(100, 50);
        FlowPane namePane = new FlowPane();
        Text external = new Text("[EXTERNAL]");
        namePane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        vb.getChildren().add(namePane);
        VBox varPane = new VBox();
        varPane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        vb.getChildren().add(varPane);
        VBox metPane = new VBox();
        metPane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        vb.getChildren().add(metPane);
        VBox exPane = new VBox();
        exPane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        exPane.getChildren().add(external);
        vb.getChildren().add(exPane);
        vb.setLayoutX(X);
        vb.setLayoutY(Y);
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
            dataManager.editName1("{interface}");
            Workspace ws = (Workspace) app.getWorkspaceComponent();
            ws.reloadNameText("{interface}");
        }
        dataManager.setID(dataManager.getPanes().indexOf(selectedItem), false);
    }    
        
        
    private void drawPane(double X, double Y, boolean isIF) {
        // MARK THE FILE AS EDITED
        AppFileController afc = app.getGUI().getAFC();
        afc.markAsEdited(app.getGUI());
        VBox vb = new VBox();
        vb.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        vb.setPrefSize(100, 50);
        FlowPane namePane = new FlowPane();
        namePane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        vb.getChildren().add(namePane);
        VBox varPane = new VBox();
        varPane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        vb.getChildren().add(varPane);
        VBox metPane = new VBox();
        metPane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
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
            if(s.isPressed() && s instanceof VBox) {
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
    }

    public void handleNameUpdate(String name) {
        // MARK THE FILE AS EDITED
        AppFileController afc = app.getGUI().getAFC();
        afc.markAsEdited(app.getGUI());
        int i = dataManager.getPanes().indexOf(selectedItem);
        if(i > -1 && i < dataManager.getNames().size() && !dataManager.getName(i).equals(name))
            if(selectedItem instanceof VBox)
                dataManager.editName(name);
    }

    public void handlePackageUpdate(VBox selectedPane, String text) {
        if(selectedItem instanceof VBox)
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
        if (selectedItem instanceof VBox) {
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

    public void handleAddVarRequest() {
        DataManager dm = (DataManager) app.getDataComponent();
        int index = dm.getPanes().indexOf(selectedItem);
        if(index > -1 && index < dm.getPanes().size() && selectedItem instanceof VBox){
            //ArrayList<jdVar> list = dm.getVars(index);
            vid.showAddVarDialog();

            // DID THE USER CONFIRM?
            if (vid.wasCompleteSelected()) {
                // GET THE VARIABLE
                jdVar var = vid.getVar();

                // AND ADD IT AS A ROW TO THE LIST
                //list.add(var);
                dm.addVar(var, index);

                // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
                // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
                // THE SAVE BUTTON IS ENABLED
                AppFileController afc = app.getGUI().getAFC();
                afc.markAsEdited(app.getGUI());
                dm.setSelected(selectedItem);

            }
            else {
                // THE USER MUST HAVE PRESSED CANCEL, SO
                // WE DO NOTHING
            }
        }
    }

    public void handleEditVarRequest(jdVar itemToEdit, Workspace ws) {
        DataManager dm = (DataManager) app.getDataComponent();
        int index = dm.getPanes().indexOf(selectedItem);
        if(index > -1 && index < dm.getPanes().size() && selectedItem instanceof VBox){
            ArrayList<jdVar> list = dm.getVars(index);
            vid.showEditVarDialog(itemToEdit);
            
                    
            // DID THE USER CONFIRM?
            if (vid.wasCompleteSelected()) {
                int i = list.indexOf(itemToEdit);
                // UPDATE THE SCHEDULE ITEM
                jdVar var = vid.getVar();
                //list.set(i, var);
                dm.changeVar(var, index, i);
                
                // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
                // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
                // THE SAVE BUTTON IS ENABLED
                AppFileController afc = app.getGUI().getAFC();
                afc.markAsEdited(app.getGUI());
                dm.setSelected(selectedItem);
            }
            else {
                // THE USER MUST HAVE PRESSED CANCEL, SO
                // WE DO NOTHING
            } 
        }
    }

    public void handleRemoveVarRequest(Workspace ws, jdVar itemToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show("Remove Variable", "Are you sure to remove this variable?");
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        DataManager dm = (DataManager) app.getDataComponent();
        int index = dm.getPanes().indexOf(selectedItem);
        
        // IF THE USER SAID YES, THEN REMOVE IT
        if (selection.equals(AppYesNoCancelDialogSingleton.YES) && index > -1 && index < dm.getPanes().size()) { 
            //dm.getVars(index).remove(itemToRemove);
            dm.delVar(itemToRemove, index);
            
            // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            AppFileController afc = app.getGUI().getAFC();
            afc.markAsEdited(app.getGUI());
            dm.setSelected(selectedItem);
        }
    }
    
    public void handleAddMetRequest(Workspace ws) {
        DataManager dm = (DataManager) app.getDataComponent();
        md = new metDialog(this.psg, ws.getArgSize());
        int index = dm.getPanes().indexOf(selectedItem);
        if(index > -1 && index < dm.getPanes().size() && selectedItem instanceof VBox){
            //ArrayList<jdVar> list = dm.getVars(index);
            md.showAddMetDialog();

            // DID THE USER CONFIRM?
            if (md.wasCompleteSelected()) {
                // GET THE VARIABLE
                jdMet met = md.getMet();

                // AND ADD IT AS A ROW TO THE LIST
                //list.add(var);
                dm.addMet(met, index);

                // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
                // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
                // THE SAVE BUTTON IS ENABLED
                AppFileController afc = app.getGUI().getAFC();
                afc.markAsEdited(app.getGUI());
                dm.setSelected(selectedItem);

            }
            else {
                // THE USER MUST HAVE PRESSED CANCEL, SO
                // WE DO NOTHING
            }
        }
    }

    public void handleEditMetRequest(jdMet itemToEdit, Workspace ws) {
        DataManager dm = (DataManager) app.getDataComponent();
        md = new metDialog(this.psg, ws.getArgSize());
        int index = dm.getPanes().indexOf(selectedItem);
        if(index > -1 && index < dm.getPanes().size() && selectedItem instanceof VBox){
            ArrayList<jdMet> list = dm.getMets(index);
            md.showEditMetDialog(itemToEdit);
            
                    
            // DID THE USER CONFIRM?
            if (md.wasCompleteSelected()) {
                int i = list.indexOf(itemToEdit);
                // UPDATE THE SCHEDULE ITEM
                jdMet met = md.getMet();
                //list.set(i, var);
                dm.changeMet(met, index, i);
                
                // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
                // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
                // THE SAVE BUTTON IS ENABLED
                AppFileController afc = app.getGUI().getAFC();
                afc.markAsEdited(app.getGUI());
                dm.setSelected(selectedItem);
            }
            else {
                // THE USER MUST HAVE PRESSED CANCEL, SO
                // WE DO NOTHING
            } 
        }
    }

    public void handleRemoveMetRequest(Workspace ws, jdMet itemToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show("Remove Method", "Are you sure to remove this method?");
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        DataManager dm = (DataManager) app.getDataComponent();
        int index = dm.getPanes().indexOf(selectedItem);
        
        // IF THE USER SAID YES, THEN REMOVE IT
        if (selection.equals(AppYesNoCancelDialogSingleton.YES) && index > -1 && index < dm.getPanes().size()) { 
            //dm.getVars(index).remove(itemToRemove);
            dm.delMet(itemToRemove, index);
            
            // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            AppFileController afc = app.getGUI().getAFC();
            afc.markAsEdited(app.getGUI());
            dm.setSelected(selectedItem);
        }
    }
    
    public void handleResizeRequest() {
        //selectedItem.
        BorderPane jdWorkspace = (BorderPane) app.getGUI().getAppPane().getCenter();
        ScrollPane SP = (ScrollPane) jdWorkspace.getCenter();
        Pane canvas = (Pane) SP.getContent();
        
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //do nothing
            }
        });
        
        
        
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double eX = mouseEvent.getX();
                double eY = mouseEvent.getY();
                if(selectedItem instanceof VBox) {
                    VBox vb = (VBox) selectedItem;
                    double bX = vb.getLayoutX() + vb.getTranslateX();
                    double bY = vb.getLayoutY() + vb.getTranslateY();
                    double rbX = bX + vb.getWidth();
                    double rbY = bY + vb.getHeight();
                    //if(eX >= rbX - 2 && eX <= rbX + 2 && eY >= rbY - 2 && eY <= rbY + 2) {
                        //canvas.setCursor(Cursor.NW_RESIZE);
                        double eW = eX - (bX);
                        double eH = eY - (bY);                        
                        resize(canvas, vb, eW, eH);
                    //}
                }
            }
        });
    }
    
    private void resize(Pane canvas, VBox vb, double w, double h) {
        // MARK THE FILE AS EDITED
        AppFileController afc = app.getGUI().getAFC();
        afc.markAsEdited(app.getGUI());
        if(h > 50) {
            vb.setPrefHeight(h);
        }
        if(w > 100) {
            vb.setPrefWidth(w);
        }
    }

    public void handleRemoveRequest() {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show("Remove Class/Interface Box", "Are you sure to remove this box?");
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        DataManager dm = (DataManager) app.getDataComponent();
        if(selectedItem instanceof VBox) {
            int index = dm.getPanes().indexOf(selectedItem);
            // IF THE USER SAID YES, THEN REMOVE IT
            if (selection.equals(AppYesNoCancelDialogSingleton.YES) && index > -1 && index < dm.getPanes().size()) { 
                //dm.getVars(index).remove(itemToRemove);
                dm.delPane(index);

                // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
                // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
                // THE SAVE BUTTON IS ENABLED
                AppFileController afc = app.getGUI().getAFC();
                afc.markAsEdited(app.getGUI());

                selectedItem = null;
            }
        }        
    }

    public void handleGridRequest(CheckBox gb) {
        boolean needGrid = gb.isSelected();
        BorderPane jdWorkspace = (BorderPane) app.getGUI().getAppPane().getCenter();
        ScrollPane SP = (ScrollPane) jdWorkspace.getCenter();
        Pane canvas = (Pane) SP.getContent();
        Workspace ws = (Workspace) app.getWorkspaceComponent();
        ws.setUpGrid(canvas.getWidth() / 10, canvas.getHeight() / 10, needGrid);
    }
    
    
}
