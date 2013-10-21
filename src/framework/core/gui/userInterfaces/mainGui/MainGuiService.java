package framework.core.gui.userInterfaces.mainGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import framework.core.gui.userInterfaces.configurationGui.ConfigurationGuiService;
import framework.core.gui.userInterfaces.pluginRegistryGui.ResourceRegistryGuiService;
import framework.core.gui.userInterfaces.toolSelectorGui.ToolSelectorGUIService;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.GuiNavigator;
import framework.core.gui.util.OpenWebsite;
import framework.core.gui.util.components.ConsoleGUI;
import framework.core.gui.util.components.PopUpPrompt;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * StartUpGUI is the general starter GUI which contains all the sub guis.
 * 
 * @author Marius Fink
 * @version 18.08.2012
 */
public class MainGuiService {
	private MainGui mainGui;

	/**
	 * Creates the MainGui and initializes it
	 * 
	 * @param params
	 */
	public MainGuiService() {
		mainGui = new MainGui();
		init();
		StatusBar.getInstance().showStatus(GUIText.getText("guiInitFinished"));
		mainGui.show();
	}

	/**
	 * Fills the GUI with tabs, creates the GUI services
	 */
	private void init() {

		// create services, add tabs
		ConfigurationGuiService configSvc = new ConfigurationGuiService();

		// SVC: ToolSelector
		ToolSelectorGUIService selectorSvc = new ToolSelectorGUIService();

		// SVC: Plug-in registry
		ResourceRegistryGuiService registrySvc = new ResourceRegistryGuiService();

		mainGui.getTabbedPane().addTab(GUIText.getText("toolSelectorGuiTitle"),
				ImageLoader.loadIcon("toolSelectorIcon", 48), selectorSvc.createPanel());
		mainGui.getTabbedPane().addTab(GUIText.getText("configurationGuiTitle"),
				ImageLoader.loadIcon("configIcon", 48), configSvc.createPanel());
		mainGui.getTabbedPane().addTab(GUIText.getText("pluginRegistryGuiTitle"),
				ImageLoader.loadIcon("pluginRegistryIcon", 48), registrySvc.createPanel());

		GuiNavigator.getInstance().setMainTabMenu(mainGui.getTabbedPane());
		GuiNavigator.getInstance().setPluginRegistryMainTabIndex(2);
		GuiNavigator.getInstance().setPluginRegistryService(registrySvc);

		mainGui.getMenu().addActionListener(createMenuActionListener());
	}

	/**
	 * Adds an individual Tab.
	 * 
	 * @param name
	 *            the name of the tab
	 * @param icon
	 *            the icon, can be null
	 * @param component
	 *            the component to display under this tab
	 */
	public void addIndividualTab(String name, ImageIcon icon, JComponent component) {
		mainGui.getTabbedPane().addTab(name, icon, component);
	}

	/**
	 * Creates the ActionListener that can handle the commands by the frame menu
	 * 
	 * @return the Actionlistener for the menu
	 */
	private ActionListener createMenuActionListener() {
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if (command.equalsIgnoreCase("exit")) {
					System.exit(0); // FIXME Find a better way to close the
					// programm!
				} else if (command.equalsIgnoreCase("licence")) {
					// TODO licences
				} else if (command.startsWith("open")) {
					String[] parts = command.split(" ");
					if (parts.length > 1) {
						try {
							OpenWebsite.open(parts[1]);
						} catch (Exception e1) {
							PopUpPrompt.displayGenericExceptionMessage(e1, GUIText.getText("cantLoadWebsiteShort"),
									GUIText.getText("cantLoadWebsite", parts[1]));
						}
					}
				} else if (command.equalsIgnoreCase("toggleconsole")) {
					ConsoleGUI c = ConsoleGUI.getInstance();
					if (c.isHidden()) {
						c.showConsole();
					}
				}
			}
		};
		return al;
	}
}
