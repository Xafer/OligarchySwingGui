package com.immatricious.oligarchy2.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NameInputDialog {
	
	private JDialog nameInput;
	private JTextField nameField;
	private Consumer<Object> currentCallback;
	
	public NameInputDialog()
	{
		initCharacterInput();
	}
	
	private void initCharacterInput()
	{
		nameInput = new JDialog();
		nameInput.setTitle("Generate Task");
		nameInput.setSize(300,140);
		
		nameInput.setLayout(new BoxLayout(nameInput.getContentPane(),BoxLayout.Y_AXIS));
		
		nameInput.add(Box.createRigidArea(new Dimension(0,24)));
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		JLabel sa = new JLabel("Save as: ");
		nameField = new JTextField();
		nameField.setMaximumSize(new Dimension(80,24));
		
		namePanel.add(sa);
		namePanel.add(nameField);
		
		nameInput.add(namePanel);
		
		JPanel buttonPanel = new JPanel();
		
		JButton cancelBtn = new JButton("Cancel");
		JButton generateBtn = new JButton("Generate");

		buttonPanel.add(generateBtn);
		buttonPanel.add(cancelBtn);
		
		nameInput.add(buttonPanel);
		
		
		cancelBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nameInput.setVisible(false);
			}
		});
		
		generateBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(currentCallback != null)
					currentCallback.accept(nameField.getText());
				
				nameField.setText("");
				nameInput.setVisible(false);
			}
		});
	}
	
	public void show(Consumer<Object> callback)
	{
		nameInput.setVisible(true);
		currentCallback = callback;
		
	}
}
