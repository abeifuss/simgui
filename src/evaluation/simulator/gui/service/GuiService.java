package evaluation.simulator.gui.service;

import javax.swing.SwingUtilities;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.layout.MainGui;
import evaluation.simulator.gui.layout.frames.ConsoleFrame;
import evaluation.simulator.gui.layout.frames.HelpFrame;
import evaluation.simulator.gui.layout.frames.ToolFrame;

public class GuiService {
	private static Logger logger = Logger.getLogger(GuiService.class);

	private static GuiService instance = null;

	public static GuiService getInstance() {
		if (instance == null) {
			instance = new GuiService();
		}
		return instance;
	}

	private final ToolFrame configToolFrame;
	private final HelpFrame helpToolFrame;
	private final MainGui mainGui;
	private final ConsoleFrame consoleFrame;

	private GuiService() {

		this.configToolFrame = ToolFrame.getInstance();
		this.helpToolFrame = HelpFrame.getInstance();
		this.mainGui = MainGui.getInstance();
		this.consoleFrame = ConsoleFrame.getInstance();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GuiService.this.loadOldWinConf();
			}
		});

	}

	public void loadOldWinConf() {
		boolean showConfToolInSeparateWindow = UserConfigService.getGUISERVICE_SEPERATE_CONF_TOOL();
		boolean showConfHelpInSeparateWindow = UserConfigService.getGUISERVICE_SEPERATE_HELP_TOOL();
		boolean showHomeTab = UserConfigService.getGUISERVICE_TOGGLE_HOME_TAB();
		//		boolean showConfConsoleInSeparateWindow = UserConfigService.getGUISERVICE_SEPERATE_CONSOLE();

		if (showConfToolInSeparateWindow) {
			logger.log(Level.DEBUG, "Seperate config tool");
			this.configToolFrame.initialize();
			this.configToolFrame.setVisible(true);
			this.mainGui.toogleConfTool(false);
		} else {
			logger.log(Level.DEBUG, "Integrate config tool");
			this.configToolFrame.setVisible(false);
			this.mainGui.toogleConfTool(true);
		}

		if (showConfHelpInSeparateWindow) {
			logger.log(Level.DEBUG, "Seperate help tool");
			this.helpToolFrame.setVisible(true);
			this.mainGui.toogleHelpTool(false);
		} else {
			logger.log(Level.DEBUG, "Integrate help tool");
			this.helpToolFrame.setVisible(false);
			this.mainGui.toogleHelpTool(true);
		}

		//		if (showConfConsoleInSeparateWindow) {
		//			logger.log(Level.DEBUG, "Seperate console");
		//			this.consoleFrame.setVisible(true);
		//			this.mainGui.toogleConsole(false);
		//		} else {
		//			logger.log(Level.DEBUG, "Integrate console");
		//			this.consoleFrame.setVisible(false);
		//			this.mainGui.toogleConsole(true);
		//		}

		this.consoleFrame.setVisible(true);

		if (showHomeTab) {
			logger.log(Level.DEBUG, "Show home tab");
			this.mainGui.toggleHomeTab(true);
		} else {
			logger.log(Level.DEBUG, "Hide home tab");
			this.mainGui.toggleHomeTab(false);
		}
	}

	public void toogleConfTools() {
		if (this.configToolFrame.isVisible()) {
			this.configToolFrame.setVisible(false);
			this.mainGui.toogleConfTool(true);
			UserConfigService.setGUISERVICE_SEPERATE_CONF_TOOL(false);
			return;
		} else{
			this.configToolFrame.initialize();
			this.configToolFrame.setVisible(true);
			this.mainGui.toogleConfTool(false);
			UserConfigService.setGUISERVICE_SEPERATE_CONF_TOOL(true);
		}
	}

	public void toogleHelpTools() {
		if (this.helpToolFrame.isVisible()) {
			this.helpToolFrame.setVisible(false);
			this.mainGui.toogleHelpTool(true);
			UserConfigService.setGUISERVICE_SEPERATE_HELP_TOOL(false);
			return;
		}
		this.helpToolFrame.initialize();
		this.helpToolFrame.setVisible(true);
		this.mainGui.toogleHelpTool(false);
		UserConfigService.setGUISERVICE_SEPERATE_HELP_TOOL(true);

	}

	//	public void toggleConsole() {
	//		if (this.consoleFrame.isVisible()) {
	//			this.consoleFrame.setVisible(false);
	//			this.mainGui.toogleConsole(true);
	//			UserConfigService.setGUISERVICE_SEPERATE_CONSOLE(false);
	//			return;
	//		} else {
	//			this.consoleFrame.setVisible(true);
	//			// FIXME is this the bug?
	//			//	this.consoleFrame.init();
	//			this.mainGui.toogleConsole(false);
	//			UserConfigService.setGUISERVICE_SEPERATE_CONSOLE(true);
	//		}
	//	}

	public void toggleHomeTab() {
		if (MainGui.getInstance().homeTabStatus) {
			this.mainGui.toggleHomeTab(false);
			UserConfigService.setGUISERVICE_TOGGLE_HOME_TAB(false);
			return;
		}
		this.mainGui.toggleHomeTab(true);
		UserConfigService.setGUISERVICE_TOGGLE_HOME_TAB(true);
	}
}