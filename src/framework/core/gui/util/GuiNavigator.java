package framework.core.gui.util;

import javax.swing.JTabbedPane;

import framework.core.gui.model.PlugInBean;
import framework.core.gui.userInterfaces.pluginRegistryGui.ResourceRegistryGuiService;

public class GuiNavigator {

	private static GuiNavigator instance;
	private JTabbedPane mainTab;
	private int pluginRegistryMainTabIndex;
	private ResourceRegistryGuiService pluginRegistryService;
	
	
	private GuiNavigator() {
		
	}
	
	public static GuiNavigator getInstance() {
		if (instance == null) {
			instance = new GuiNavigator();
		}
		return instance;
	}
	
	public void navigateToPluginSettings(PlugInBean plugIn) {
		if (mainTab != null) {
			mainTab.setSelectedIndex(pluginRegistryMainTabIndex);
			pluginRegistryService.showPlugIn(plugIn);
		}
		
		
	}
	
	public void setMainTabMenu(JTabbedPane mainTab) {
		this.mainTab = mainTab;
	}
	
	public void setPluginRegistryMainTabIndex(int index) {
		this.pluginRegistryMainTabIndex = index;
	}

	public void setPluginRegistryService(ResourceRegistryGuiService registrySvc) {
		this.pluginRegistryService = registrySvc;
	}
	

}
