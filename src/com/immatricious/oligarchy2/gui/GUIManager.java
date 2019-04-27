package com.immatricious.oligarchy2.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.immatricious.oligarchy2.OligarchyApplication;
import com.immatricious.oligarchy2.gui.dialog.DialogManager;

public class GUIManager {
	
	private JFrame appFrame;
	private DialogManager dm;
	
	private JTabbedPane sidebar;//Data Insert ui
	private JTabbedPane bottombar;//Data Read ui
	private JPanel mapDisplay;
	
	public GUIManager()
	{
		dm = new DialogManager();
		sidebar = new InsertPanel();
		bottombar = new ReadPanel();
	}
	
	public void init()
	{
		dm.init();

		GridBagConstraints c = new GridBagConstraints();
		
		appFrame = new JFrame("Oligarchy");
		appFrame.setSize(1024, 600);
		GridBagLayout b1 = new GridBagLayout();
		appFrame.setLayout(b1);
		
		appFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent windowEvent) {
				OligarchyApplication.getCurrentApplication().getTaskHandler().close();
				System.exit(0);
			}
		});
		
		appFrame.setIconImage(new ImageIcon("assets/ico.png").getImage());

		((InsertPanel)sidebar).initSidebar();
		((ReadPanel)bottombar).initTaskPanel();

		JPanel combinedBars = new JPanel();
		GridLayout b2 = new GridLayout(1,2);
		combinedBars.setLayout(b2);
		combinedBars.add(sidebar);
		combinedBars.add(bottombar);
		
		combinedBars.setMaximumSize(new Dimension(Integer.MAX_VALUE,200));
		
		c.weighty = 4;
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		initMapDisplay();
		appFrame.add(mapDisplay,c);
		
		c.weighty = 0;
		c.gridy = 1;
		appFrame.add(combinedBars,c);
		
		//dm.showCharacterInput();
		
		//appFrame.pack();
		appFrame.setVisible(true);
		((MapPanel)mapDisplay).resize();
	}
	
	public void initMapDisplay()
	{
		mapDisplay = new MapPanel();
	}
	
	public void resize()
	{
		mapDisplay.setMinimumSize(new Dimension(appFrame.getWidth(),300));
	}
	
	public DialogManager getDialogManager() { return this.dm; }

	public Component getTaskList()
	{
		return ((ReadPanel)bottombar).getNamedComponent("tasklist");
	}
	
	public Component getTaskPool()
	{
		return ((ReadPanel)bottombar).getNamedComponent("taskpool");
	}
	
	public ReadPanel getBottomBar() { return (ReadPanel) this.bottombar; }
	
	public MapPanel getMapDisplay() { return (MapPanel) this.mapDisplay; }
}
