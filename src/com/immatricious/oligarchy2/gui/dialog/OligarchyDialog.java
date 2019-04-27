package com.immatricious.oligarchy2.gui.dialog;

import javax.swing.JDialog;

public abstract class OligarchyDialog {

	private DialogCallback currentCallback;
	
	protected JDialog dialogBox;
	
	public OligarchyDialog(JDialog d)
	{
		this.dialogBox = d;
	}
	
	public void setCurrentCallback(DialogCallback cb) { this.currentCallback = cb; }
	protected void triggerCallback(OligarchyDialog d) { currentCallback.act(d); }
	
	public void show(DialogCallback cb)
	{
		dialogBox.setVisible(true);
		setCurrentCallback(cb);
	}
}
