package gui.service;

import gui.layout.MainGui;
import gui.layout.frames.HelpFrame;
import gui.layout.frames.ToolFrame;
import conf.service.UserConfigService;


public class GuiService {

	private MainGui _mainGui;
	private ToolFrame _configToolFrame;
	private HelpFrame _helpToolFrame;

	private static GuiService instance = null;
	
	private GuiService() {
		
			_mainGui = MainGui.getInstance();
			_configToolFrame = ToolFrame.getInstance();
			_helpToolFrame = HelpFrame.getInstance();
			
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
			_configToolFrame.setVisible(true);
			_mainGui.toogleConfTool(false);
		}else{
			System.out.println("SHOW INTEGRATED CONF TOOL");
			_configToolFrame.setVisible(false);
			_mainGui.toogleConfTool(true);
		}
		
		if ( showConfHelpInSeparateWindow ){
			System.out.println("SHOW SEPERATE HELP TOOL");
			_helpToolFrame.setVisible(true);
			_mainGui.toogleHelpTool(false);
		}else{
			System.out.println("SHOW INTEGRATED HELP TOOL");
			_helpToolFrame.setVisible(false);
			_mainGui.toogleHelpTool(true);
		}
	}
	
	public void toogleConfTools(){
		if ( _configToolFrame.isVisible() ){
			_configToolFrame.setVisible(false);
			_mainGui.toogleConfTool(true);
			UserConfigService.getInstance().setBool("SEPERATE_CONF_TOOL", false);
			return;
		}
		_configToolFrame.setVisible(true);
		_configToolFrame.init();
		_mainGui.toogleConfTool(false);
		UserConfigService.getInstance().setBool("SEPERATE_CONF_TOOL", true);
	}

	public void toogleHelpTools() {
		if ( _helpToolFrame.isVisible() ){
			_helpToolFrame.setVisible(false);
			_mainGui.toogleHelpTool(true);
			UserConfigService.getInstance().setBool("SEPERATE_HELP_TOOL", false);
			return;
		}
		_helpToolFrame.setVisible(true);
		_helpToolFrame.init();
		_mainGui.toogleHelpTool(false);
		UserConfigService.getInstance().setBool("SEPERATE_HELP_TOOL", true);
		
	}
}