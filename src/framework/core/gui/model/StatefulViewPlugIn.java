package framework.core.gui.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a wrapper extending the PlugIn with the filtered and illegal flags.
 * 
 * @author Marius Fink
 * 
 */
public class StatefulViewPlugIn extends PlugInBean {

	private boolean filtered;
	private boolean illegal;
	private boolean sourceIllegal;
	private List<ActionListener> actionListeners = new ArrayList<ActionListener>(10);

	/**
	 * @see framework.core.gui.model.PlugInBean
	 * @param descriptor
	 *            the XML Resource
	 */
	public StatefulViewPlugIn(XMLResource descriptor) {
		super(descriptor);
	}

	/**
	 * Creates a new StatefulViewPlugIn from a given PlugIn
	 * 
	 * @param plugin
	 *            the plugin to extend
	 */
	public StatefulViewPlugIn(PlugInBean plugin) {
		super(plugin);
	}

	/**
	 * @return true if a filter disables this this plug in
	 */
	public boolean isFiltered() {
		return filtered;
	}

	/**
	 * @param filtered
	 *            Is this plugin disabled by a filter?
	 */
	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	/**
	 * @return Is this framework illegal because of compatibility?
	 */
	public boolean isIllegal() {
		return illegal;
	}

	/**
	 * @param illegal
	 *            Is this framework illegal because of compatibility?
	 */
	public void setIllegal(boolean illegal) {
		this.illegal = illegal;
	}

	/**
	 * 
	 * @param al
	 */
	public void addActionListener(ActionListener al) {
		actionListeners.add(al);
	}

	private void notifyListeners() {
		for (ActionListener al : actionListeners) {
			al.actionPerformed(new ActionEvent(this, layer, "State changed"));
		}
	}

	/**
	 * @return the sourceIllegal
	 */
	public boolean isSourceIllegal() {
		return sourceIllegal;
	}

	/**
	 * @param sourceIllegal
	 *            the sourceIllegal to set
	 */
	public void setSourceIllegal(boolean sourceIllegal) {
		this.sourceIllegal = sourceIllegal;
	}

	/**
	 * Call this method after setting all the values to the new state. This notifies all listeners
	 * and prevents the GUI from redrawing while plug-in state is not final.
	 */
	public void finishedEditing() {
		notifyListeners();
	}
}
