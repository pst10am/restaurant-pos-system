package app_admin;

import javax.swing.JPanel;

public interface IxPanel {
	public JPanel getPanel();
	public boolean isDataValid();
	public Object getObject();
	public void setObject(Object obj);
}
