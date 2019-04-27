package com.immatricious.oligarchy2.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import com.immatricious.macromanager.character.Account;
import com.immatricious.macromanager.character.CharacterManager;
import com.immatricious.macromanager.character.Character;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventListener;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.macromanager.task.TaskPriority;
import com.immatricious.macromanager.util.MMUtilities;
import com.immatricious.oligarchy2.OligarchyApplication;
import com.immatricious.oligarchy2.gui.dialog.CharInputDialog;
import com.immatricious.oligarchy2.gui.dialog.DialogCallback;
import com.immatricious.oligarchy2.gui.dialog.OligarchyDialog;

@SuppressWarnings("serial")
public class ReadPanel extends JTabbedPane
{
	private Map<String,Component> components;
	private long lastStartedClient;
	
	private JPanel connectionTab;
	private JPanel toolBoxTab;
	private JPanel characterTab;
	private JPanel taskTab;
	
	public ReadPanel()
	{
		components = new HashMap<String,Component>();
		lastStartedClient = 0;
	}
	
	public void inittaskTab()
	{
		buildTaskTab();
		buildCharacterTab();
		buildConnectionTab();
		buildToolboxTab();
		
		initListeners();
	}
	
	private void buildTaskTab()
	{
		//TASK TAB
		taskTab = new JPanel();
		taskTab.setLayout(new BoxLayout(taskTab,BoxLayout.Y_AXIS));
		taskTab.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel labelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel taskLabel = new JLabel("Tasks");
		labelContainer.add(taskLabel);
		
		JPanel listContainer = new JPanel();
		listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.X_AXIS));
		
		JList<String> taskList = new JList<String>(new DefaultListModel<String>());
		JList<String> priorityList = new JList<String>(new DefaultListModel<String>());
		
		JScrollPane scrollList = new JScrollPane(taskList);
		JScrollPane scrollPriority = new JScrollPane(priorityList);
		
		scrollPriority.setMaximumSize(new Dimension(160,Integer.MAX_VALUE));
		scrollPriority.setMinimumSize(new Dimension(160,0));
		
		components.put("tasklist", taskList);
		components.put("prioritylist", priorityList);
	
		listContainer.add(scrollList);
		listContainer.add(Box.createRigidArea(new Dimension(10,0)));
		listContainer.add(scrollPriority);
		
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton removeBtn = new JButton("Remove Task");
		JButton poolTask = new JButton("Pool Task");
		JButton assignTask = new JButton("Assign Task");
	
		buttonContainer.add(removeBtn);
		buttonContainer.add(poolTask);
		buttonContainer.add(assignTask);
	
		taskTab.add(labelContainer);
		taskTab.add(listContainer);
		taskTab.add(new JSeparator());
		taskTab.add(buttonContainer);
		
		this.addTab("Tasks", taskTab);
		
		//Adding button listeners
		
		assignTask.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				OligarchyApplication.getCurrentApplication().getGUIManager().getDialogManager().showCharacterChoose();
			}
		});
		
		poolTask.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				OligarchyApplication.getCurrentApplication().getGUIManager().getDialogManager().showPriorityChoose();
			}
		});
	}
		
	private void buildCharacterTab()
	{
		//CHARACTER AND ACCOUNT TAB
		characterTab = new JPanel();
		characterTab.setLayout(new BoxLayout(characterTab,BoxLayout.Y_AXIS));
		characterTab.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel accCharLabelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel accCharLabel = new JLabel("Accounts and Characters");
		accCharLabelContainer.add(accCharLabel);
		
		JPanel accountListContainer = new JPanel();
		accountListContainer.setLayout(new BoxLayout(accountListContainer, BoxLayout.X_AXIS));
		
		JList<String> accountList = new JList<String>(new DefaultListModel<String>());
		JList<String> characterList = new JList<String>(new DefaultListModel<String>());
	
		JScrollPane accountListPane = new JScrollPane(accountList);
		JScrollPane characterListPane = new JScrollPane(characterList);
		
		characterListPane.setMaximumSize(new Dimension(160,Integer.MAX_VALUE));
		characterListPane.setMinimumSize(new Dimension(100,0));		
		
		accountListContainer.add(accountListPane);
		accountListContainer.add(Box.createRigidArea(new Dimension(10,0)));
		accountListContainer.add(characterListPane);
		
		JPanel accButtonContainer = new JPanel();
		accountListContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
	
		JButton addAccountBtn = new JButton("+Account");
		JButton addCharacterBtn = new JButton("+Character");
		
		accButtonContainer.add(addAccountBtn);
		accButtonContainer.add(addCharacterBtn);
		
		characterTab.add(accCharLabelContainer);
		characterTab.add(accountListContainer);
		characterTab.add(new JSeparator());
		characterTab.add(accButtonContainer);
	
		components.put("accountList", accountList);
		components.put("characterList", characterList);
		
		this.addTab("Characters & Accounts", characterTab);
		
		addAccountBtn.addActionListener(new ActionListener()
		{
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OligarchyApplication.getCurrentApplication().getGUIManager().getDialogManager().showAccountInput();
			}
			
		});
		
		addCharacterBtn.addActionListener(new ActionListener()
		{
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Careate a callback for opening character input from this button's listener
				DialogCallback cb = new DialogCallback(){
					@Override
					public void act(OligarchyDialog d) {
						CharInputDialog castedd = (CharInputDialog) d; 
						Account acc = castedd.readAccount();
						String characterName = castedd.readCharacterName();
						Character nc = new Character(acc, characterName, new int[13], new int[9]);
						
						OligarchyApplication.	getCurrentApplication().
												getTaskHandler().
												getCharacterManager().addCharacter(nc);
					}
				};
				
				
				OligarchyApplication.	getCurrentApplication().
										getGUIManager().
										getDialogManager().showCharacterInput(cb);
			}
			
		});
	}
		
	private void buildConnectionTab()
	{
		//CONNECTIONS TAB
		connectionTab = new JPanel();
		connectionTab.setLayout(new BoxLayout(connectionTab,BoxLayout.Y_AXIS));
		connectionTab.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel connectLabelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel connectLabel = new JLabel("Connections");
		connectLabelContainer.add(connectLabel);
		
		JPanel connectionListContainer = new JPanel();
		connectionListContainer.setLayout(new BoxLayout(connectionListContainer, BoxLayout.X_AXIS));
		JList<String> connectionList = new JList<String>(new DefaultListModel<String>());
		JScrollPane connectionListPane = new JScrollPane(connectionList);
		components.put("connectionList", connectionList);
		connectionListContainer.add(connectionListPane);
		
		JPanel connectionControlContainer = new JPanel();
		connectionControlContainer.setLayout(new BoxLayout(connectionControlContainer, BoxLayout.X_AXIS));
		connectionControlContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE,128));
		JButton startConnectionBtn = new JButton("Boot Client");
		startConnectionBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				long now = System.currentTimeMillis();
				if(now - lastStartedClient < 5000)
					return;
				
				MMUtilities.runOligarchyClient();
				lastStartedClient = now;
			}
		});
		JButton stopConnectionBtn = new JButton("Stop Client");
		stopConnectionBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> selectedConnections = connectionList.getSelectedValuesList();
				String connectionId = selectedConnections.get(0).split(":")[0];
				int c = Integer.parseInt(connectionId);
				System.out.println(c);
				MMUtilities.stopOligarchyClient(c);
			}
		});
		connectionControlContainer.add(startConnectionBtn);
		connectionControlContainer.add(stopConnectionBtn);
		/*connectionListPane.setMaximumSize(new Dimension(160,Integer.MAX_VALUE));
		connectionListPane.setMinimumSize(new Dimension(160,0));*/
		
		
		connectionTab.add(connectLabelContainer);
		connectionTab.add(connectionListContainer);
		connectionTab.add(connectionControlContainer);
		this.addTab("Connections", connectionTab);
	}
		
	private void buildToolboxTab(){
	//TOOLBOX TAB
	toolBoxTab = new JPanel();
	toolBoxTab.setLayout(new BoxLayout(toolBoxTab,BoxLayout.Y_AXIS));
	toolBoxTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	
	JPanel toolLabelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JLabel toolLabel = new JLabel("Toolbox and usefulities");
	toolLabelContainer.add(toolLabel);
	
	JPanel mapUtilityContainer = new JPanel();
	mapUtilityContainer.setLayout(new BoxLayout(mapUtilityContainer,BoxLayout.X_AXIS));
	mapUtilityContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	
	JPanel mapViewContainer = new JPanel();
	mapViewContainer.setLayout(new BoxLayout(mapViewContainer,BoxLayout.Y_AXIS));
	mapViewContainer.setMaximumSize(new Dimension(200,Integer.MAX_VALUE));
	
	JPanel zoomContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel zoomLabelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JLabel zoomLabel = new JLabel("Zoom");
	zoomLabelContainer.add(zoomLabel);
	JButton zoominBtn = new JButton("+");
	JButton zoomoutBtn = new JButton("-");
	zoomContainer.add(zoomLabelContainer);
	zoomContainer.add(zoominBtn);
	zoomContainer.add(zoomoutBtn);
	
	zoominBtn.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			MapPanel mp = OligarchyApplication.getCurrentApplication().getGUIManager().getMapDisplay();
			mp.setScale(Math.min(mp.getScale()+1,5));
		}
	});
	
	zoomoutBtn.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			MapPanel mp = OligarchyApplication.getCurrentApplication().getGUIManager().getMapDisplay();
			mp.setScale(Math.max(mp.getScale()-1,1));
		}
	});
	
	JPanel offsetContainer = new JPanel();
	offsetContainer.setLayout(new BoxLayout(offsetContainer,BoxLayout.Y_AXIS));
	JPanel offsetLabelContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JLabel offsetLabel = new JLabel("Offset");
	offsetLabelContainer.add(offsetLabel);
	
	SpinnerNumberModel snm = new SpinnerNumberModel(0,Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	SpinnerNumberModel snm2 = new SpinnerNumberModel(0,Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	//Spinner x
	JPanel cx = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JSpinner spinnerx = new JSpinner(snm);
	JLabel xLabel = new JLabel("X: ");
	components.put("mapx",spinnerx);
	cx.add(xLabel);
	cx.add(spinnerx);
	//Spinner y
	JPanel cy = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JSpinner spinnery = new JSpinner(snm2);
	JLabel yLabel = new JLabel("Y: ");
	components.put("mapy",spinnery);
	cy.add(yLabel);
	cy.add(spinnery);

	offsetContainer.add(offsetLabelContainer);
	offsetContainer.add(cx);
	offsetContainer.add(cy);
	
	mapViewContainer.add(zoomContainer);
	mapViewContainer.add(new JSeparator(JSeparator.HORIZONTAL));
	mapViewContainer.add(offsetContainer);
	
	mapUtilityContainer.add(mapViewContainer);
	//mapUtilityContainer.add(new JSeparator(JSeparator.VERTICAL));
	
	toolBoxTab.add(toolLabelContainer);
	//mapUtilityContainer.add(new JSeparator(JSeparator.HORIZONTAL));
	toolBoxTab.add(mapUtilityContainer);
	
	this.addTab("Toolbox", toolBoxTab);
}
	
	public void initListeners()
	{
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.TASK_INSERT, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				Task task = (Task) ((MMEventDataMap)event.getEventData()).getData("task").value;
				JList<String> taskList = (JList<String>) components.get("tasklist");
				((DefaultListModel<String>)taskList.getModel()).addElement(task.getName());
			}

			@Override
			public MMEventType getEventType() { return MMEventType.TASK_INSERT; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.TASK_REMOVE, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				Task task = (Task) ((MMEventDataMap)event.getEventData()).getData("task").value;
				JList<String> taskList = (JList<String>) components.get("tasklist");
				((DefaultListModel<String>)taskList.getModel()).removeElement(task.getName());
			}

			@Override
			public MMEventType getEventType() { return MMEventType.TASK_REMOVE; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.TASK_POOLED, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				Task task = (Task) ((MMEventDataMap)event.getEventData()).getData("task").value;
				TaskPriority priority = (TaskPriority) ((MMEventDataMap)event.getEventData()).getData("priority").value;
				JList<String> priorityList = (JList<String>) components.get("prioritylist");
				((DefaultListModel<String>)priorityList.getModel()).addElement(task.getName()+":"+priority.name());
			}

			@Override
			public MMEventType getEventType() { return MMEventType.TASK_POOLED; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.TASK_UNPOOLED, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				Task task = (Task) ((MMEventDataMap)event.getEventData()).getData("task").value;
				JList<String> priorityList = (JList<String>) components.get("prioritylist");
				Enumeration<String> elements = ((DefaultListModel<String>)priorityList.getModel()).elements();
				while(elements.hasMoreElements())
				{
					String element = elements.nextElement();
					String[] data = element.split(":");
					if(task.getName().equals(data[0]))
					{
						((DefaultListModel<String>)priorityList.getModel()).removeElement(element);
						break;
					}
				}
			}

			@Override
			public MMEventType getEventType() { return MMEventType.TASK_UNPOOLED; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.CONNECTION_STARTED, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				JList<String> connectionList = (JList<String>) components.get("connectionList");
				ThreadedConnection tc = (ThreadedConnection)((MMEventDataMap)event.getEventData()).getData("connection").value;
				((DefaultListModel<String>)connectionList.getModel()).addElement(tc.getConnectionId() + ":" + tc.getIP());
			}

			@Override
			public MMEventType getEventType() { return MMEventType.CONNECTION_STARTED; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.CONNECTION_ENDED, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				JList<String> connectionList = (JList<String>) components.get("connectionList");
				ThreadedConnection tc = (ThreadedConnection)((MMEventDataMap)event.getEventData()).getData("connection").value;
				DefaultListModel<String> connectionModel = ((DefaultListModel<String>)connectionList.getModel());
				Enumeration<String> elements = connectionModel.elements();
				while(elements.hasMoreElements())
				{
					String element = elements.nextElement();
					String[] data = element.split(":");
					if(tc.getConnectionId() == Long.parseLong(data[0]))
					{
						connectionModel.removeElement(element);
						break;
					}
				}
			}

			@Override
			public MMEventType getEventType() { return MMEventType.CONNECTION_ENDED; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.CONNECTION_LOGGED_ON, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				JList<String> connectionList = (JList<String>) components.get("connectionList");
				ThreadedConnection tc = (ThreadedConnection)((MMEventDataMap)event.getEventData()).getData("connection").value;
				DefaultListModel<String> connectionModel = ((DefaultListModel<String>)connectionList.getModel());
				Enumeration<String> elements = connectionModel.elements();
				while(elements.hasMoreElements())
				{
					String element = elements.nextElement();
					String[] data = element.split(":");
					if(tc.getId() == Long.parseLong(data[0]))
					{
						connectionModel.removeElement(element);
						String e = tc.getConnectionId() + ":" + tc.getIP() + ":" + tc.getAccount().getUsername();
						if(MMUtilities.startedProcesses.get(tc.getConnectionId())!=null)e += ":sub";
						connectionModel.addElement(e);
						CharacterManager cm = OligarchyApplication.getCurrentApplication().getTaskHandler().getCharacterManager();
						Account acc = cm.getAccount(tc.getAccount().getUsername());
						if(acc == null)
							cm.addAccount(tc.getAccount());
						break;
					}
				}
			}

			@Override
			public MMEventType getEventType() { return MMEventType.CONNECTION_LOGGED_ON; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.CONNECTION_LOGGED_OFF, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				JList<String> connectionList = (JList<String>) components.get("connectionList");
				ThreadedConnection tc = (ThreadedConnection)((MMEventDataMap)event.getEventData()).getData("connection").value;
				DefaultListModel<String> connectionModel = ((DefaultListModel<String>)connectionList.getModel());
				Enumeration<String> elements = connectionModel.elements();
				while(elements.hasMoreElements())
				{
					String element = elements.nextElement();
					String[] data = element.split(":");
					if(tc.getId() == Long.parseLong(data[0]))
					{
						connectionModel.removeElement(element);
						connectionModel.addElement(tc.getId() + ":" + tc.getIP());
						break;
					}
				}
			}

			@Override
			public MMEventType getEventType() { return MMEventType.CONNECTION_LOGGED_OFF; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.ACCOUNT_ADDED, new MMEventListener()
		{
			@Override
			public void act(MMEvent event)
			{
				JList<String> accountList = (JList<String>) components.get("accountList");
				Account acc = (Account) ((MMEventDataMap)event.getEventData()).getData("account").value;
				components.get("accountList");
				DefaultListModel<String> accountListModel = ((DefaultListModel<String>)accountList.getModel());
				accountListModel.addElement(acc.getUsername());
			}

			@Override
			public MMEventType getEventType() { return MMEventType.ACCOUNT_ADDED; }
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.CHARACTER_ADDED,new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				Character cha = (Character) ((MMEventDataMap)event.getEventData()).getData("character").value;
				JList charList = (JList) components.get("characterList");
				DefaultListModel<String> charListModel = (DefaultListModel<String>) charList.getModel();
				charListModel.addElement(cha.getName() + ":" + cha.getAccount().getUsername());
			}

			@Override
			public MMEventType getEventType() { return MMEventType.CHARACTER_ADDED; }
			
		});
	}

	
	public Component getNamedComponent(String componentName)
	{
		return components.getOrDefault(componentName, null);
	}
	
	public void updateTasks()
	{
		JList<String> priorityList = (JList<String>) components.get("prioritylist");
		
		Map<TaskPriority,List<Task>> taskPool =  OligarchyApplication.getCurrentApplication().getTaskHandler().getTaskPool();
		
		for(int i = 0; i < 3; i++)
		{
			TaskPriority p = TaskPriority.getFromInt(i);
			List<Task> tasks = taskPool.get(p);
			
			for(Task t : tasks)
			{
				boolean contained = false;
				
				for(int n2 = 0, l = priorityList.getModel().getSize(); n2 < l; n2++)
				{
					String s = priorityList.getModel().getElementAt(n2);
					if(s.split(":")[1].equals(t.getName()))
					{
						contained = true;
						break;
					}
				}
				
				if(!contained)
					((DefaultListModel<String>)priorityList.getModel()).addElement(p.toString() + ":" + t.getName());
			}
		}
	}
}
