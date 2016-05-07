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
import jd.jdLine;
import jd.jdMet;
import jd.jdVar;
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
    
    // THE PARENTS
    ArrayList<ArrayList<String>> parents;
    
    // THE INTERFACES THAT THE CLASS IMPLEMENT
    //ArrayList<String> ipms;
    
    // THE AGGREGATES
    ArrayList<ArrayList<String>> aggs;
    
    // THE USES
    ArrayList<ArrayList<String>> uses;
    
    // THE VARIABLES
    ArrayList<ArrayList<jdVar>> vars;
    
    // THE METHODS
    ArrayList<ArrayList<jdMet>> mets;
    
    // THE LINES
    ArrayList<ArrayList<jdLine>> lines;

    //If is in design
    ArrayList<Boolean> inDesign;

    // THIS IS THE SHAPE CURRENTLY SELECTED
    Node selectedItem;

    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;

    boolean isTest = false;
    
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    // THIS IS THE WORKSPACE
    Workspace ws;
    
    // USING THIS WHEN THE SHAPE IS SELECTED
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
        
	// NO PANE STARTS OUT AS SELECTED
	selectedItem = null;

        // INITIALIZE THE LISTS
        names = new ArrayList();
        packages = new ArrayList();
        parents = new ArrayList();
        //ipms = new ArrayList();
        aggs = new ArrayList();
        uses = new ArrayList();
        vars = new ArrayList();
        mets = new ArrayList();
        lines = new ArrayList();
        inDesign = new ArrayList();
        
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
    
    public ObservableList<Node> getPanes() {
	return panes;
    }
 
    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void reset() {
	//setState(SELECTING_SHAPE);
	selectedItem = null;
	
        if(panes != null) {
            panes.clear();
        }
        names.clear();
        packages.clear();
        parents.clear();
        //ipms.clear();
        aggs.clear();
        uses.clear();
        vars.clear();
        mets.clear();
        lines.clear();
        inDesign.clear();
        if(app.getWorkspaceComponent() != null) {
            ((Workspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
        }
    }

    public void setPanes(ObservableList<Node> initPanes) {
	panes = initPanes;
    }
    
    public void addClassPane(VBox vb) {
        panes.add(vb);
        initialName("");
        initialPackage("");
        initialIMP("");
        inDesign.add(Boolean.FALSE);
        parents.add(new ArrayList());
        aggs.add(new ArrayList());
        uses.add(new ArrayList());
        mets.add(new ArrayList());
        vars.add(new ArrayList());
        lines.add(new ArrayList());
    }    
    
    public void setSelected(Node n) {
        selectedItem = n;
        if(selectedItem != null) {
            reloadEditPane();
        } else {
            ws = (Workspace) app.getWorkspaceComponent();
            ws.reloadNameText("");
            ws.reloadPackageText("");
            ws.uncheckParentChoice();
            ws.setVarTable(new ArrayList());
            ws.setMetTable(new ArrayList());
        }
    }
    
    public Node getSelected() {
        return selectedItem;
    }
    
    public void editName(String name) {
        int index = panes.indexOf(selectedItem);
        String oldName = names.get(index);
        boolean existed = false;
        for(int i = 0; i < names.size(); i ++) {
            if(names.get(i).equals(name) && packages.get(i).equals(packages.get(index))) {
                if(i != index)
                    existed = true;
            }
        }
        if(!existed) {
            names.set(index, name);
            VBox selectedPane = (VBox) selectedItem;
            FlowPane namePane = (FlowPane) selectedPane.getChildren().get(0);
            //VBox namePane = (VBox) selectedPane.getChildren().get(0);
            Text nameText = new Text(names.get(index));
            namePane.getChildren().clear();
            namePane.getChildren().add(nameText);
            ws.removeParentChoice(oldName);
            ws.addParentChoice(name);
            updateParents(oldName, name);
        }        
    }
    
    // UPDATES THE PARENT NAMES WHILE A CLASS'S NAME IS BEING EDITED
    private void updateParents(String oldName, String newName) {
        for(int i = 0; i < parents.size(); i++) {
            ArrayList<String> prt = parents.get(i);
            for(int j = 0; j < prt.size(); j++) {
                if(prt.get(j).equals(oldName)) {
                    prt.remove(j);
                    prt.add(newName);
                    j = prt.size() + 1;
                }
            }
        }
    } 
    
    // For adding interfaces only (Shows "{interface}" on the boxes)
    public void editName1(String name) {
        int index = panes.indexOf(selectedItem);
        names.set(index, name);
        VBox selectedPane = (VBox) selectedItem;
        FlowPane namePane = (FlowPane) selectedPane.getChildren().get(0);
        //VBox namePane = (VBox) selectedPane.getChildren().get(0);
        Text nameText = new Text(names.get(index));
        namePane.getChildren().clear();
        namePane.getChildren().add(nameText);
    }
    
    private void initialName(String name) {
        names.add(name);
    }
    
    
    private void initialIMP(String ipm) {
        //ipms.add(ipm);
    }
    
    
    public ArrayList<String> getNames() {
        return names;
    }
    
    public ArrayList<Boolean> getID() {
        return inDesign;
    }
    
    
    public void editPackage(String pname) {
        int index = panes.indexOf(selectedItem);
        boolean existed = false;
        for(int i = 0; i < names.size(); i ++) {
            if(packages.get(i).equals(pname) && names.get(i).equals(names.get(index))) {
                if(i != index)
                    existed = true;
            }
        }
        if(!existed)
            packages.set(index, pname);
    }

    private void initialPackage(String pname) {
        packages.add(pname);
    }
    
    public ArrayList<String> getPackages() {
        return packages;
    }

    public String getPackage(int i) {
        return packages.get(i);
    }
    
    public String getName(int i) {
        return names.get(i);
    }
    public ArrayList<String> getParents(int i) {
        return parents.get(i);
    }
    
    public void addParent(String p, int i) {
        parents.get(i).add(p);
    }
    
    public void removeParent(String p, int i) {
        for(int j = 0; j < parents.get(i).size(); j++) {
            if(parents.get(i).get(j).equals(p)) {
                parents.get(i).remove(j);
                j = parents.get(i).size() + 1;
            }
        }
    }
    
    public boolean getID(int i) {
        return inDesign.get(i);
    }
    
    public void setID(int i, boolean value) {
        inDesign.set(i, value);
    }
    
    public ArrayList<jdMet> getMets(int i) {
        return mets.get(i);
    }
    
    public ArrayList<jdVar> getVars(int i) {
        return vars.get(i);
    }
    
    public ArrayList<jdLine> getLines(int i) {
        return lines.get(i);
    }
    
    public ArrayList<String> getAggs(int i) {
        return aggs.get(i);
    }
    
    public ArrayList<String> getUses(int i) {
        return uses.get(i);
    }
    
    public void addVar1(jdVar v, int i) {
        vars.get(i).add(v);
    }
    
    public void addVar(jdVar v, int i) {
        vars.get(i).add(v);

        // Then shows in the ui
        VBox selectedPane = (VBox) selectedItem;
        VBox varPane = (VBox) selectedPane.getChildren().get(1);
        //VBox namePane = (VBox) selectedPane.getChildren().get(0);
        String varInfo = "";
        // First the access
        if(v.getAccess().equals("public")) {
            varInfo += "+";
        } else if(v.getAccess().equals("protected")) {
            varInfo += "#";
        } else if(v.getAccess().equals("private")) {
            varInfo += "-";
        }
        // Then the Static
        if(v.getStatic()) 
            varInfo += "$";
        //Then the name
        varInfo += v.getName();
        // Then the type
        if(!v.getType().isEmpty()) {
            varInfo += " : ";
            varInfo += v.getType();
        }
                
        Text varText = new Text(varInfo);
        varPane.getChildren().add(varText);  
    }
    
    // i is the index of the class, j is the index of the variable
    public void changeVar(jdVar v, int i, int j) {
        // Change the info in the ui
        VBox selectedPane = (VBox) selectedItem;
        VBox varPane = (VBox) selectedPane.getChildren().get(1);
        String varInfo = "";
        // First the access
        if(v.getAccess().equals("public")) {
            varInfo += "+";
        } else if(v.getAccess().equals("protected")) {
            varInfo += "#";
        } else if(v.getAccess().equals("private")) {
            varInfo += "-";
        }
        // Then the Static
        if(v.getStatic()) 
            varInfo += "$";
        //Then the name
        varInfo += v.getName();
        // Then the type
        if(!v.getType().isEmpty()) {
            varInfo += " : ";
            varInfo += v.getType();
        }
                
        Text varText = new Text(varInfo);
        varPane.getChildren().set(j, varText);
        
        
        vars.get(i).set(j, v);
    }
    
    public void delVar(jdVar v, int i) {
        int index = vars.get(i).indexOf(v);
        vars.get(i).remove(v);
        VBox selectedPane = (VBox) selectedItem;
        VBox varPane = (VBox) selectedPane.getChildren().get(1);
        varPane.getChildren().remove(index);
    }
    
    
    public void addMet1(jdMet m, int i) {
        mets.get(i).add(m);
    }
    
    public void addMet(jdMet m, int i) {
        mets.get(i).add(m);
        
        // Then shows in the ui
        VBox selectedPane = (VBox) selectedItem;
        VBox metPane = (VBox) selectedPane.getChildren().get(2);
        //VBox namePane = (VBox) selectedPane.getChildren().get(0);
        String metInfo = "";
        // First the access
        if(m.getAccess().equals("public")) {
            metInfo += "+";
        } else if(m.getAccess().equals("protected")) {
            metInfo += "#";
        } else if(m.getAccess().equals("private")) {
            metInfo += "-";
        }
        // Then the Static
        if(m.getStatic()) 
            metInfo += "$";
        //Then the name
        metInfo += m.getName();
        //Then the arguments
        metInfo += "(";
        for(int j = 0; j < m.getArgs().size(); j++) {
            String argName = (String) m.getArgs().get(j);
            if(!argName.isEmpty()) {
                metInfo += "arg";
                int argNum = j+1;
                metInfo += argNum;
                metInfo += " : ";
                metInfo += argName;
                if(j != m.getArgs().size() - 1)
                    metInfo += ", ";
            }
        }
        if(metInfo.endsWith(", ")) {
            metInfo = metInfo.substring(0, metInfo.length() - 2);
        }
        
        metInfo += ")";
        
        // Then the type
        if(!m.getType().isEmpty()) {
            metInfo += " : ";
            metInfo += m.getType();
        }
          
        //Then the abstract
        if(m.getAbstract())
            metInfo += " {abstract}";
        
        Text metText = new Text(metInfo);
        metPane.getChildren().add(metText);  
    }
   
    // i is the index of the class, j is the index of the variable
    public void changeMet(jdMet m, int i, int j) {
        // Change the info in the ui
        VBox selectedPane = (VBox) selectedItem;
        VBox metPane = (VBox) selectedPane.getChildren().get(2);
        String metInfo = "";
        // First the access
        if(m.getAccess().equals("public")) {
            metInfo += "+";
        } else if(m.getAccess().equals("protected")) {
            metInfo += "#";
        } else if(m.getAccess().equals("private")) {
            metInfo += "-";
        }
        // Then the Static
        if(m.getStatic()) 
            metInfo += "$";
        //Then the name
        metInfo += m.getName();
        //Then the arguments
        metInfo += "(";
        for(int k = 0; k < m.getArgs().size(); k++) {
            String argName = (String) m.getArgs().get(k);
            if(!argName.isEmpty()) {
                metInfo += "arg";
                int argNum = k+1;
                metInfo += argNum;
                metInfo += " : ";
                metInfo += argName;
                if(k != m.getArgs().size() - 1)
                    metInfo += ", ";
            }
        }
        if(metInfo.endsWith(", ")) {
            metInfo = metInfo.substring(0, metInfo.length() - 2);
        }
        
        metInfo += ")";
        
        // Then the type
        if(!m.getType().isEmpty()) {
            metInfo += " : ";
            metInfo += m.getType();
        }
        
        //Then the abstract
        if(m.getAbstract())
            metInfo += " {abstract}";
        
        Text metText = new Text(metInfo);
        metPane.getChildren().set(j, metText);
        
        mets.get(i).set(j, m);
    }
    
    public void delMet(jdMet m, int i) {
        int index = mets.get(i).indexOf(m);
        mets.get(i).remove(m);
        VBox selectedPane = (VBox) selectedItem;
        VBox metPane = (VBox) selectedPane.getChildren().get(2);
        metPane.getChildren().remove(index);
    }
        
    public void addLine(jdLine l, int i) {
        lines.get(i).add(l);
    }
    
    public void addAgg(String a, int i) {
        aggs.get(i).add(a);
    }
    
    
    public void addUse(String u, int i) {
        uses.get(i).add(u);
    }
    
    private void reloadEditPane() {
        int i = panes.indexOf(selectedItem);
        ws = (Workspace) app.getWorkspaceComponent();
        ws.reloadNameText(names.get(i));
        ws.reloadPackageText(packages.get(i));
        ws.uncheckParentChoice();
        ArrayList<String> prts = getParents(i);
        for(int j = 0; j < prts.size(); j++) {
            ws.checkParentChoice(prts.get(j));
        }
        ws.setVarTable(getVars(i));
        ws.setMetTable(getMets(i));
    }
    
    public boolean isTest() {
        return isTest;
    }
    
    public void setTest(boolean b) {
        isTest = b;
    }
}
