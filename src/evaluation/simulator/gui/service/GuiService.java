package evaluation.simulator.gui.service;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.layout.MainGui;
import evaluation.simulator.gui.layout.frames.HelpFrame;
import evaluation.simulator.gui.layout.frames.ToolFrame;

public class GuiService {

	private static GuiService instance = null;

	public static GuiService getInstance() {
		if (instance == null) {
			instance = new GuiService();
		}
		return instance;
	}

	private final ToolFrame _configToolFrame;

	private final HelpFrame _helpToolFrame;

	private final MainGui _mainGui;

	private GuiService() {

		this._mainGui = MainGui.getInstance();
		this._configToolFrame = ToolFrame.getInstance();
		this._helpToolFrame = HelpFrame.getInstance();

		this.loadOldWinConf();

		// JFrame jf = new TutorialPlayer("TEST");
		// jf.setVisible(true);
		// jf.setBounds(400, 400, 640, 480);
	}

	public void loadOldWinConf() {
		boolean showConfToolInSeparateWindow = UserConfigService.getInstance()
				.getBool("SEPERATE_CONF_TOOL");
		boolean showConfHelpInSeparateWindow = UserConfigService.getInstance()
				.getBool("SEPERATE_HELP_TOOL");
		boolean showHomeTab = UserConfigService.getInstance()
				.getBool("TOGGLE_HOME_TAB");

		if (showConfToolInSeparateWindow) {
			System.out.println("SHOW SEPERATE CONF TOOL");
			this._configToolFrame.setVisible(true);
			this._mainGui.toogleConfTool(false);
		} else {
			System.out.println("SHOW INTEGRATED CONF TOOL");
			this._configToolFrame.setVisible(false);
			this._mainGui.toogleConfTool(true);
		}

		if (showConfHelpInSeparateWindow) {
			System.out.println("SHOW SEPERATE HELP TOOL");
			this._helpToolFrame.setVisible(true);
			this._mainGui.toogleHelpTool(false);
		} else {
			System.out.println("SHOW INTEGRATED HELP TOOL");
			this._helpToolFrame.setVisible(false);
			this._mainGui.toogleHelpTool(true);
		}
		
		if (showHomeTab) {
			System.out.println("SHOW HOME TAB");
			this._mainGui.toggleHomeTab(true);
		} else {
			System.out.println("HIDE HOME TAB");
			this._mainGui.toggleHomeTab(false);
		}
	}

	public void toogleConfTools() {
		if (this._configToolFrame.isVisible()) {
			this._configToolFrame.setVisible(false);
			this._mainGui.toogleConfTool(true);
			UserConfigService.getInstance()
					.setBool("SEPERATE_CONF_TOOL", false);
			return;
		}
		this._configToolFrame.setVisible(true);
		this._configToolFrame.init();
		this._mainGui.toogleConfTool(false);
		UserConfigService.getInstance().setBool("SEPERATE_CONF_TOOL", true);
	}

	public void toogleHelpTools() {
		if (this._helpToolFrame.isVisible()) {
			this._helpToolFrame.setVisible(false);
			this._mainGui.toogleHelpTool(true);
			UserConfigService.getInstance()
					.setBool("SEPERATE_HELP_TOOL", false);
			return;
		}
		this._helpToolFrame.setVisible(true);
		this._helpToolFrame.init();
		this._mainGui.toogleHelpTool(false);
		UserConfigService.getInstance().setBool("SEPERATE_HELP_TOOL", true);

	}
	
	public void toggleHomeTab() {
		
		if (MainGui.getInstance().hTisShown()) {			
			this._mainGui.toggleHomeTab(false);
			UserConfigService.getInstance()
					.setBool("TOGGLE_HOME_TAB", false);
			return;
		}		
		this._mainGui.toggleHomeTab(true);
		UserConfigService.getInstance().setBool("TOGGLE_HOME_TAB", true);		
		
	}
}