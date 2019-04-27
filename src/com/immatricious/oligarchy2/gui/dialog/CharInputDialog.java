package com.immatricious.oligarchy2.gui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.immatricious.macromanager.character.*;
import com.immatricious.macromanager.character.Character;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEventListener;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.oligarchy2.OligarchyApplication;

/**
 * Manages char input dialog and events
 * @author June
 *
 */
public class CharInputDialog extends OligarchyDialog{
	
	private JTextField username;
	private JComboBox<String> accBox;
	private JLabel usernameLabel;
	private JLabel passLabel;
	private JButton okBtn;
	private JButton cancelBtn;
	
	private String name;//Only used for callbacks and updated right before one is triggered
	private Account account;//Idem ^
	
	public CharInputDialog()
	{
		super(new JDialog());
		initCharacterInput();
		setAccounts();
	}
	
	private void initCharacterInput()
	{
		this.dialogBox.setTitle("Character Information");
		this.dialogBox.setSize(300,140);
		
		this.dialogBox.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		username = new JTextField();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.8;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(10,0,0,30);
		this.dialogBox.add(username,c);
		
		accBox = new JComboBox<String>();
		c.weightx = 0.8;
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(10,0,0,30);
		this.dialogBox.add(accBox, c);
		
		usernameLabel = new JLabel("Character name");
		c.weightx = 0.2;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10,30,0,0);
		this.dialogBox.add(usernameLabel,c);
		
		passLabel = new JLabel("Account");
		c.weightx = 0.2;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(10,30,0,0);
		this.dialogBox.add(passLabel,c);

		okBtn = new JButton("Ok");
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(10,30,10,15);
		this.dialogBox.add(okBtn, c);
		okBtn.setActionCommand("ok");
		
		CharInputDialog me = this;
		
		okBtn.addActionListener(new ActionListener(){	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String cmd = e.getActionCommand();
				if("ok".equals(cmd))
				{
					triggerCallback(me);
					/*if(cm.getCharacter(name) == null)
					{
						cm.addCharacter(new Character(account, name, new int[13], new int[9]));
						System.out.println("added " +name+ " to account " + account.getUsername());
					}*/
					
					account.add(name);
				}
				dialogBox.setVisible(false);
			}
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.ACCOUNT_ADDED, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				Account acc = (Account) ((MMEventDataMap)event.getEventData()).getData("account").value;
				DefaultComboBoxModel<String> accBoxModel = ((DefaultComboBoxModel<String>)accBox.getModel());
				accBoxModel.addElement(acc.getUsername());
			}

			@Override
			public MMEventType getEventType() {return MMEventType.ACCOUNT_ADDED;}
			
		});
	}

	public Account readAccount() {
		return OligarchyApplication.getCurrentApplication().
									getTaskHandler().
									getCharacterManager().getAccount((String) accBox.getSelectedItem());
	}
	public String readCharacterName() { return username.getText();}
	
	public void setAccounts()
	{
		for(String n : 	OligarchyApplication.
						getCurrentApplication().
						getTaskHandler().
						getCharacterManager().getAccountNames())
			accBox.addItem(n);
	}
}
