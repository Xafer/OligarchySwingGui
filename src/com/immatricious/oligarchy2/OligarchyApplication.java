package com.immatricious.oligarchy2;

import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.oligarchy2.gui.GUIManager;
import com.immatricious.oligarchy2.task.TaskBuilder;

public class OligarchyApplication {
	private GUIManager gm;
	private TaskHandler th;
	private TaskBuilder tb;
	
	private static OligarchyApplication currentApplication;
	
	public OligarchyApplication( )
	{
		tb = new TaskBuilder();
		gm = new GUIManager();
		th = new TaskHandler();
		OligarchyApplication.currentApplication = this;
	}
	
	public void init()
	{
		th.init();
		gm.init();
		
		th.read();
	}
	
	public GUIManager getGUIManager() { return this.gm; }
	public TaskHandler getTaskHandler() { return this.th; }
	public TaskBuilder getTaskBuilder() { return this.tb; }
	
	public static OligarchyApplication getCurrentApplication() { return OligarchyApplication.currentApplication; }
}
