/**
 * 
 */
package framework.core.gui.services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import framework.core.gui.model.PlugInFilter;
import framework.core.gui.model.PlugInFilterModel;
import framework.core.gui.model.StatefulViewPlugIn;
import framework.core.gui.util.GUIText;

/**
 * This is the knowledge base for filters and is able to filter by a set of "value" has to be "key"
 * pairs.
 * 
 * @author Marius Fink
 */
public class CompositionFilterService {

	private List<ActionListener> actionListeners = new ArrayList<ActionListener>(1);
	private static final List<PlugInFilterModel> filters = new LinkedList<PlugInFilterModel>();

	/**
	 * This is the place to create filters.
	 */
	static {
		filters.add(new PlugInFilterModel(GUIText.getText("duplex") + " (" + GUIText.getText("client") + ")",
				"plugIn/plugInSettings/globalCapabilites/duplex/client", "TRUE", "FALSE"));
		filters.add(new PlugInFilterModel(GUIText.getText("duplex") + " (" + GUIText.getText("mix") + ")",
				"plugIn/plugInSettings/globalCapabilites/duplex/mix", "FALSE", "TRUE"));

		filters.add(new PlugInFilterModel(GUIText.getText("topology") + " (" + GUIText.getText("client") + ")",
				"plugIn/plugInSettings/globalCapabilites/topology/client", "FIXED_ROUTE", "FREE_ROUTE"));
		filters.add(new PlugInFilterModel(GUIText.getText("topology") + " (" + GUIText.getText("mix") + ")",
				"plugIn/plugInSettings/globalCapabilites/topology/mix", "FIXED_ROUTE", "FREE_ROUTE"));

	}

	private List<StatefulViewPlugIn> availablePlugIns;

	/**
	 * Creates a new filter service
	 * 
	 * @param availablePlugIns
	 *            the plugIns that will be set (remember to use the setter method after reload)
	 */
	public CompositionFilterService(List<StatefulViewPlugIn> availablePlugIns) {
		this.availablePlugIns = availablePlugIns;
	}

	/**
	 * Changes the states of all current available plug-ins. Sets filtered to true, if values don't
	 * match. Plug-ins that are not affected by any of the specified actual filters will be set to
	 * <code>filtered = false</code>
	 * 
	 * @param currentActualFilters
	 *            the filters to apply
	 */
	public void filter(List<PlugInFilter> currentActualFilters) {
		for (StatefulViewPlugIn plugIn : availablePlugIns) {
			plugIn.setFiltered(false); // first reactivate it
		}

		for (PlugInFilter filter : currentActualFilters) {
			for (StatefulViewPlugIn plugIn : availablePlugIns) {
				String is = plugIn.getResource().getPropertyAsString(filter.getModel().getPlugInLocation()).trim();
				String should = filter.getFilterValue().trim();
				if (!is.equalsIgnoreCase(should)) {
					plugIn.setFiltered(true);
				}
			}
		}
		
		for (StatefulViewPlugIn p: availablePlugIns) {
			p.finishedEditing();
		}
		
		notifyListeners();
	}

	public static List<PlugInFilterModel> getFilters() {
		return filters;
	}

	/**
	 * @param availablePlugIns
	 *            the availablePlugIns to set
	 */
	public void setAvailablePlugIns(List<StatefulViewPlugIn> availablePlugIns) {
		this.availablePlugIns = availablePlugIns;
	}

	/**
	 * Adds an ActionListener to this component.
	 * 
	 * @param al
	 *            the new ActionListener
	 */
	public void addActionListener(ActionListener al) {
		actionListeners.add(al);
	}

	/**
	 * Notifies all currently registered ActionListeners
	 */
	private void notifyListeners() {
		for (ActionListener al : actionListeners) {
			al.actionPerformed(new ActionEvent(this, 0, "State changed"));
		}
	}

}
