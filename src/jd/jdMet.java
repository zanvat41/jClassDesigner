/*
 * This class is used to sotres the information in a method.
 */
package jd;

import java.util.ArrayList;

/**
 *
 * @author: Zhe Lin
 */
public class jdMet {
    boolean isStatic;
    boolean isAbstract;
    String name;
    String rt;
    String access;
    ArrayList<String> args;
    
    public jdMet() {
        isStatic = false;
        isAbstract = false;
        name = "";
        rt = "";
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
    
    public String getRT() {
        return rt;
    }
    
    public void setRt(String value) {
        rt = value;
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
}
