/*
 * This class is used to sotres the information in a variable.
 */
package jd;

import java.util.ArrayList;

/**
 *
 * @author 哲
 */
public class jdVar {
    boolean isStatic;
    boolean isAbstract;
    String name;
    String access;
    
    jdVar() {
        isStatic = false;
        isAbstract = false;
        name = "";
        access = "";
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
}
