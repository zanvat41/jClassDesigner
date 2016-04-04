package jd.controller;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import jd.data.DataManager;
import jd.gui.Workspace;
import saf.AppTemplate;

/**
 * This class responds to interactions with the rendering surface.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public class CanvasController {
    AppTemplate app;
    
    public CanvasController(AppTemplate initApp) {
	app = initApp;
    }
}
