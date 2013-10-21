/**
 * 
 */
package framework.core.gui.model;

/**
 * ActionListener for changing plug-ins in a drop down menu.
 * 
 * @author Marius Fink
 */
public interface PlugInChangedActionListener {

	/**
	 * Fires the event
	 * 
	 * @param nowSelectedPlugin
	 *            the currentyl selected plug-in
	 */
	public void actionPerformed(StatefulViewPlugIn nowSelectedPlugin);
}
