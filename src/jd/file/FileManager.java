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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
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
import jd.jdLine;
import jd.jdMet;
import jd.jdVar;
import static jdk.nashorn.internal.objects.NativeRegExp.source;
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
    static final String JSON_USE = "uses";
    static final String JSON_LINE = "lines";
    static final String JSON_ID = "inDesign";
    static final String JSON_W = "Width";
    static final String JSON_H = "Height";
    
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
    
    int argSize;
    
    boolean justLoad = false;
    
    String loadPath;
 
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
	//System.out.println(filePath);

        // GET THE DATA
	DataManager dataManager = (DataManager)data;
	
	// NOW BUILD THE JSON OBJCTS TO SAVE
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	ObservableList<Node> panes = dataManager.getPanes();
	for (int i = 0; i < panes.size(); i++) {
	    if(panes.get(i) instanceof VBox){
                VBox pane = (VBox) panes.get(i);
                //Node pane = panes.get(i);
                String name = dataManager.getName(i);
                String Package = dataManager.getPackage(i);
                //ArrayList<String> parents = dataManager.getParent(i);
                //String ipm = dataManager.getIpm(i);
                boolean id = dataManager.getID(i);
                double x = pane.getLayoutX();
                double y = pane.getLayoutY();
                double tx = pane.getTranslateX();
                double ty = pane.getTranslateY();
                double w = pane.getPrefWidth();
                double h = pane.getPrefHeight();       

                JsonObject paneJson = Json.createObjectBuilder()
                        .add(JSON_NAME, name)
                        .add(JSON_PACKAGE, Package)
                        .add(JSON_PARENT, buildStrArray(dataManager.getParents(i)))
                        //.add(JSON_IMPLEMENT, ipm)
                        .add(JSON_ID, id)
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_TX, tx)
                        .add(JSON_TY, ty)
                        .add(JSON_W, w)
                        .add(JSON_H, h)
                        .add(JSON_AGGREGATE, buildStrArray(dataManager.getAggs(i)))
                        .add(JSON_USE, buildStrArray(dataManager.getUses(i)))
                        .add(JSON_LINE, buildLineArray(dataManager.getLines(i)))
                        .add(JSON_VAR, buildVarArray(dataManager.getVars(i)))
                        .add(JSON_METHOD, buildMetArray(dataManager.getMets(i))).build();
                arrayBuilder.add(paneJson);
            }
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
    
    
    private JsonArray buildLineArray(ArrayList<jdLine> lines) {
       JsonArrayBuilder jvb = Json.createArrayBuilder();
       
       	for (int i = 0; i < lines.size(); i++) {
	    jdLine line = lines.get(i);
            String type = line.getType();
            double bX = line.getBX();
            double bY = line.getBY();
            double mX = line.getMX();
            double mY = line.getMY();
            double eX = line.getEX();
            double eY = line.getEY();

	    JsonObject varJson = Json.createObjectBuilder()
                    .add(JSON_TYPE, type)
                    .add("bX", bX)
                    .add("bY", bY)
                    .add("mX", mX)
                    .add("mY", mY)
                    .add("eX", eX)
                    .add("eY", eY)
                    .build();
	    jvb.add(varJson);
	}
        
       JsonArray jA = jvb.build();
       return jA;
    }
    
    
    private JsonArray buildStrArray(ArrayList<String> aggs) {
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
            String rt = met.getType();
            
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
            if(!a.isEmpty())
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
	argSize = 0;
        
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	
	// AND NOW LOAD ALL THE PANES
	JsonArray jsonPaneArray = json.getJsonArray(JSON_PANES);
        
        if(jsonPaneArray.size() > 0){
            dataManager.addGrid1();
            for (int i = 0; i < jsonPaneArray.size(); i++) {
                JsonObject jsonPane = jsonPaneArray.getJsonObject(i);
                VBox vb = loadPane(jsonPane);
                dataManager.addClassPane(vb);
                // GET THE INFO OF THE PANES
                getPaneInfo(jsonPane, dataManager, i + 1);
            }

            dataManager.addGrid2();
            justLoad = true;
            loadPath = filePath;
        }
    }
    
    private VBox loadPane(JsonObject jsonPane) {
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
        if(!jsonPane.getBoolean(JSON_ID))
            vb.getChildren().add(exPane);
        
        vb.setLayoutX(getDataAsDouble(jsonPane, JSON_X));
        vb.setLayoutY(getDataAsDouble(jsonPane, JSON_Y));
        vb.setTranslateX(getDataAsDouble(jsonPane, JSON_TX));
        vb.setTranslateY(getDataAsDouble(jsonPane, JSON_TY)); 
        vb.setPrefWidth(getDataAsDouble(jsonPane, JSON_W));
        vb.setPrefHeight(getDataAsDouble(jsonPane, JSON_H));
        return vb;
    }
    
    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    private void getPaneInfo(JsonObject json, DataManager dm, int i) {
        // First get name, package, interface, in design
        String name = json.getString(JSON_NAME);
        dm.getNames().set(i, name);
        String pkg = json.getString(JSON_PACKAGE);
        dm.getPackages().set(i, pkg);
        boolean id = json.getBoolean(JSON_ID);
        dm.setID(i, id);
        if(!dm.isTest()) {
            dm.setSelected(dm.getPanes().get(i));
            dm.editName(name, false);
            dm.editPackage(pkg, false);
        }
        
        // Second the parents
        JsonArray jsonPrtArray = json.getJsonArray(JSON_PARENT);
        for(int j = 0; j < jsonPrtArray.size(); j++) {
            JsonObject jsonPrt = jsonPrtArray.getJsonObject(j);
            String prtName = jsonPrt.getString(JSON_NAME);
            dm.addParent(prtName, i);
        }
        


        // Then the aggregates
        JsonArray jsonAggArray = json.getJsonArray(JSON_AGGREGATE);
        for(int j = 0; j < jsonAggArray.size(); j++) {
            JsonObject jsonAgg = jsonAggArray.getJsonObject(j);
            String aggName = jsonAgg.getString(JSON_NAME);
            dm.addAgg(aggName, i);
        }
        
        //Then the uses
        JsonArray jsonUseArray = json.getJsonArray(JSON_USE);
        for(int j = 0; j < jsonUseArray.size(); j++) {
            JsonObject jsonUse = jsonUseArray.getJsonObject(j);
            String useName = jsonUse.getString(JSON_NAME);
            dm.addUse(useName, i);
        }
        
        //Then the lines
        JsonArray jsonLineArray = json.getJsonArray(JSON_LINE);
        for(int j = 0; j < jsonLineArray.size(); j++) {
            JsonObject jsonLine = jsonLineArray.getJsonObject(j);
            String lineType = jsonLine.getString(JSON_TYPE);

            jdLine newLine = new jdLine();

            newLine.setType(lineType);
            newLine.setB(getDataAsDouble(jsonLine, "bX"), getDataAsDouble(jsonLine, "bY"));
            newLine.setM(getDataAsDouble(jsonLine, "mX"), getDataAsDouble(jsonLine, "mY"));
            newLine.setE(getDataAsDouble(jsonLine, "eX"), getDataAsDouble(jsonLine, "eY"));
            dm.addLine(newLine, i);
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
            dm.addVar1(newVar, i);
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
            newMet.setType(metType);
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
            if(jsonArgArray.size() > argSize)
                argSize = jsonArgArray.size();
            dm.addMet1(newMet, i);
        }  
    }
    
    public int getArgSize() {
        return argSize;
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
        // ArrayList<String> parents = dataManager.getParents();
        //ArrayList<String> ipms = dataManager.getIpms();
        
        
        int paneNumber = panes.size();
        String dash = "\\";
        //PrintWriter pw = new PrintWriter(path);
        for(int i = 0; i < paneNumber; i ++) {
            if(dataManager.getID(i)){
                String finalPath = path;
                String pkg = pkgs.get(i);
                ArrayList<String> parents = dataManager.getParents(i);
          
                // If there's nested package
                pkg = pkg.replace(".", dash);
          
                if(!pkg.isEmpty())
                    finalPath = finalPath + dash + pkg + dash;
                else
                    finalPath = finalPath + dash;
                String name = names.get(i);
                String name1 = name;
                if(name.contains("{abstract}"))
                    name1 = name1.replace("{abstract}", "");
                else if(name.contains("{interface}"))
                    name1 = name1.replace("{interface}", "");
                if(!name.isEmpty()){
                    File pkgFolder = new File(finalPath);
                    pkgFolder.mkdir();
                    finalPath = finalPath + name1 + ".java";
                    PrintWriter pw = new PrintWriter(finalPath);

                    // Check if it is in a package
                    if(!pkg.isEmpty())
                        pw.println("package " + pkgs.get(i) +";");

                    // Imports
                    for(int j = 0; j < pkgs.size(); j++) {
                        if(!pkgs.get(j).equals(pkgs.get(i)) && !pkgs.get(j).isEmpty())
                            pw.println("import " + pkgs.get(j) +".*;");
                    }

                    pw.print("public ");

                    // Check if it's an abstract class, interface or normal class
                    if(name.contains("{abstract}"))
                        pw.print("abstract class ");
                    else if(name.contains("{interface}"))
                        pw.print("interface "); 
                    else
                        pw.print("class ");

                    pw.print(name1 + " ");

                    //Check if it extends a class or not
                    for(String p : parents) {
                        String p1;
                        if(!p.contains("{interface}")) {
                            p1 = p.replace("{abstract}", "");
                            pw.print("extends " + p1 + " ");
                        }
                    }    

                    //Check if it implements an interface or not
                    for(String p : parents) {
                        if(p.contains("{interface}")) {
                            pw.print("implements " + p + " ");
                        }
                    }  

                    pw.println("{");

                    //Then the variables
                    ArrayList<jdVar> varList = dataManager.getVars(i);
                    for(int j = 0; j < varList.size(); j++) {
                        jdVar var = varList.get(j);
                        boolean st = var.getStatic();
                        String acc = var.getAccess();
                        String varName = var.getName();
                        String varType = var.getType();
                        pw.print("    " + acc +" ");
                        if(st)
                            pw.print("static ");
                        pw.print(varType + " " + varName + " = ");
                        if(varType.equals("int") || varType.equals("double") || varType.equals("byte")
                                || varType.equals("short") || varType.equals("float"))
                                pw.println("0;" );
                            else if(varType.equals("char"))
                                pw.println("'0';" );
                            else if(varType.equals("boolean"))
                                pw.println("true;" );
                            else
                                pw.println("null;" );

                    }

                    //Finally the methods
                    ArrayList<jdMet> metList = dataManager.getMets(i);
                    for(int j = 0; j < metList.size(); j++) {
                        jdMet met = metList.get(j);
                        boolean st = met.getStatic();
                        boolean ab = met.getAbstract();
                        String acc = met.getAccess();
                        String metName = met.getName();
                        String rt = met.getType();
                        ArrayList<String> args = met.getArgs();
                        pw.print("    " + acc + " ");
                        if(st)
                            pw.print("static ");
                        if(ab)
                            pw.print("abstract ");
                        pw.print(rt + " " + metName + "(");
                        for(int k = 0; k < args.size(); k++) {
                            pw.print(args.get(k) + " arg" + k);
                            if(k != args.size() - 1)
                                pw.print(", ");
                        }
                        pw.println(") {");
                        if(!rt.equals("void") && !rt.isEmpty()){
                            if(rt.equals("int") || rt.equals("double") || rt.equals("byte") || rt.equals("short") || rt.equals("float"))
                                pw.println("        return 0;" );
                            else if(rt.equals("char"))
                                pw.println("        return '0';" );
                            else if(rt.equals("boolean"))
                                pw.println("        return true;" );
                            else
                                pw.println("        return null;" );
                        }
                        pw.println("    }");
                    }

                    pw.println("}");
                    pw.close();
                }
            }
        }
    }
    
    /**
     * This function clears the contents of the file argument.
     * 
     * @param filePath The file to clear.
     * 
     * @throws IOException Thrown should there be an issue writing
     * to the file to clear.
     */
    public void clearFile(String filePath) throws IOException {
	PrintWriter out = new PrintWriter(filePath);
	out.print("");
	out.close();
    }

    public void writeEmpty(String filePath) throws FileNotFoundException, IOException {
        if(justLoad) {
            InputStream input = null;
            OutputStream output = null;
            try {
                //System.out.println("uphere");
                input = new FileInputStream(loadPath);
                output = new FileOutputStream(filePath);
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
            } finally {
                input.close();
                output.close();
            }
            justLoad = false;
        } else{
            //System.out.println("imhere");
            PrintWriter out = new PrintWriter(filePath);
            out.print("\n" +
            "{\n" +
            "    \"panes\":[\n" +
            "    ]\n" +
            "}");
            out.close();
        }
    }

    public void setLoad(boolean b) {
        justLoad = false;
    }
    
}
