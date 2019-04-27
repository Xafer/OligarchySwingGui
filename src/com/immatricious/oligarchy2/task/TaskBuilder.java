package com.immatricious.oligarchy2.task;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.immatricious.macromanager.task.Action;
import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskRequirement;
import com.immatricious.oligarchy2.OligarchyApplication;
import com.immatricious.oligarchy2.gui.GUIManager;

public class TaskBuilder
{
	
	/**
	 * Builds a new tasks and updates the task viewer gui and the taskpool
	 * @param taskName Name of the new task
	 * @param repeat Repeat time
	 * @param actions Actions
	 * @param req
	 * @return The created task
	 */
	public Task buildTask(String taskName, int repeat, List<Action> actions, TaskRequirement req)
	{
		Task t = new Task(taskName,actions,false,req);
		
		JList<String> taskList = (JList<String>) OligarchyApplication.getCurrentApplication().getGUIManager().getTaskList();
		((DefaultListModel<String>)taskList.getModel()).addElement(taskName + ": " + t.getMessageString());
		
		return t;
	}
}
