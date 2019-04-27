package com.immatricious.oligarchy2.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.immatricious.macromanager.environment.HashedLocation;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventListener;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.task.Action;
import com.immatricious.macromanager.task.ActionTarget;
import com.immatricious.macromanager.task.ActionTargetDataType;
import com.immatricious.macromanager.task.ActionType;
import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.macromanager.task.TaskRequirement;
import com.immatricious.macromanager.util.CommandListener;
import com.immatricious.macromanager.util.CommandType;
import com.immatricious.macromanager.util.DataParser;
import com.immatricious.oligarchy2.OligarchyApplication;

/**
 * Manages the panel and interactions within it
 * @author June
 *
 */
public class InsertPanel extends JTabbedPane{

	private JPanel taskBuilderTab;
	private JPanel prePathTab;
	private JPanel hashLocTab;
	private JPanel chatterTab;
	private JPanel consoleTab;
	private JPanel cconsoleTab;

	private static final String stringForm = "s";
	private static final String vectorForm = "v";
	
	private Map<String,Component> components = new HashMap<String,Component>();
	
	private List<Action> currentTaskActions = new ArrayList<Action>();
	private Action currentAction;
	
	private DefaultListModel<String> dlm;
	
	public InsertPanel()
	{
		super(JTabbedPane.TOP);
	}
	
	public void initSidebar()
	{
		initTaskBuilderTab();
		initPathTab();
		initHashLocTab();
		initChatter();
		initConsole();
		initClientConsole();
		
		initListeners();
	}
	
	private void initPathTab() {
		prePathTab = new JPanel();
		this.addTab("PrePath", prePathTab);
	}
	
	private void initHashLocTab() {
		hashLocTab = new JPanel();
		hashLocTab.setLayout(new BoxLayout(hashLocTab,BoxLayout.Y_AXIS));
		
		JPanel labelContainer = new JPanel();
		labelContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("Hashed Locations");
		
		labelContainer.add(label);
		
		JPanel hashLocContainer = new JPanel();
		hashLocContainer.setLayout(new BoxLayout(hashLocContainer,BoxLayout.Y_AXIS));
		JList<String> hashLocList = new JList<String>(new DefaultListModel<String>());
		JScrollPane HashListPane = new JScrollPane(hashLocList);
		
		hashLocContainer.add(HashListPane);
		
		hashLocTab.add(labelContainer);
		hashLocTab.add(hashLocContainer);
		
		components.put("hashList", hashLocList);
		
		this.addTab("HashLoc", hashLocTab);
	}

	private void initChatter()
	{
		chatterTab = new JPanel();
		chatterTab.setLayout(new BoxLayout(chatterTab,BoxLayout.Y_AXIS));
		
		JPanel labelContainer = new JPanel();
		labelContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("Chatter");
		
		labelContainer.add(label);
		
		JPanel chatContainer = new JPanel();
		chatContainer.setLayout(new BoxLayout(chatContainer,BoxLayout.Y_AXIS));
		JList<String> chatList = new JList<String>(new DefaultListModel<String>());
		JScrollPane chatListPane = new JScrollPane(chatList);
		
		chatContainer.add(chatListPane);
		
		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new BoxLayout(inputContainer,BoxLayout.X_AXIS));
		
		JPanel cboxContainer = new JPanel();
		cboxContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JComboBox<String> connectionBox = new JComboBox<String>();
		JComboBox<String> channelBox = new JComboBox<String>();
		JButton sendBtn = new JButton(">");
		JTextField sendField = new JTextField();

		((DefaultComboBoxModel<String>)channelBox.getModel()).addElement("v");
		((DefaultComboBoxModel<String>)channelBox.getModel()).addElement("l");
		((DefaultComboBoxModel<String>)channelBox.getModel()).addElement("r");
		((DefaultComboBoxModel<String>)channelBox.getModel()).addElement("b");
		
		cboxContainer.add(connectionBox);
		cboxContainer.add(channelBox);
		
		inputContainer.add(cboxContainer);
		inputContainer.add(sendBtn);
		inputContainer.add(sendField);

		
		connectionBox.setSize(new Dimension(240,70));
		inputContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
		
		chatterTab.add(labelContainer);
		chatterTab.add(chatContainer);
		chatterTab.add(new JSeparator(JSeparator.HORIZONTAL));
		chatterTab.add(inputContainer);

		components.put("chatList", chatList);
		components.put("connectionBox", connectionBox);
		components.put("channelBox", channelBox);
		
		this.addTab("Chat", chatterTab);
		
		sendBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String msg = sendField.getText();

				JComboBox<String> connectionBox = (JComboBox<String>) components.get("connectionBox");
				JComboBox<String> channelBox = (JComboBox<String>) components.get("channelBox");
				DefaultComboBoxModel<String> connectionModel = (DefaultComboBoxModel<String>) connectionBox.getModel();
				DefaultComboBoxModel<String> channelModel = (DefaultComboBoxModel<String>) channelBox.getModel();
				int connectionID = Integer.parseInt((String)connectionModel.getSelectedItem());
				String channelID = (String) channelModel.getSelectedItem();
				
				System.out.println("TRying to talk to " + connectionID + " on channel " + channelID + ": " + msg);
				
				OligarchyApplication.getCurrentApplication().getTaskHandler().getConnectionManager().sendChatMessageToConnection(connectionID, msg, channelID);
				
				sendField.setText("");
			}
			
		});
	}
	
	private void initConsole()
	{
		consoleTab = new JPanel();
		consoleTab.setLayout(new BoxLayout(consoleTab,BoxLayout.Y_AXIS));
		
		JPanel labelContainer = new JPanel();
		labelContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("Console");
		
		labelContainer.add(label);
		
		JPanel consoleContainer = new JPanel();
		consoleContainer.setLayout(new BoxLayout(consoleContainer,BoxLayout.Y_AXIS));
		JList<String> consoleList = new JList<String>(new DefaultListModel<String>());
		JScrollPane consoleListPane = new JScrollPane(consoleList);
		
		consoleContainer.add(consoleListPane);
		
		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new BoxLayout(inputContainer,BoxLayout.X_AXIS));
		JButton sendBtn = new JButton(">");
		JTextField sendField = new JTextField();
		
		inputContainer.add(sendBtn);
		inputContainer.add(sendField);
		
		inputContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
		
		consoleTab.add(labelContainer);
		consoleTab.add(consoleContainer);
		consoleTab.add(new JSeparator(JSeparator.HORIZONTAL));
		consoleTab.add(inputContainer);
		
		components.put("consoleList", consoleList);
		
		this.addTab("Console", consoleTab);
		
		sendBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String cmd = sendField.getText();
				TaskHandler.log.parseCommand(cmd);
				sendField.setText("");
			}
			
		});
	}
	
	private void initClientConsole()
	{
		cconsoleTab = new JPanel();
		cconsoleTab.setLayout(new BoxLayout(cconsoleTab,BoxLayout.Y_AXIS));
		
		JPanel labelContainer = new JPanel();
		labelContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("Clients Console");
		
		labelContainer.add(label);
		
		JPanel consoleContainer = new JPanel();
		consoleContainer.setLayout(new BoxLayout(consoleContainer,BoxLayout.Y_AXIS));
		JList<String> consoleList = new JList<String>(new DefaultListModel<String>());
		JScrollPane consoleListPane = new JScrollPane(consoleList);
		
		consoleContainer.add(consoleListPane);
		
		cconsoleTab.add(labelContainer);
		cconsoleTab.add(consoleContainer);
		cconsoleTab.add(new JSeparator(JSeparator.HORIZONTAL));
		
		components.put("cconsoleList", consoleList);
		
		this.addTab("Client Log", cconsoleTab);
	}
	
	private void initTaskBuilderTab()
	{
		taskBuilderTab = new JPanel();
		//taskBuilderTab.setBackground(Color.BLUE);
		taskBuilderTab.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Create form
		JPanel form = new JPanel();
		//form.setBackground(Color.CYAN);
		form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));
		form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//Populate form
		
		//Task Actions Label
		JPanel alContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel al = new JLabel("Task Actions: ");
		alContainer.add(al);
		
		//Generate actionList
		dlm = new DefaultListModel<String>();
		JList<String> actionList = new JList<String>(dlm);
		actionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		actionList.setLayoutOrientation(JList.VERTICAL);
		components.put("actionlist", actionList);

		JScrollPane actionZone = new JScrollPane(actionList);
		actionZone.setPreferredSize(new Dimension(200,80));
		//actionZone.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		
		//Add text field for reqs
		JPanel reqs = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel reqLabel = new JLabel("Requirements: ");
		JTextField reqAbText = new JTextField("0,0,0,0,0,0,0,0,0,0,0,0,0");
		JTextField reqAtText = new JTextField("0,0,0,0,0,0,0,0,0");

		components.put("reqab",reqAbText);
		components.put("reqat",reqAtText);
		
		reqAbText.setPreferredSize(new Dimension(160,24));
		reqAtText.setPreferredSize(new Dimension(160,24));
		
		reqs.add(reqLabel);
		reqs.add(reqAbText);
		reqs.add(reqAtText);
		
		//insert current action button
		JButton insertBtn = new JButton("Insert action");
		
		//remove selected action button
		JButton removeBtn = new JButton("Remove selected");
		
		//Add req button
		JButton reqBtn = new JButton("Add req.");
		
		//"Generate" button
		JButton genBtn = new JButton("Generate");
		//genBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//Add buttons
		buttons.add(insertBtn);
		buttons.add(removeBtn);
		buttons.add(reqBtn);
		buttons.add(genBtn);
		
		form.add(alContainer);
		form.add(actionZone);
		form.add(reqs);
		form.add(new JSeparator());
		form.add(buttons);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.BASELINE_TRAILING;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		taskBuilderTab.add(new JScrollPane(form),c);
		
		//Create data target form
		JPanel dataTargetContainer = new JPanel();
		dataTargetContainer.setLayout(new BoxLayout(dataTargetContainer,BoxLayout.Y_AXIS));
		//dataTargetContainer.setBackground(Color.DARK_GRAY);
		dataTargetContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//Action cbox
		JComboBox<ActionType> actions = new JComboBox<ActionType>();
		//Generate actions
		for(ActionType a : ActionType.values())
			actions.addItem(a);
		
		//Contain label and combo box 
		JPanel actionContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel taskLabel = new JLabel("Action: ");

		actionContainer.add(taskLabel);
		actionContainer.add(actions);
		components.put("action", actions);
		
		//Data type combo box
		JPanel cboxContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JComboBox<ActionTargetDataType> dataCbox = new JComboBox<ActionTargetDataType>();
		for(ActionTargetDataType t : ActionTargetDataType.values())
			dataCbox.addItem(t);
		JLabel cboxLabel = new JLabel("Data type: ");
		
		//Containing label and combo box together
		cboxContainer.add(cboxLabel);
		cboxContainer.add(dataCbox);
		components.put("datatype", dataCbox);
		
		//Create cards panels
		
		//Single string
		JPanel stringCard = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JTextField stringCardInput = new JTextField();
		JLabel stringLabel = new JLabel("Value: ");
		stringCard.add(stringLabel);
		stringCardInput.setPreferredSize(new Dimension(128,24));
		stringCard.add(stringCardInput);
		components.put("stringdata", stringCardInput);
		
		//Two interactively previewed spinners
		JPanel vectorCard = new JPanel();
		vectorCard.setLayout(new BoxLayout(vectorCard,BoxLayout.Y_AXIS));
		
		//Set spinner model
		SpinnerNumberModel snm = new SpinnerNumberModel(0,Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
		SpinnerNumberModel snm2 = new SpinnerNumberModel(0,Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
		
		//group spinners and labels
		//Spinner x
		JPanel cx = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JSpinner spinnerx = new JSpinner(snm);
		JLabel xLabel = new JLabel("X: ");
		components.put("vecx",spinnerx);
		cx.add(xLabel);
		cx.add(spinnerx);
		
		//Spinner y
		JPanel cy = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JSpinner spinnery = new JSpinner(snm2);
		JLabel yLabel = new JLabel("Y: ");
		components.put("vecy",spinnery);
		cy.add(yLabel);
		cy.add(spinnery);
		
		//Add spinners in
		vectorCard.add(cx);
		vectorCard.add(cy);
		
		//Data specific card layout
		JPanel dataCards = new JPanel(new CardLayout());

		dataCards.add(stringCard,stringForm);
		dataCards.add(vectorCard,vectorForm);
		
		//Adding the switching effect to the cards
		dataCbox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == 1)
				{
					CardLayout cl = (CardLayout)(dataCards.getLayout());
					String value = (ActionTargetDataType.VECTOR == e.getItem())?vectorForm:stringForm;
					cl.show(dataCards, value);
					System.out.println(value);
				}
			}
		});
		
		//Create action insert text dialog
		JPanel actionInsert = new JPanel();
		actionInsert.setLayout(new BoxLayout(actionInsert, BoxLayout.X_AXIS));
		actionInsert.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		JTextField actionText = new JTextField();
		actionText.setMaximumSize(new Dimension(Integer.MAX_VALUE,24));
		components.put("actionstring",actionText);
		JButton appendBtn = new JButton("Assemble");

		actionInsert.add(actionText);
		actionInsert.add(appendBtn);
		
		//Button listener
		insertBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(currentAction != null)
				{
					((JTextField)components.get("actionstring")).setText("");
					dlm.addElement(currentAction.getMessageString());
					currentTaskActions.add(currentAction);
					currentAction = null;
				}
			}
		});
		removeBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dlm.removeElement(((JList<String>)components.get("actionlist")).getSelectedValue());
			}
		});
		
		appendBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionType actionType = (ActionType) ((JComboBox<ActionType>)components.get("action")).getSelectedItem();
				ActionTargetDataType dataType = (ActionTargetDataType) ((JComboBox<ActionTargetDataType>)components.get("datatype")).getSelectedItem();
				char header = ' ';
				
				String composed = "";
				
				for(ActionType t : ActionType.values())
					if(t == actionType)
					{
						header = t.getActionChar();
						break;
					}
				
				char type = ' ';
				
				for(ActionTargetDataType t : ActionTargetDataType.values())
					if(t == dataType)
					{
						type = t.getDataType();
						break;
					}
				
				String value = "";
				
				if(dataType == ActionTargetDataType.VECTOR)
				{
					value = ((JSpinner)components.get("vecx")).getValue() + "," + ((JSpinner)components.get("vecy")).getValue();
				}
				else value = ((JTextField)components.get("stringdata")).getText();
				
				currentAction = new Action(actionType, new ActionTarget(type + value));
				
				((JTextField)components.get("actionstring")).setText("" + header + type + value);
			}
		});
		
		genBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				OligarchyApplication.getCurrentApplication().getGUIManager().getDialogManager().showNameInput(InsertPanel.this::assembleTask);
			}
		});

		dataTargetContainer.add(actionContainer);
		dataTargetContainer.add(cboxContainer);
		dataTargetContainer.add(new JSeparator());
		dataTargetContainer.add(dataCards);
		dataTargetContainer.add(new JSeparator());
		dataTargetContainer.add(actionInsert);
		
		taskBuilderTab.add(new JSeparator());
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		
		JScrollPane dtcScroller = new JScrollPane(dataTargetContainer);
		dtcScroller.setMinimumSize(new Dimension(200,240));
		
		taskBuilderTab.add(dtcScroller, c);
		
		this.addTab("Task Builder", taskBuilderTab);
		
	}
	
	private void initListeners()
	{
		TaskHandler.log.addCommandListener(new CommandListener(){
			@Override
			public void read(String log, CommandType cmdType) 
			{
				JList<String> consoleList = (JList<String>) components.get("consoleList");
				((DefaultListModel<String>)consoleList.getModel()).addElement(cmdType.toString().toLowerCase() + ": " + log.toLowerCase());
			}
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.LOADED_APP_INPUT, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				String log = (String)((MMEventDataMap)event.getEventData()).getData("input").value;
				//System.err.println("ERROR >> " + log);
				JList<String> consoleList = (JList<String>) components.get("cconsoleList");
				((DefaultListModel<String>)consoleList.getModel()).addElement(log);
			}

			@Override
			public MMEventType getEventType() { return MMEventType.LOADED_APP_INPUT; }
			
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.LOADED_APP_ERROR, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				String log = (String)((MMEventDataMap)event.getEventData()).getData("input").value;
				System.err.println("ERROR >> " + log);
				//JList<String> consoleList = (JList<String>) components.get("cconsoleList");
				//((DefaultListModel<String>)consoleList.getModel()).addElement(log);
			}

			@Override
			public MMEventType getEventType() { return MMEventType.LOADED_APP_ERROR; }
			
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.HASHLOC_ADDED, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				JList<String> hashList = (JList<String>) components.get("hashList");
				HashedLocation hl = (HashedLocation)((MMEventDataMap)event.getEventData()).getData("hashedLocation").value;
				DefaultListModel<String> hashModel = ((DefaultListModel<String>)hashList.getModel());
				hashModel.addElement(""+hl.getHash()+": " + hl.getX() + "," + hl.getY());
			}

			@Override
			public MMEventType getEventType() { return MMEventType.HASHLOC_ADDED; }
			
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.CONNECTION_STARTED, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				JComboBox<String> connectionBox = (JComboBox<String>) components.get("connectionBox");
				ThreadedConnection tc = (ThreadedConnection)((MMEventDataMap)event.getEventData()).getData("connection").value;
				DefaultComboBoxModel<String> connectionModel = ((DefaultComboBoxModel<String>)connectionBox.getModel());
				connectionModel.addElement(""+tc.getId());
			}

			@Override
			public MMEventType getEventType() { return MMEventType.CONNECTION_STARTED; }
			
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.RCV_CHAT, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				JList<String> chatList = (JList<String>) components.get("chatList");
				ThreadedConnection tc = (ThreadedConnection)((MMEventDataMap)event.getEventData()).getData("connection").value;
				String msg = tc.getLastMessage();
				((DefaultListModel<String>)chatList.getModel()).addElement(msg);
			}

			@Override
			public MMEventType getEventType() { return MMEventType.RCV_CHAT; }
			
		});
		
		TaskHandler.getCurrentDispatcher().addEventListener(MMEventType.SENT_MSG, new MMEventListener(){

			@Override
			public void act(MMEvent event) {
				JList<String> chatList = (JList<String>) components.get("chatList");
				String msg = (String)((MMEventDataMap)event.getEventData()).getData("message").value;
				((DefaultListModel<String>)chatList.getModel()).addElement(msg);
			}

			@Override
			public MMEventType getEventType() { return MMEventType.SENT_MSG; }
			
		});
	}
	
	public Component getComponent(String componentName) { return components.getOrDefault(componentName, null); }
	
	
	private void assembleTask(Object nameObject)
	{
		String name = (String) nameObject;
		String abilityData = ((JTextField)components.get("reqab")).getText();
		String attributeData = ((JTextField)components.get("reqat")).getText();
		TaskRequirement tr = new TaskRequirement();
		tr.setAbilities(DataParser.parseIntegers(abilityData));
		tr.setAttributes(DataParser.parseIntegers(attributeData));
		
		OligarchyApplication.getCurrentApplication().getTaskBuilder().buildTask(name, 0, currentTaskActions, tr);
	}
}
