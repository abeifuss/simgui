package evaluation.simulator.gui.service;

import javax.swing.SwingUtilities;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.layout.MainGui;
import evaluation.simulator.gui.layout.frames.HelpFrame;
import evaluation.simulator.gui.layout.frames.ToolFrame;

/**
 * Implemented as a singleton
 * 
 * @author alex
 *
 */
public class GuiService {
	private static Logger logger = Logger.getLogger(GuiService.class);

	private static GuiService instance = null;

	/**
	 * Singleton
	 * 
	 * @return an instance of {@link GuiService}
	 */
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
			@Override
			public void run() {
				GuiService.this.loadOldWinConf();
			}
		});

	}

	/**
	 * Loads the user window configuration
	 * 
	 */
	public void loadOldWinConf() {
		boolean showConfToolInSeparateWindow = UserConfigService.getGUISERVICE_SEPERATE_CONF_TOOL();
		boolean showConfHelpInSeparateWindow = UserConfigService.getGUISERVICE_SEPERATE_HELP_TOOL();
		boolean showHomeTab = UserConfigService.getGUISERVICE_TOGGLE_HOME_TAB();
		//		boolean showConfConsoleInSeparateWindow = UserConfigService.getGUISERVICE_SEPERATE_CONSOLE();

		if (showConfToolInSeparateWindow) {
			logger.log(Level.DEBUG, "Seperate config tool");
			this.configToolFrame.initialize();
			this.configToolFrame.setVisible(true);
			this.getMainGui().toogleConfTool(false);
		} else {
			logger.log(Level.DEBUG, "Integrate config tool");
			this.configToolFrame.setVisible(false);
			this.getMainGui().toogleConfTool(true);
		}

		if (showConfHelpInSeparateWindow) {
			logger.log(Level.DEBUG, "Seperate help tool");
			this.helpToolFrame.setVisible(true);
			this.getMainGui().toogleHelpTool(false);
		} else {
			logger.log(Level.DEBUG, "Integrate help tool");
			this.helpToolFrame.setVisible(false);
			this.getMainGui().toogleHelpTool(true);
		}

	}

	/**
	 * Toggle the display mode of the config tool
	 */
	public void toogleConfTools() {
		if (this.configToolFrame.isVisible()) {
			this.configToolFrame.setVisible(false);
			this.getMainGui().toogleConfTool(true);
			UserConfigService.setGUISERVICE_SEPERATE_CONF_TOOL(false);
			return;
		} else{
			this.configToolFrame.initialize();
			this.configToolFrame.setVisible(true);
			this.getMainGui().toogleConfTool(false);
			UserConfigService.setGUISERVICE_SEPERATE_CONF_TOOL(true);
		}
	}

	/**
	 * Toggle the display mode of the help tool
	 */
	public void toogleHelpTools() {
		if (this.helpToolFrame.isVisible()) {
			this.helpToolFrame.setVisible(false);
			this.getMainGui().toogleHelpTool(true);
			UserConfigService.setGUISERVICE_SEPERATE_HELP_TOOL(false);
			return;
		}
		this.helpToolFrame.initialize();
		this.helpToolFrame.setVisible(true);
		this.getMainGui().toogleHelpTool(false);
		UserConfigService.setGUISERVICE_SEPERATE_HELP_TOOL(true);

	}

	public MainGui getMainGui() {
		return mainGui;
	}

}