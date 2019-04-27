package com.immatricious.oligarchy2.gui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.immatricious.macromanager.character.Account;
import com.immatricious.oligarchy2.OligarchyApplication;

public class AccInputDialog {
	private JDialog accInput;
	private JTextField username;
	private JPasswordField password;
	private JLabel usernameLabel;
	private JLabel passLabel;
	private JButton okBtn;
	private JButton cancelBtn;
	
	public AccInputDialog()
	{
		initAccountInput();
	}
	
	private void initAccountInput()
	{
		accInput = new JDialog();
		accInput.setTitle("Account Information");
		accInput.setSize(300,140);
		
		GridBagLayout gbl = new GridBagLayout();
		
		accInput.setLayout(gbl);
		
		GridBagConstraints c = new GridBagConstraints();
		
		username = new JTextField();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.8;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(10,0,0,30);
		accInput.add(username,c);
		
		password = new JPasswordField();
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(10,0,0,30);
		accInput.add(password, c);
		
		usernameLabel = new JLabel("Username:");
		c.weightx = 0.2;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10,30,0,0);
		accInput.add(usernameLabel, c);
		
		passLabel = new JLabel("Password:");
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(10,30,0,0);
		accInput.add(passLabel, c);

		okBtn = new JButton("Ok");
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(10,30,10,15);
		accInput.add(okBtn, c);
		
		okBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JList<String> accList = (JList<String>) OligarchyApplication.getCurrentApplication().getGUIManager().getBottomBar().getNamedComponent("accountList");
				
				
				OligarchyApplication.getCurrentApplication().getTaskHandler().getCharacterManager().addAccount(new Account(username.getText(), new String(password.getPassword())));
				
				username.setText("");
				password.setText("");
				
				accInput.setVisible(false);
			}
			
		});
	}
	
	public void show()
	{
		accInput.setVisible(true);
	}
}
