package com.immatricious.oligarchy2.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.macromanager.task.TaskPriority;
import com.immatricious.oligarchy2.OligarchyApplication;

public class PriorityChooseDialog {
	private JDialog priorityInput;

	private static final String POOL = "pool";
	private static final String CANCEL = "cancel";
	private JComboBox<TaskPriority> pList;
	
	public PriorityChooseDialog()
	{
		initPriorityInput();
	}
	
	public void initPriorityInput()
	{
		priorityInput = new JDialog();
		priorityInput.setSize(300,140);
		priorityInput.setLayout(new BoxLayout(priorityInput.getContentPane(),BoxLayout.Y_AXIS));
		
		JPanel priorityPanel = new JPanel();
		priorityPanel.setLayout(new BoxLayout(priorityPanel,BoxLayout.X_AXIS));
		JLabel pLabel = new JLabel("Priority: ");
		
		pList = new JComboBox<TaskPriority>(); 
		
		for(TaskPriority p : TaskPriority.values())
			pList.addItem(p);
		
		priorityPanel.add(pLabel);
		priorityPanel.add(pList);
		
		JPanel buttonPanel = new JPanel();
		JButton okBtn = new JButton(POOL);
		JButton cancelBtn = new JButton(CANCEL);

		okBtn.setActionCommand("pool");
		cancelBtn.setActionCommand("cancel");
		
		buttonPanel.add(okBtn);
		buttonPanel.add(cancelBtn);

		priorityInput.add(priorityPanel);
		priorityInput.add(buttonPanel);
		
		BtnListener btnListener = new BtnListener();

		okBtn.addActionListener(btnListener);
		cancelBtn.addActionListener(btnListener);
		
	}
	
	public void show() { priorityInput.setVisible(true); }
	
	private class BtnListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {

			if(POOL.equals(e.getActionCommand()))
			{
				TaskHandler th = OligarchyApplication.getCurrentApplication().getTaskHandler();
				String selectedTask = ((JList<String>) OligarchyApplication.getCurrentApplication().getGUIManager().getTaskList()).getSelectedValue();
				th.poolTask(th.getTaskByName(selectedTask), (TaskPriority) pList.getSelectedItem());
			}
			
			priorityInput.setVisible(false);
		}
		
	}
}
