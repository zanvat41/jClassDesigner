package jd.file;

import java.io.FileInputStream;
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
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    
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
	    double x = pane.getLayoutX();
	    double y = pane.getLayoutY();
            double tx = pane.getTranslateX();
            double ty = pane.getTranslateY();
	    
	    JsonObject paneJson = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
		    .add(JSON_PACKAGE, Package)
                    .add(JSON_PARENT, parent)
                    .add(JSON_X, x)
		    .add(JSON_Y, y)
                    .add(JSON_TX, tx)
                    .add(JSON_TY, ty)
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
    
    
    /*public JsonObject makeJsonMetObject(ArrayList<jdMet> mets) {
	JsonObject metJson = Json.createObjectBuilder()
		.add(JSON_ALPHA, "Hello").build();
	return metJson;
    }
    
    public JsonObject makeJsonVarObject(ArrayList<jdVar> vars) {
	JsonObject varJson = Json.createObjectBuilder()
		.add(JSON_NAME, "Hello").build();
	return varJson;
    }*/
      
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
	/*DataManager dataManager = (DataManager)data;
	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	
	// LOAD THE BACKGROUND COLOR
	Color bgColor = loadColor(json, JSON_BG_COLOR);
	dataManager.setBackgroundColor(bgColor);
	
	// AND NOW LOAD ALL THE SHAPES
	JsonArray jsonShapeArray = json.getJsonArray(JSON_SHAPES);
	for (int i = 0; i < jsonShapeArray.size(); i++) {
	    JsonObject jsonShape = jsonShapeArray.getJsonObject(i);
	    Shape shape = loadShape(jsonShape);
	    dataManager.addShape(shape);
	}*/
    }
    
    /*public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }*/
    
    /*public Shape loadShape(JsonObject jsonShape) {
	// FIRST BUILD THE PROPER SHAPE TYPE
	String type = jsonShape.getString(JSON_TYPE);
	Shape shape;
	if (type.equals(RECTANGLE)) {
	    shape = new DraggableRectangle();
	}
	else {
	    shape = new DraggableEllipse();
	}
	
	// THEN LOAD ITS FILL AND OUTLINE PROPERTIES
	Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
	Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
	double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
	shape.setFill(fillColor);
	shape.setStroke(outlineColor);
	shape.setStrokeWidth(outlineThickness);
	
	// AND THEN ITS DRAGGABLE PROPERTIES
	double x = getDataAsDouble(jsonShape, JSON_X);
	double y = getDataAsDouble(jsonShape, JSON_Y);
	double width = getDataAsDouble(jsonShape, JSON_WIDTH);
	double height = getDataAsDouble(jsonShape, JSON_HEIGHT);
	Draggable draggableShape = (Draggable)shape;
	draggableShape.setLocationAndSize(x, y, width, height);
	
	// ALL DONE, RETURN IT
	return shape;
    }*/
    
    /*public Color loadColor(JsonObject json, String colorToGet) {
	JsonObject jsonColor = json.getJsonObject(colorToGet);
	double red = getDataAsDouble(jsonColor, JSON_RED);
	double green = getDataAsDouble(jsonColor, JSON_GREEN);
	double blue = getDataAsDouble(jsonColor, JSON_BLUE);
	double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
	Color loadedColor = new Color(red, green, blue, alpha);
	return loadedColor;
    }*/

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
}
