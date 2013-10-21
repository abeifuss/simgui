package framework.core.gui.userInterfaces.configurationGui.compositionBranch;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import framework.core.gui.model.PlugInChangedActionListener;
import framework.core.gui.model.PlugInType;
import framework.core.gui.model.StatefulViewPlugIn;
import framework.core.gui.model.XMLAttribute;
import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.services.AvailableResourcesDAO;
import framework.core.gui.services.CompositionDependencyService;
import framework.core.gui.services.CompositionFilterService;
import framework.core.gui.services.PlugInCompositionDependencyDropDownCreator;
import framework.core.gui.services.PlugInConverter;
import framework.core.gui.userInterfaces.configurationGui.compositionBranch.plugInComboBox.PlugInDropDownGuiService;
import framework.core.gui.util.GUIText;

/**
 * DependencyBranchPanel is a Panel in the configuration GUI that is able to compose the registered
 * plug-ins and save this composition as an xml node. Because of its special features this component
 * is hard coded.
 * 
 * @author Marius Fink
 * @version 18.08.2012
 */
public class CompositionBranchGuiService implements XMLPartGUIContainer, ActionListener {

	private CompositionBranchGui gui;
	private PlugInCompositionDependencyDropDownCreator dependencyManager;
	private JPanel wrapperPanel = new JPanel(new BorderLayout());
	
	/**
	 * Builds the gui initially
	 */
	public CompositionBranchGuiService() {
		
		refresh();
	}

	/**
	 * Refreshes the gui by building it with the new values from the AvailableResourcesDAO.
	 */
	private void refresh() {
		List<StatefulViewPlugIn> currentAvailablePlugIns = new PlugInConverter()
				.convertToStateful(new AvailableResourcesDAO().getAvailablePlugIns());

		// important: those are working on the same objects - otherwise filters won't work
		CompositionFilterService filterService = new CompositionFilterService(currentAvailablePlugIns);
		CompositionFilterGuiService filterGuiService = new CompositionFilterGuiService(filterService);
		dependencyManager = new PlugInCompositionDependencyDropDownCreator(currentAvailablePlugIns);
		final CompositionDependencyService dependencyService = new CompositionDependencyService(currentAvailablePlugIns);
		
		filterService.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				redrawAllDropDowns();
			}
		});
		dependencyManager.setPlugInChangedActionListener(new PlugInChangedActionListener() {
			@Override
			public void actionPerformed(StatefulViewPlugIn arg0) {
				dependencyService.checkDependencies(arg0);
				redrawAllDropDowns();
			}
		}); 
		
		gui = new CompositionBranchGui(filterGuiService.getComponent());
		//gui.removeAllRows();

		for (int i = 1; i <= 5; i++) {
			PlugInDropDownGuiService clientSvc = dependencyManager.getDropDownGuiService(i, PlugInType.CLIENT);
			PlugInDropDownGuiService mixSvc = dependencyManager.getDropDownGuiService(i, PlugInType.MIX);

			gui.addRow(clientSvc.getPanel(), mixSvc.getPanel());
		}
		
		//initial
		filterGuiService.filter();

		gui.getRefreshButton().removeActionListener(this);
		gui.getRefreshButton().addActionListener(this);
		redrawAllDropDowns();
		gui.drawRows();
		
		wrapperPanel.removeAll();
		wrapperPanel.add(gui.getJPanel(), BorderLayout.CENTER);
	}

	private void redrawAllDropDowns() {
		for (int i = 1; i <= 5; i++) {
			dependencyManager.getDropDownGuiService(i, PlugInType.CLIENT).updateUI();
			dependencyManager.getDropDownGuiService(i, PlugInType.MIX).updateUI();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see framework.core.gui.model.XMLPartGUIContainer#getCurrentXMLParts()
	 */
	@Override
	public Set<XMLPart> getCurrentXMLParts() {
		// These settings will be stored at:
		// gMixConfiguration/composition/[layerXname]/[mix|client]/plugIn[@id and @src]

		Set<XMLPart> parts = new HashSet<XMLPart>();

		for (int i = 1; i <= 5; i++) {
			PlugInDropDownGuiService clientSvc = dependencyManager.getDropDownGuiService(i, PlugInType.CLIENT);
			parts.addAll(convertToXmlPart(clientSvc.getCurrentlyChosenPlugIn()));

			PlugInDropDownGuiService mixSvc = dependencyManager.getDropDownGuiService(i, PlugInType.MIX);
			parts.addAll(convertToXmlPart(mixSvc.getCurrentlyChosenPlugIn()));
		}

		return parts;
	}

	/**
	 * Converts the StatefulViewPlugIn into two XMLAttributes (source, id) also handles null values!
	 * 
	 * @param currentlyChosenPlugIn
	 *            the plug-in to convert or null.
	 * @return the two parts that describe a plugin in the config as composition: source and id
	 */
	private Collection<? extends XMLPart> convertToXmlPart(StatefulViewPlugIn currentlyChosenPlugIn) {
		Set<XMLPart> parts = new HashSet<XMLPart>();
		if (currentlyChosenPlugIn == null) {
			return parts;
		} else {
			String type = currentlyChosenPlugIn.getPluginType().name().toLowerCase();
			String location = "/gMixConfiguration/composition/layer" + currentlyChosenPlugIn.getLayer() + "/" + type
					+ "/plugIn";

			XMLAttribute src = new XMLAttribute();
			src.setLocation(location);
			src.setAttributeName("source");
			src.setValue(currentlyChosenPlugIn.getHome() + "/" + GUIText.getText("plugInSettingsXml"));
			parts.add(src);

			XMLAttribute idAttr = new XMLAttribute();
			idAttr.setLocation(location);
			idAttr.setAttributeName("id");
			idAttr.setValue(currentlyChosenPlugIn.getId());
			parts.add(idAttr);

			return parts;
		}
	}

	@Override
	public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed) {
		Map<Integer, Map<PlugInType, String>> map = filterPlugInIds(toBeDisplayed);
		for (Entry<Integer, Map<PlugInType, String>> l1 : map.entrySet()) {
			for (Entry<PlugInType, String> l2 : l1.getValue().entrySet()) {
				dependencyManager.getDropDownGuiService(l1.getKey(), l2.getKey()).choosePlugin(l2.getValue());
			}
		}
	}

	/**
	 * Helper method for loading the beans by xml parts
	 * 
	 * @param toFilter
	 *            find the corresponding parts in this collection
	 * @return a categorized map with all relevant values
	 */
	private Map<Integer, Map<PlugInType, String>> filterPlugInIds(Collection<XMLPart> toFilter) {
		Map<Integer, Map<PlugInType, String>> map = new HashMap<Integer, Map<PlugInType, String>>();

		for (int i = 1; i <= 5; i++) {
			for (PlugInType t : PlugInType.values()) {
				for (XMLPart p : toFilter) {
					if (p instanceof XMLAttribute) {
						if (p.getLocation().equals(
								"/gMixConfiguration/composition/layer" + i + "/" + t.name().toLowerCase() + "/plugIn")) {
							XMLAttribute attr = (XMLAttribute) p;
							if (attr.getAttributeName().equals("id")) {
								Map<PlugInType, String> subMap = map.get(i);
								if (subMap == null) {
									subMap = new HashMap<PlugInType, String>();
									map.put(i, subMap);
								}
								subMap.put(t, attr.getValue().toString());
							}
						}
					}
				}
			}
		}

		return map;
	}

	/**
	 * @return the component
	 */
	public JComponent getJPanel() {
		return wrapperPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		refresh();
	}

}
