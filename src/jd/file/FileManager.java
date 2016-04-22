package jd.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jd.data.DataManager;
import jd.jdMet;
import jd.jdVar;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;


/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class FileManager implements AppFileComponent {
    // FOR JSON LOADING
    static final String JSON_ACCESS = "Access";
    static final String JSON_STATIC = "Static";
    static final String JSON_ABSTRACT = "Abstract";
    static final String JSON_TYPE = "type";
    static final String JSON_ARGS = "args";
    static final String JSON_PANES = "panes";
    static final String JSON_PACKAGE = "package";
    static final String JSON_NAME = "name";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_TX = "tx";
    static final String JSON_TY = "ty";
    static final String JSON_METHOD = "methods";
    static final String JSON_VAR = "vars";
    static final String JSON_PARENT = "parent";
    static final String JSON_IMPLEMENT = "implement";
    static final String JSON_AGGREGATE = "aggregates";
    
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
 
    /**
     * This method is for saving user work, which in the case of this
     * application means the data that constitutes the page DOM.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    
    
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	DataManager dataManager = (DataManager)data;
	
	// NOW BUILD THE JSON OBJCTS TO SAVE
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	ObservableList<Node> panes = dataManager.getPanes();
	for (int i = 0; i < panes.size(); i++) {
	    VBox pane = (VBox) panes.get(i);
            String name = dataManager.getName(i);
            String Package = dataManager.getPackage(i);
            String parent = dataManager.getParent(i);
            String ipm = dataManager.getIpm(i);
	    double x = pane.getLayoutX();
	    double y = pane.getLayoutY();
            double tx = pane.getTranslateX();
            double ty = pane.getTranslateY();
	    
	    JsonObject paneJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
		    .add(JSON_PACKAGE, Package)
                    .add(JSON_PARENT, parent)
                    .add(JSON_IMPLEMENT, ipm)
                    .add(JSON_X, x)
		    .add(JSON_Y, y)
                    .add(JSON_TX, tx)
                    .add(JSON_TY, ty)
                    .add(JSON_AGGREGATE, buildAggArray(dataManager.getAggs(i)))
		    .add(JSON_VAR, buildVarArray(dataManager.getVars(i)))
		    .add(JSON_METHOD, buildMetArray(dataManager.getMets(i))).build();
	    arrayBuilder.add(paneJson);
	}
	JsonArray panesArray = arrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_PANES, panesArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    
    private JsonArray buildAggArray(ArrayList<String> aggs) {
       JsonArrayBuilder jab = Json.createArrayBuilder();
       
       	for (int i = 0; i < aggs.size(); i++) {
	    String agg = aggs.get(i);
	    JsonObject varJson = Json.createObjectBuilder()
                    .add(JSON_NAME, agg)
                    .build();
            
	    jab.add(varJson);
	}
       JsonArray jA = jab.build();
       return jA;
    }
    
    private JsonArray buildVarArray(ArrayList<jdVar> vars) {
       JsonArrayBuilder jvb = Json.createArrayBuilder();
       
       	for (int i = 0; i < vars.size(); i++) {
	    jdVar var = vars.get(i);
            String name = var.getName();
            String access = var.getAccess();
            String type = var.getType();
            boolean isStatic = var.getStatic();
            String st;
            if(isStatic) 
                st = "true";
            else
                st = "false";

	    JsonObject varJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
		    .add(JSON_ACCESS, access)
                    .add(JSON_TYPE, type)
		    .add(JSON_STATIC, st).build();
            
	    jvb.add(varJson);
	}
        
       JsonArray jA = jvb.build();
       return jA;
    }
    
    private JsonArray buildMetArray(ArrayList<jdMet> mets) {
       JsonArrayBuilder jmb = Json.createArrayBuilder();
       
       	for (int i = 0; i < mets.size(); i++) {
	    jdMet met = mets.get(i);
            String name = met.getName();
            String access = met.getAccess();
            String rt = met.getRT();
            
            boolean isStatic = met.getStatic();
            String st;
            if(isStatic) 
                st = "true";
            else
                st = "false";
            
            boolean isAbstract = met.getAbstract();
            String ab;
            if(isAbstract) 
                ab = "true";
            else
                ab = "false";

	    JsonObject metJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
		    .add(JSON_ACCESS, access)
                    .add(JSON_TYPE, rt)
                    .add(JSON_ABSTRACT, ab)
		    .add(JSON_STATIC, st)
                    .add(JSON_ARGS, buildArgArray(met.getArgs())).build();
            
	    jmb.add(metJson);
	}
        
       JsonArray jA = jmb.build();
       return jA;
    }
    
    private JsonArray buildArgArray(ArrayList<String> args) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (String a : args) {
           jab.add(a);
        }
        JsonArray jA = jab.build();
        return jA;
    }
      
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	
	// AND NOW LOAD ALL THE SHAPES
	JsonArray jsonPaneArray = json.getJsonArray(JSON_PANES);
	for (int i = 0; i < jsonPaneArray.size(); i++) {
	    JsonObject jsonPane = jsonPaneArray.getJsonObject(i);
	    VBox vb = loadPane(jsonPane);
	    dataManager.addClassPane(vb);
            getPaneInfo(jsonPane, dataManager, i);
	}
    }
    
    private VBox loadPane(JsonObject jsonPane) {
        VBox vb = new VBox();
        FlowPane namePane = new FlowPane();
        namePane.setStyle("-fx-border-color: Black; -fx-background-color: White;");
        namePane.setMinSize(150, 50);
        namePane.setPrefSize(150, 50);
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
        vb.setLayoutX(getDataAsDouble(jsonPane, JSON_X));
        vb.setLayoutY(getDataAsDouble(jsonPane, JSON_Y));
        vb.setTranslateX(getDataAsDouble(jsonPane, JSON_TX));
        vb.setTranslateY(getDataAsDouble(jsonPane, JSON_TY));  
        return vb;
    }
    
    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    private void getPaneInfo(JsonObject json, DataManager dm, int i) {
        // First get name, package and parent
        String name = json.getString(JSON_NAME);
        dm.getNames().set(i, name);
        String pkg = json.getString(JSON_PACKAGE);
        dm.getPackages().set(i, pkg);
        String parent = json.getString(JSON_PARENT);
        dm.getParents().set(i, parent);
        String ipm = json.getString(JSON_IMPLEMENT);
        dm.getIpms().set(i, parent);
        dm.setSelected(dm.getPanes().get(i));
        dm.editName(name);
        dm.editPackage(pkg);
        
        // Second the aggregates
        JsonArray jsonAggArray = json.getJsonArray(JSON_AGGREGATE);
        for(int j = 0; j < jsonAggArray.size(); j++) {
            JsonObject jsonAgg = jsonAggArray.getJsonObject(j);
            String aggName = jsonAgg.getString(JSON_NAME);
            dm.addAgg(aggName, i);
        }
        
        // Then the variables
        JsonArray jsonVarArray = json.getJsonArray(JSON_VAR);
        for(int j = 0; j < jsonVarArray.size(); j++) {
            JsonObject jsonVar = jsonVarArray.getJsonObject(j);
            String varName = jsonVar.getString(JSON_NAME);
            String varType = jsonVar.getString(JSON_TYPE);
            String varAccess = jsonVar.getString(JSON_ACCESS);
            String varStatic = jsonVar.getString(JSON_STATIC);
            jdVar newVar = new jdVar();
            newVar.setName(varName);
            newVar.setType(varType);
            newVar.setAccess(varAccess);
            if(varStatic.equals("true"))
                newVar.setStatic(true);
            else
                newVar.setStatic(false);
            dm.addVar(newVar, i);
        }
        
        // Finally the methods
        JsonArray jsonMetArray = json.getJsonArray(JSON_METHOD);
        for(int j = 0; j < jsonMetArray.size(); j++) {
            JsonObject jsonMet = jsonMetArray.getJsonObject(j);
            String metName = jsonMet.getString(JSON_NAME);
            String metType = jsonMet.getString(JSON_TYPE);
            String metAccess = jsonMet.getString(JSON_ACCESS);
            String metStatic = jsonMet.getString(JSON_STATIC);
            String metAbstract = jsonMet.getString(JSON_ABSTRACT);
            jdMet newMet = new jdMet();
            newMet.setName(metName);
            newMet.setRt(metType);
            newMet.setAccess(metAccess);
            if(metStatic.equals("true"))
                newMet.setStatic(true);
            else
                newMet.setStatic(false);
            if(metAbstract.equals("true"))
                newMet.setAbstract(true);
            else
                newMet.setAbstract(false);
            JsonArray jsonArgArray = jsonMet.getJsonArray(JSON_ARGS);
            for(int k = 0; k < jsonArgArray.size(); k++) { 
                String argName = jsonArgArray.getString(k);
                newMet.addArg(argName);
                //System.out.println(argName);
            }
            
            dm.addMet(newMet, i);
        }  
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method exports the contents of the data manager to a 
     * Web page including the html page, needed directories, and
     * the CSS file.
     * 
     * @param data The data management component.
     * 
     * @param filePath Path (including file name/extension) to where
     * to export the page to.
     * 
     * @throws IOException Thrown should there be an error writing
     * out data to the file.
     */
    
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {

    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// NOTE THAT THE Web Page Maker APPLICATION MAKES
	// NO USE OF THIS METHOD SINCE IT NEVER IMPORTS
	// EXPORTED WEB PAGES
    }

    public void exportCode(DataManager dataManager, String path) throws IOException {
        ObservableList panes = dataManager.getPanes();
        ArrayList<String> pkgs = dataManager.getPackages();
        ArrayList<String> names = dataManager.getNames();
        int paneNumber = panes.size();
        String dash = "\\";
        //PrintWriter pw = new PrintWriter(path);
        for(int i = 0; i < paneNumber; i ++) {
          String finalPath = path;
          String pkg = pkgs.get(i);
          
        // If there's nested package
          pkg = pkg.replace(".", dash);
          
          if(!pkg.isEmpty())
            finalPath = finalPath + dash + pkg + dash;
          else
              finalPath = finalPath + dash;
          String name = names.get(i);
          if(!name.isEmpty()){
            File pkgFolder = new File(finalPath);
            pkgFolder.mkdir();
            finalPath = finalPath + name + ".java";
            PrintWriter pw = new PrintWriter(finalPath);
            pw.close();
          }
        }
    }

}
