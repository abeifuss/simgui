package framework.core.gui.userInterfaces.pluginRegistryGui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import framework.core.config.Paths;
import framework.core.gui.model.PlugInBean;
import framework.core.gui.model.ResourceType;
import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLResource;
import framework.core.gui.model.XMLResourceContainer;
import framework.core.gui.services.AvailableResourcesDAO;
import framework.core.gui.services.PlugInDirScanService;
import framework.core.gui.services.ResourceIOManagementService;
import framework.core.gui.services.StaticFunctionDirScanService;
import framework.core.gui.userInterfaces.mainGui.StatusBar;
import framework.core.gui.userInterfaces.pluginRegistryGui.plugIn.PlugInRegistrySettingsGuiService;
import framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel.RepertoryPanelGuiService;
import framework.core.gui.userInterfaces.pluginRegistryGui.staticFunction.StaticFunctionRegistrySettingsGuiService;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.PopUpPrompt;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * PluginRegistryGuiService is the service that holds the complete plugin registry GUI with main
 * branch and resource management panels.
 * 
 * @author Marius Fink
 * @version 17.11.2012
 */
public class ResourceRegistryGuiService {

	private ResourceRegistryGui gui;
	private ResourceIOManagementService resourceIOManagementService;
	private AvailableResourcesDAO resourceDao;
	private RepertoryPanelGuiService<XMLResourceContainer> lastTreeService;
	private RegistryGuiService lastGuiService;

	/**
	 * Constructor of PluginRegistryGuiService
	 */
	public ResourceRegistryGuiService() {
		gui = new ResourceRegistryGui();
		resourceIOManagementService = new ResourceIOManagementService();
		resourceDao = new AvailableResourcesDAO();

		init();
	}

	/**
	 * Initializes this component, sets all action listeners
	 */
	private void init() {
		final RepertoryPanelGuiService<XMLResourceContainer> plugInRepertoryPanelSvc = new RepertoryPanelGuiService<XMLResourceContainer>(
				GUIText.getText("availablePlugins"), ImageLoader.loadIcon("plugin"), new PlugInDirScanService());
		plugInRepertoryPanelSvc.getAddButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (resourceIOManagementService.createNewDescriptor(ResourceType.PLUGIN)) {
					plugInRepertoryPanelSvc.refreshValues();
				}
			}
		});
		plugInRepertoryPanelSvc.getDeleteButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XMLResourceContainer selectedItem = plugInRepertoryPanelSvc.getSelectedItem();
				try {
					if (resourceIOManagementService.deleteResource(selectedItem)) {
						resourceDao.unregisterResourceById(selectedItem.getId());
						plugInRepertoryPanelSvc.refreshValues();
					}
				} catch (Exception e1) {
					PopUpPrompt.displayGenericExceptionMessage(e1, GUIText.getText("unableToUnregister"), GUIText
							.getText("unableToUnregisterDetail", selectedItem.getName(),
									Paths.AVAILABLE_RESOURCES_XML_FILE_PATH));
					StatusBar.getInstance().showStatus(GUIText.getText("unableToUnregister"));
				}
			}
		});
		plugInRepertoryPanelSvc.getRefreshButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				plugInRepertoryPanelSvc.refreshValues();
			}
		});
		plugInRepertoryPanelSvc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				XMLResourceContainer selectedItem = plugInRepertoryPanelSvc.getSelectedItem();
				if (selectedItem != null) {
					refresh(selectedItem);
					PlugInRegistrySettingsGuiService pluginGuiService = new PlugInRegistrySettingsGuiService(
							selectedItem);
					lastGuiService = pluginGuiService;
					changeGui(pluginGuiService, selectedItem);
				} else {
					lastGuiService = null;
					gui.removeMainContent();
				}
				changeRepertory(plugInRepertoryPanelSvc);
			}
		});
		gui.addRepertoryPanel(plugInRepertoryPanelSvc.getRepertoryPanel());

		final RepertoryPanelGuiService<XMLResourceContainer> staticFunctionRepertoryPanelSvc = new RepertoryPanelGuiService<XMLResourceContainer>(
				GUIText.getText("availableStaticFunctions"), ImageLoader.loadIcon("function"),
				new StaticFunctionDirScanService());
		gui.addRepertoryPanel(staticFunctionRepertoryPanelSvc.getRepertoryPanel());

		staticFunctionRepertoryPanelSvc.getAddButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (resourceIOManagementService.createNewDescriptor(ResourceType.STATIC_FUNCTION)) {
					staticFunctionRepertoryPanelSvc.refreshValues();
				}
			}
		});
		staticFunctionRepertoryPanelSvc.getDeleteButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				XMLResourceContainer selectedItem = staticFunctionRepertoryPanelSvc.getSelectedItem();
				try {
					if (resourceIOManagementService.deleteResource(selectedItem)) {
						resourceDao.unregisterResourceById(selectedItem.getId());
						staticFunctionRepertoryPanelSvc.refreshValues();
					}
				} catch (Exception e) {
					PopUpPrompt.displayGenericExceptionMessage(e, GUIText.getText("unableToUnregister"), GUIText
							.getText("unableToUnregisterDetail", selectedItem.getName(),
									Paths.AVAILABLE_RESOURCES_XML_FILE_PATH));
				}
			}
		});
		staticFunctionRepertoryPanelSvc.getRefreshButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				staticFunctionRepertoryPanelSvc.refreshValues();
			}
		});

		staticFunctionRepertoryPanelSvc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				XMLResourceContainer selectedItem = staticFunctionRepertoryPanelSvc.getSelectedItem();

				if (selectedItem != null) {
					refresh(selectedItem);
					StaticFunctionRegistrySettingsGuiService guiSvc = new StaticFunctionRegistrySettingsGuiService(
							selectedItem);
					lastGuiService = guiSvc;
					changeGui(guiSvc, selectedItem);
				} else {
					lastGuiService = null;
					gui.removeMainContent();
				}
				changeRepertory(staticFunctionRepertoryPanelSvc);
			}

		});

		gui.removeMainContent(); // initially removes the content
	}

	/**
	 * Reloads the selected item
	 * 
	 * @param selectedItem
	 */
	private void refresh(XMLResourceContainer selectedItem) {
		if (selectedItem != null) {
			String appendix = "";
			switch (selectedItem.getType()) {
			case PLUGIN:
				appendix = GUIText.getText("plugInSettingsXml");
				break;
			case STATIC_FUNCTION:
				appendix = GUIText.getText("staticFunctionSettingsXml");
				break;
			}
			selectedItem.setResource(new XMLResource(selectedItem.getHome() + "/" + appendix));
		}
	}

	/**
	 * Changes the current repertory ONLY IF the user changed the active one.
	 * 
	 * @param currentService
	 *            the currently active service
	 */
	private void changeRepertory(RepertoryPanelGuiService<XMLResourceContainer> currentService) {
		if (lastTreeService != null) {
			if (!lastTreeService.equals(currentService)) {
				lastTreeService.clearSelection();
				if (lastGuiService == null) {
					gui.removeMainContent();
				}
			}
		}
		lastTreeService = currentService;
	}

	/**
	 * Replaces the GUI
	 * 
	 * @param newGuiService
	 *            the GUI service to replace
	 * @param selectedItem
	 *            th item to display
	 */
	private void changeGui(RegistryGuiService newGuiService, XMLResourceContainer selectedItem) {

		// saveLastItem(selectedItem); This caused a lot of bugs. Workaround:
		// Don't switch without saving the current state
		List<XMLPart> tobeDisplayed = selectedItem.getResource().getAllXMLParts();
		newGuiService.replaceDisplayedValuesWith(tobeDisplayed);
		gui.setMainContent(newGuiService.getPanel());
	}

	/**
	 * Creates the panel
	 * 
	 * @return the plugin management gui panel
	 */
	public Component createPanel() {
		return gui.getComponent();
	}

	/**
	 * Opens immediately the given plugin, saves the existing one
	 * 
	 * @param plugIn
	 *            the plugin to navigate to
	 */
	public void showPlugIn(PlugInBean plugIn) {
		XMLResourceContainer c = new XMLResourceContainer();
		c.setName(plugIn.getName());
		c.setResource(plugIn.getResource());
		c.setType(ResourceType.PLUGIN);
		c.setHome(plugIn.getResource().getRawAttributeValue("/plugIn", "home"));

		refresh(c);
		PlugInRegistrySettingsGuiService pluginGuiService = new PlugInRegistrySettingsGuiService(c);
		changeGui(pluginGuiService, c);
		lastGuiService = pluginGuiService;
	}

}
