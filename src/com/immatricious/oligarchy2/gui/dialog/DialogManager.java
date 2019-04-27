package com.immatricious.oligarchy2.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DialogManager {

	private CharInputDialog cid;
	private AccInputDialog aid;
	private NameInputDialog nid;
	private CharacterChooseDialog ccd;
	private PriorityChooseDialog pcd;
	
	public void init()
	{
		cid = new CharInputDialog();
		aid = new AccInputDialog();
		nid = new NameInputDialog();
		ccd = new CharacterChooseDialog();
		pcd = new PriorityChooseDialog();
	}

	public void showCharacterInput(DialogCallback cb){cid.show(cb);}
	public void showAccountInput(){aid.show();}
	public void showNameInput(Consumer<Object> callback){nid.show(callback); }
	public void showCharacterChoose() { ccd.show(); }

	public void showPriorityChoose() { pcd.show(); }
	
	
	
}
