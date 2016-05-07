/*
 * This class is used to sotres the information in a method.
 */
package jd;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author: Zhe Lin
 */
public class jdMet {
    boolean isStatic;
    boolean isAbstract;
    String name;
    String type;
    String access;
    ArrayList<String> args;
    
    public jdMet() {
        isStatic = false;
        isAbstract = false;
        name = "";
        type = "";
        access = "";
        args = new ArrayList<String>();    
    }
    
    public boolean getStatic() {
        return isStatic;
    }
    
    public void setStatic(boolean value) {
        isStatic = value;
    }
    
    public boolean getAbstract() {
        return isAbstract;
    }
    
    public void setAbstract(boolean value) {
        isAbstract = value;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String value) {
        name = value;
    }
    
    public String getAccess() {
        return access;
    }
    
    public void setAccess(String value) {
        access = value;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String value) {
        type = value;
    }
    
    public ArrayList getArgs() {
        return args;
    }
    
    public void addArg(String value) {
        args.add(value);
    }
    
    public void removeArg(String value) {
        args.remove(value);
    }
    
    
    public SimpleStringProperty getArgPro(int i) {
        SimpleStringProperty arg = new SimpleStringProperty();
        if(i > -1 && i < args.size())
            arg.set(args.get(i));
        return arg;
    }
}
