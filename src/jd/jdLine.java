/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jd;


/**
 *
 * @author 哲
 */
public class jdLine {
    String type;
    double bX, bY;
    double mX, mY;
    double eX, eY;
    
    public void setType(String t) {
        type = t;
    }
    
    public String getType() {
        return type;
    }
    
    public void setB(double x, double y) {
        bX = x;
        bY = y;
    }
    
    public double getBX() {
        return  bX;
    }
    
    public double getBY() {
        return  bY;
    }
    
        public void setM(double x, double y) {
        mX = x;
        mY = y;
    }
    
    public double getMX() {
        return mX;
    }
    
    public double getMY() {
        return  mY;
    }
    
    public void setE(double x, double y) {
        eX = x;
        eY = y;
    }
    
    public double getEX() {
        return  eX;
    }
    
    public double getEY() {
        return  eY;
    }
}
