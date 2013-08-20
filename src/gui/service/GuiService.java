package gui.service;

import conf.service.UserConfigService;
import gui.layout.MainGui;
import gui.layout.Frames.HelpFrame;
import gui.layout.Frames.ToolFrame;


public class GuiService {

	private MainGui mainGui;
	private ToolFrame toolFrame;
	private HelpFrame helpFrame;

	private static GuiService instance = null;
	
	private GuiService() {
		
			mainGui = MainGui.getInstance();
			toolFrame = ToolFrame.getInstance();
			helpFrame = HelpFrame.getInstance();
			loadOldWinConf();
	}

	public static GuiService getInstance() {
		if (instance == null) {
			instance = new GuiService();
		}
		return instance;
	}
	
	public void loadOldWinConf(){
		boolean showConfToolInSeparateWindow = UserConfigService.getInstance().getBool("SEPERATE_CONF_TOOL");
		boolean showConfHelpInSeparateWindow = UserConfigService.getInstance().getBool("SEPERATE_HELP_TOOL");
		
		
		if ( showConfToolInSeparateWindow ){
			System.out.println("SHOW SEPERATE CONF TOOL");
			toolFrame.setVisible(true);
			mainGui.toogleConfTool(false);
		}else{
			System.out.println("SHOW INTEGRATED CONF TOOL");
			toolFrame.setVisible(false);
			mainGui.toogleConfTool(true);
		}
		
		if ( showConfHelpInSeparateWindow ){
			System.out.println("SHOW SEPERATE HELP TOOL");
			helpFrame.setVisible(true);
			mainGui.toogleHelpTool(false);
		}else{
			System.out.println("SHOW INTEGRATED HELP TOOL");
			helpFrame.setVisible(false);
			mainGui.toogleHelpTool(true);
		}
	}
	
	public void toogleConfTools(){
		if ( toolFrame.isVisible() ){
			toolFrame.setVisible(false);
			mainGui.toogleConfTool(true);
			UserConfigService.getInstance().setBool("SEPERATE_CONF_TOOL", false);
			return;
		}
		toolFrame.setVisible(true);
		toolFrame.init();
		mainGui.toogleConfTool(false);
		UserConfigService.getInstance().setBool("SEPERATE_CONF_TOOL", true);
	}

	public void toogleHelpTools() {
		if ( helpFrame.isVisible() ){
			helpFrame.setVisible(false);
			mainGui.toogleHelpTool(true);
			UserConfigService.getInstance().setBool("SEPERATE_HELP_TOOL", false);
			return;
		}
		helpFrame.setVisible(true);
		helpFrame.init();
		mainGui.toogleHelpTool(false);
		UserConfigService.getInstance().setBool("SEPERATE_HELP_TOOL", true);
		
	}
}