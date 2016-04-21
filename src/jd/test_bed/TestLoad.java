/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jd.test_bed;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import jd.data.DataManager;
import jd.file.FileManager;
import jd.jClassDesigner;
import jd.jdMet;
import jd.jdVar;
import saf.AppTemplate;
import static saf.settings.AppStartupConstants.PATH_WORK;

/**
 *
 * @author 哲
 */
public class TestLoad {
    public static void main(String[] args) throws Exception {
        jClassDesigner jd = new jClassDesigner();
        DataManager dm = new DataManager((AppTemplate)jd);
        FileManager fm = new FileManager();
        ObservableList<Node> Panes = FXCollections.observableArrayList();
        dm.setPanes(Panes);
        fm.loadData(dm, PATH_WORK + "DesignSaveTest");
        int size = Panes.size();
        //System.out.println(size);
        for(int i = 0; i < size; i++) {
            System.out.println(dm.getName(i));
            // Print the vars
            ArrayList<jdVar> newVars = dm.getVars(i);
            for(int j = 0; j < newVars.size(); j++) {
                System.out.println(newVars.get(j).getName() + " : " + newVars.get(j).getType());
            }
            // Print the methods
            ArrayList<jdMet> newMets = dm.getMets(i);
            for(int j = 0; j < newMets.size(); j++) {
                System.out.print(newMets.get(j).getName() + "(");
                for(int k = 0; k < newMets.get(j).getArgs().size(); k++) {
                    System.out.print(newMets.get(j).getArgs().get(k));
                    if(k != newMets.get(j).getArgs().size() - 1) 
                        System.out.print(", ");
                }
                String rt = newMets.get(j).getRT();
                if(!rt.isEmpty())
                    System.out.println(") : " + rt);
                else
                    System.out.println(")");
            }
                        
            System.out.println();
        }
    }
}
