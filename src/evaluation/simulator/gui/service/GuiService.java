package evaluation.simulator.gui.service;

import javax.swing.SwingUtilities;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.layout.MainGui;
import evaluation.simulator.gui.layout.frames.HelpFrame;
import evaluation.simulator.gui.layout.frames.ToolFrame;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class GuiService {

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

	private GuiService() {

		this.configToolFrame = ToolFrame.getInstance();
		this.helpToolFrame = HelpFrame.getInstance();
		this.mainGui = MainGui.getInstance();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loadOldWinConf();
			}
		});

	}

	public void loadOldWinConf() {
		boolean showConfToolInSeparateWindow = UserConfigService.getInstance()
				.getBool("SEPERATE_CONF_TOOL");
		boolean showConfHelpInSeparateWindow = UserConfigService.getInstance()
				.getBool("SEPERATE_HELP_TOOL");
		boolean showHomeTab = UserConfigService.getInstance().getBool(
				"TOGGLE_HOME_TAB");

		if (showConfToolInSeparateWindow) {
			Logger.Log(LogLevel.DEBUG, "Seperate config tool");
			this.configToolFrame.setVisible(true);
			this.mainGui.toogleConfTool(false);
		} else {
			Logger.Log(LogLevel.DEBUG, "Integrate config tool");
			this.configToolFrame.setVisible(false);
			this.mainGui.toogleConfTool(true);
		}

		if (showConfHelpInSeparateWindow) {
			Logger.Log(LogLevel.DEBUG, "Seperate help tool");
			this.helpToolFrame.setVisible(true);
			this.mainGui.toogleHelpTool(false);
		} else {
			Logger.Log(LogLevel.DEBUG, "Integrate help tool");
			this.helpToolFrame.setVisible(false);
			this.mainGui.toogleHelpTool(true);
		}

		if (showHomeTab) {
			Logger.Log(LogLevel.DEBUG, "Show home tab");
			this.mainGui.toggleHomeTab(true);
		} else {
			Logger.Log(LogLevel.DEBUG, "Hide home tab");
			this.mainGui.toggleHomeTab(false);
		}
	}

	public void toogleConfTools() {
		if (this.configToolFrame.isVisible()) {
			this.configToolFrame.setVisible(false);
			this.mainGui.toogleConfTool(true);
			UserConfigService.getInstance()
					.setBool("SEPERATE_CONF_TOOL", false);
			return;
		}
		this.configToolFrame.setVisible(true);
		this.configToolFrame.init();
		this.mainGui.toogleConfTool(false);
		UserConfigService.getInstance().setBool("SEPERATE_CONF_TOOL", true);
	}

	public void toogleHelpTools() {
		if (this.helpToolFrame.isVisible()) {
			this.helpToolFrame.setVisible(false);
			this.mainGui.toogleHelpTool(true);
			UserConfigService.getInstance()
					.setBool("SEPERATE_HELP_TOOL", false);
			return;
		}
		this.helpToolFrame.setVisible(true);
		this.helpToolFrame.init();
		this.mainGui.toogleHelpTool(false);
		UserConfigService.getInstance().setBool("SEPERATE_HELP_TOOL", true);

	}

	public void toggleHomeTab() {

		if (MainGui.getInstance().homeTabIsShown()) {
			this.mainGui.toggleHomeTab(false);
			UserConfigService.getInstance().setBool("TOGGLE_HOME_TAB", false);
			return;
		}
		this.mainGui.toggleHomeTab(true);
		UserConfigService.getInstance().setBool("TOGGLE_HOME_TAB", true);

	}
}