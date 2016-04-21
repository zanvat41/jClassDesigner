/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jd.test_bed;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jd.data.DataManager;
import jd.file.FileManager;
import jd.jClassDesigner;
import jd.jdMet;
import jd.jdVar;
import saf.AppTemplate;
import static saf.settings.AppStartupConstants.PATH_WORK;

/**
 *
 * @author:Zhe Lin
 */
public class TestSave {
    public static void main(String[] args) throws Exception {
        jClassDesigner jd = new jClassDesigner();
        DataManager dm = new DataManager((AppTemplate)jd);
        //DataManager dm = (DataManager) jd.getDataComponent();
        FileManager fm = new FileManager();
        Pane canvas = new Pane();
        //canvas.getChildren();
        dm.setPanes(canvas.getChildren());
        //System.out.println(dm.getNames());
        ArrayList<String> names = dm.getNames();
        ArrayList<String> pks = dm.getPackages();
        ArrayList<String> prts = dm.getParents();
        ArrayList<String> ipms = dm.getIpms();
        
        //First is the ThreadExample class(The 0th elements in the lists in dm)
        VBox te = new VBox();
        te.setLayoutX(0);
        te.setLayoutY(0);
        dm.addClassPane(te);
        names.set(0, "ThreadExample");
        pks.set(0, "te");
        prts.set(0, "Application");
        
        dm.addAgg("CounterTask", 0);
        dm.addAgg("DateTask", 0);
        dm.addAgg("PauseHandler", 0);
        dm.addAgg("StartHandler", 0);
        
        jdVar ST = new jdVar();
        ST.setName("START_TEXT");
        ST.setAccess("public");
        ST.setStatic(true);
        ST.setType("String");
        dm.addVar(ST, 0);
        
        jdVar PT = new jdVar();
        PT.setName("PAUSE_TEXT");
        PT.setAccess("public");
        PT.setStatic(true);
        PT.setType("String");
        dm.addVar(PT, 0);
        
        jdVar win = new jdVar();
        win.setName("window");
        win.setAccess("private");
        win.setStatic(false);
        win.setType("Stage");
        dm.addVar(win, 0);
        
        jdVar ap = new jdVar();
        ap.setName("appPane");
        ap.setAccess("private");
        ap.setStatic(false);
        ap.setType("BorderPane");
        dm.addVar(ap, 0);
        
        jdVar tp = new jdVar();
        tp.setName("topPane");
        tp.setAccess("private");
        tp.setStatic(false);
        tp.setType("FlowPane");
        dm.addVar(tp, 0);

        jdVar stB = new jdVar();
        stB.setName("startButton");
        stB.setAccess("private");
        stB.setStatic(false);
        stB.setType("Button");
        dm.addVar(stB, 0);
                
        jdVar psB = new jdVar();
        psB.setName("pauseButton");
        psB.setAccess("private");
        psB.setStatic(false);
        psB.setType("Button");
        dm.addVar(psB, 0);        
                
        jdVar scP = new jdVar();
        scP.setName("scrollPane");
        scP.setAccess("private");
        scP.setStatic(false);
        scP.setType("ScrollPane");
        dm.addVar(scP, 0);        
                
        jdVar txA = new jdVar();
        txA.setName("textArea");
        txA.setAccess("private");
        txA.setStatic(false);
        txA.setType("TextArea");
        dm.addVar(txA, 0);

        jdVar dT = new jdVar();
        dT.setName("dateThread");
        dT.setAccess("private");
        dT.setStatic(false);
        dT.setType("Thread");
        dm.addVar(dT, 0);
        
        jdVar dTK = new jdVar();
        dTK.setName("dateTask");
        dTK.setAccess("private");
        dTK.setStatic(false);
        dTK.setType("Task");
        dm.addVar(dTK, 0);
        
        jdVar cT = new jdVar();
        cT.setName("counterThread");
        cT.setAccess("private");
        cT.setStatic(false);
        cT.setType("Thread");
        dm.addVar(cT, 0);
        
        jdVar cTK = new jdVar();
        cTK.setName("counterTask");
        cTK.setAccess("private");
        cTK.setStatic(false);
        cTK.setType("Task");
        dm.addVar(cTK, 0);
        
        jdVar wk = new jdVar();
        wk.setName("work");
        wk.setAccess("private");
        wk.setStatic(false);
        wk.setType("boolean");
        dm.addVar(wk, 0);        
                
        jdMet start = new jdMet();
        start.setName("start");
        start.setAbstract(false);
        start.setAccess("public");
        start.setRt("void");
        start.addArg("Stage");
        start.setStatic(false);
        dm.addMet(start, 0);
        
        jdMet stW = new jdMet();
        stW.setName("startWork");
        stW.setAbstract(false);
        stW.setAccess("public");
        stW.setRt("void");
        stW.setStatic(false);
        dm.addMet(stW, 0);
        
        jdMet psW = new jdMet();
        psW.setName("pauseWork");
        psW.setAbstract(false);
        psW.setAccess("public");
        psW.setRt("void");
        psW.setStatic(false);
        dm.addMet(psW, 0);
        
        jdMet dW = new jdMet();
        dW.setName("doWork");
        dW.setAbstract(false);
        dW.setAccess("public");
        dW.setRt("boolean");
        dW.setStatic(false);
        dm.addMet(dW, 0);
        
        jdMet apT = new jdMet();
        apT.setName("appendText");
        apT.setAbstract(false);
        apT.setAccess("public");
        apT.setRt("void");
        apT.addArg("String");
        apT.setStatic(false);
        dm.addMet(apT, 0);
        
        jdMet sp = new jdMet();
        sp.setName("sleep");
        sp.setAbstract(false);
        sp.setAccess("public");
        sp.setRt("void");
        sp.addArg("int");
        sp.setStatic(false);
        dm.addMet(sp, 0);
        
        jdMet inL = new jdMet();
        inL.setName("initLayout");
        inL.setAbstract(false);
        inL.setAccess("private");
        inL.setRt("void");
        inL.setStatic(false);
        dm.addMet(inL, 0);
        
        jdMet inH = new jdMet();
        inH.setName("initHandlers");
        inH.setAbstract(false);
        inH.setAccess("private");
        inH.setRt("void");
        inH.setStatic(false);
        dm.addMet(inH, 0);
        
        jdMet inW = new jdMet();
        inW.setName("initWindow");
        inW.setAbstract(false);
        inW.setAccess("private");
        inW.setRt("void");
        inW.addArg("Stage");
        inW.setStatic(false);
        dm.addMet(inW, 0);
        
        jdMet inT = new jdMet();
        inT.setName("initThreads");
        inT.setAbstract(false);
        inT.setAccess("private");
        inT.setRt("void");
        inT.setStatic(false);
        dm.addMet(inT, 0);
        
        jdMet main = new jdMet();
        main.setName("main");
        main.setAbstract(false);
        main.setAccess("public");
        main.setRt("void");
        main.addArg("String[]");
        main.setStatic(true);
        dm.addMet(main, 0); 
        
        // Second is CounterTask
        VBox ct = new VBox();
        ct.setLayoutX(200);
        ct.setLayoutY(0);
        dm.addClassPane(ct);
        names.set(1, "CounterTask");
        pks.set(1, "te.task");
        prts.set(1, "Task");

        dm.addAgg("ThreadExample", 1);
        
        jdVar cAP = new jdVar();
        cAP.setName("app");
        cAP.setAccess("private");
        cAP.setStatic(false);
        cAP.setType("ThreadExample");
        dm.addVar(cAP, 1);
        
        jdVar cct = new jdVar();
        cct.setName("counter");
        cct.setAccess("private");
        cct.setStatic(false);
        cct.setType("int");
        dm.addVar(cct, 1);
        
        jdMet ctCons = new jdMet();
        ctCons.setName("CounterTask");
        ctCons.setAbstract(false);
        ctCons.setAccess("public");
        ctCons.addArg("ThreadExample");
        ctCons.setStatic(false);
        dm.addMet(ctCons, 1);
        
        jdMet ctCall = new jdMet();
        ctCall.setName("call");
        ctCall.setAbstract(false);
        ctCall.setAccess("protect");
        ctCall.setRt("void");
        ctCall.setStatic(false);
        dm.addMet(ctCall, 1);
        
        
        // Third is DateTask
        VBox dt = new VBox();
        dt.setLayoutX(400);
        dt.setLayoutY(0);
        dm.addClassPane(dt);
        names.set(2, "DateTask");
        pks.set(2, "te.task");
        prts.set(2, "Task");

        dm.addAgg("ThreadExample", 2);
        
        jdVar dAP = new jdVar();
        dAP.setName("app");
        dAP.setAccess("private");
        dAP.setStatic(false);
        dAP.setType("ThreadExample");
        dm.addVar(dAP, 2);
        
        jdVar now = new jdVar();
        now.setName("now");
        now.setAccess("private");
        now.setStatic(false);
        now.setType("Date");
        dm.addVar(now, 2);
        
        jdMet dtCons = new jdMet();
        dtCons.setName("DateTask");
        dtCons.setAbstract(false);
        dtCons.setAccess("public");
        dtCons.addArg("ThreadExample");
        dtCons.setStatic(false);
        dm.addMet(dtCons, 2);
        
        jdMet dtCall = new jdMet();
        dtCall.setName("call");
        dtCall.setAbstract(false);
        dtCall.setAccess("protect");
        dtCall.setRt("void");
        dtCall.setStatic(false);
        dm.addMet(dtCall, 2);
        
        // Fourth is Pause Handler
        VBox ph = new VBox();
        ph.setLayoutX(0);
        ph.setLayoutY(600);
        dm.addClassPane(ph);
        names.set(3, "PauseHandler");
        pks.set(3, "te.control");
        ipms.set(3, "EventHandler");

        dm.addAgg("ThreadExample", 3);
        
        jdVar pAP = new jdVar();
        pAP.setName("app");
        pAP.setAccess("private");
        pAP.setStatic(false);
        pAP.setType("ThreadExample");
        dm.addVar(pAP, 3);
        
        jdMet phCons = new jdMet();
        phCons.setName("PauseHandler");
        phCons.setAbstract(false);
        phCons.setAccess("public");
        phCons.addArg("ThreadExample");
        phCons.setStatic(false);
        dm.addMet(phCons, 3);
        
        jdMet phHandle = new jdMet();
        phHandle.setName("handle");
        phHandle.setAbstract(false);
        phHandle.setAccess("public");
        phHandle.setRt("void");
        phHandle.addArg("Event");
        phHandle.setStatic(false);
        dm.addMet(phHandle, 3);
        
        
        // Fifth is Start Handler
        VBox sh = new VBox();
        sh.setLayoutX(200);
        sh.setLayoutY(600);
        dm.addClassPane(sh);
        names.set(4, "StartHandler");
        pks.set(4, "te.control");
        ipms.set(4, "EventHandler");
        
        dm.addAgg("ThreadExample", 4);
        
        jdVar sAP = new jdVar();
        sAP.setName("app");
        sAP.setAccess("private");
        sAP.setStatic(false);
        sAP.setType("ThreadExample");
        dm.addVar(sAP, 4);
        
        jdMet shCons = new jdMet();
        shCons.setName("StartHandler");
        shCons.setAbstract(false);
        shCons.setAccess("public");
        shCons.addArg("ThreadExample");
        shCons.setStatic(false);
        dm.addMet(shCons, 4);
        
        jdMet shHandle = new jdMet();
        shHandle.setName("handle");
        shHandle.setAbstract(false);
        shHandle.setAccess("public");
        shHandle.setRt("void");
        shHandle.addArg("Event");
        shHandle.setStatic(false);
        dm.addMet(shHandle, 4);
        
        // And Some Java API classes
        VBox stage = new VBox();
        stage.setLayoutX(400);
        stage.setLayoutY(600);
        dm.addClassPane(stage);
        dm.addAgg("ThreadExample", 5);
        names.set(5, "Stage");

        VBox bdp = new VBox();
        bdp.setLayoutX(400);
        bdp.setLayoutY(700);
        dm.addClassPane(bdp);
        dm.addAgg("ThreadExample", 6);
        names.set(6, "BorderPane");
        
        VBox flp = new VBox();
        flp.setLayoutX(400);
        flp.setLayoutY(800);
        dm.addClassPane(flp);
        dm.addAgg("ThreadExample", 7);
        names.set(7, "FlowPane");
        
        VBox button = new VBox();
        button.setLayoutX(400);
        button.setLayoutY(900);
        dm.addClassPane(button);
        dm.addAgg("ThreadExample", 8);
        names.set(8, "Button");
        
        VBox scp = new VBox();
        scp.setLayoutX(600);
        scp.setLayoutY(600);
        dm.addClassPane(scp);
        dm.addAgg("ThreadExample", 9);
        names.set(9, "ScrollPane");
        
        VBox ta = new VBox();
        ta.setLayoutX(600);
        ta.setLayoutY(700);
        dm.addClassPane(ta);
        dm.addAgg("ThreadExample", 10);
        names.set(10, "TextArea");
        
        VBox td = new VBox();
        td.setLayoutX(600);
        td.setLayoutY(800);
        dm.addClassPane(td);
        dm.addAgg("ThreadExample", 11);
        names.set(11, "Thread");

        // Finally test saveData
        fm.saveData(dm, PATH_WORK + "DesignSaveTest");
        
        
    }
}
