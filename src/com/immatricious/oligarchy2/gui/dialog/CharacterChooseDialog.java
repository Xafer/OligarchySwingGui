package com.immatricious.oligarchy2.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import com.immatricious.macromanager.character.Character;
import com.immatricious.macromanager.task.Task;
import com.immatricious.oligarchy2.OligarchyApplication;

public class CharacterChooseDialog {
	private JDialog charChoose;

	private static final String ASSIGN = "assign";
	private static final String CANCEL = "cancel";
	
	private JComboBox<String> cboxChar;
	
	public CharacterChooseDialog()
	{
		initCharacterChoose();
	}
	
	private void reloadCharacterList()
	{
		cboxChar.removeAllItems();
		for(Character character : OligarchyApplication.getCurrentApplication().getTaskHandler().getCharacterManager().getCharacters())
			if(character.isLogged())
				cboxChar.addItem(character.getName());
	}
	
	private void initCharacterChoose()
	{
		charChoose = new JDialog();
		charChoose.setTitle("Choose character");
		charChoose.setSize(300,140);
		charChoose.getContentPane().setLayout(new BoxLayout(charChoose.getContentPane(),BoxLayout.Y_AXIS));
		
		JPanel cboxContainer = new JPanel();
		cboxChar = new JComboBox<String>();
		
		cboxContainer.add(cboxChar);
		
		JPanel buttonContainer = new JPanel();
		JButton assignBtn = new JButton("Assign");
		JButton cancelBtn = new JButton("Cancel");

		assignBtn.setActionCommand(ASSIGN);
		cancelBtn.setActionCommand(CANCEL);
		
		buttonContainer.add(assignBtn);
		buttonContainer.add(cancelBtn);
		
		ActionListener btn = new BtnListener();

		assignBtn.addActionListener(btn);
		cancelBtn.addActionListener(btn);
		
		charChoose.add(cboxContainer);
		charChoose.add(buttonContainer);
	}
	
	public void show()
	{
		reloadCharacterList();
		charChoose.setVisible(true);
	}
	
	private class BtnListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(ASSIGN.equals(e.getActionCommand()))
			{
				Character c = OligarchyApplication.getCurrentApplication().getTaskHandler().getCharacterManager().getCharacter((String) cboxChar.getSelectedItem());
				JList<String> tasks = (JList<String>) OligarchyApplication.getCurrentApplication().getGUIManager().getTaskList();
				if(tasks.getSelectedValue() != null)
				{
					String taskName = tasks.getSelectedValue().split(":")[0];
					Task t = OligarchyApplication.getCurrentApplication().getTaskHandler().getTaskByName(taskName);
					c.queueTask(true, t);
				}
			}
			
			charChoose.setVisible(false);
		}
		
	}
}
