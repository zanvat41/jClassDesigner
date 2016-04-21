/*
 * This class is used to sotres the information in a variable.
 */
package jd;

import java.util.ArrayList;

/**
 *
 * @author:Zhe Lin
 */
public class jdVar {
    boolean isStatic;
    String type;
    String name;
    String access;
    
    public jdVar() {
        isStatic = false;
        type = "";
        name = "";
        access = "";
    }
    
    public boolean getStatic() {
        return isStatic;
    }
    
    public void setStatic(boolean value) {
        isStatic = value;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String value) {
        type = value;
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
}
