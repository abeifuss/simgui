package framework.core.gui.services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import framework.core.gui.model.PlugInChangedActionListener;
import framework.core.gui.model.PlugInType;
import framework.core.gui.model.StatefulViewPlugIn;
import framework.core.gui.userInterfaces.configurationGui.compositionBranch.plugInComboBox.PlugInDropDownGuiService;
import framework.core.gui.util.GUIText;

/**
 * This manager handles the beans for the composition branch by invoking the matching mechanism
 * 
 * @author Marius Fink
 */
public class PlugInCompositionDependencyDropDownCreator {
	private List<StatefulViewPlugIn> availablePlugIns;
	private Map<Integer, Map<PlugInType, PlugInDropDownGuiService>> dropDownGuiServices;
	private PlugInChangedActionListener plugInChangedActionListener;

	/**
	 * Constructor with the list of all available plug ins
	 * 
	 * @param availablePlugIns
	 *            the available plugins
	 */
	public PlugInCompositionDependencyDropDownCreator(List<StatefulViewPlugIn> availablePlugIns) {
		this.availablePlugIns = availablePlugIns;

		dropDownGuiServices = new HashMap<Integer, Map<PlugInType, PlugInDropDownGuiService>>();

		for (int i = 1; i <= 5; i++) {
			Map<PlugInType, PlugInDropDownGuiService> map = new HashMap<PlugInType, PlugInDropDownGuiService>();
			map.put(PlugInType.CLIENT, createDropDownGuiServiceFor(i, PlugInType.CLIENT));
			map.put(PlugInType.MIX, createDropDownGuiServiceFor(i, PlugInType.MIX));

			dropDownGuiServices.put(i, map);
		}

	}

	/**
	 * Creates the corresponding PlugInDropDownGuiService adds the listener to recalculate the
	 * dependnecies by invoking the Matching Mechanism
	 * 
	 * @param layer
	 *            the layer for this service
	 * @param type
	 *            the type (client/mix) for this service
	 * @return the corresponding PlugInDropDownGuiService
	 */
	private PlugInDropDownGuiService createDropDownGuiServiceFor(int layer, PlugInType type) {
		String text = GUIText.getText("mix");
		if (type == PlugInType.CLIENT) {
			text = GUIText.getText("client");
		}

		final PlugInDropDownGuiService svc = new PlugInDropDownGuiService(text, getAvailablePluginsFor(layer, type));

		svc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				plugInChangedActionListener.actionPerformed(svc.getCurrentlyChosenPlugIn());
			}
		});

		return svc;
	}

	/**
	 * @param plugInChangedActionListener2
	 *            the Actionlistener that is fired whenever the user changes a plug-in.
	 */
	public void setPlugInChangedActionListener(PlugInChangedActionListener plugInChangedActionListener2) {
		plugInChangedActionListener = plugInChangedActionListener2;
	}

	/**
	 * @param layer
	 *            the layer
	 * @param type
	 *            the type (client/mix) of the service
	 * @return the corresponding PlugInDropDownGuiService
	 */
	public PlugInDropDownGuiService getDropDownGuiService(int layer, PlugInType type) {
		assert layer >= 1 && layer <= 5 : "Precondition violated: layer >= 1 && layer <= 5";
		assert type != null : "Precondition violated: type != null";

		return dropDownGuiServices.get(layer).get(type);
	}

	/**
	 * Filteres the list by the given restrictions.
	 * 
	 * @param layer
	 *            the layer
	 * @param type
	 *            the type (client/mix) of the valid available plugins
	 * @return the filtered list
	 */
	private List<StatefulViewPlugIn> getAvailablePluginsFor(int layer, PlugInType type) {
		List<StatefulViewPlugIn> matches = new LinkedList<StatefulViewPlugIn>();
		for (StatefulViewPlugIn p : availablePlugIns) {
			if (p.getLayer() == layer && p.getPluginType().equals(type)) {
				matches.add(p);
			}
		}
		return matches;
	}

}
