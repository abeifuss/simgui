package gui.layout.menu;

import gui.service.GuiService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MainMenu extends JMenuBar{

	private static MainMenu _instance = null;
	
	private JMenu _fileMenu;
	private JMenu _windowMenu;
	private JMenu _helpMenu;
	
	private JMenuItem _open;
	private JMenuItem _close;
	private JMenuItem _preferences;
	private JMenuItem _seperateConfigTool;
	private JMenuItem _seperateHelpTool;
	private JMenuItem _faq;
	private JMenuItem _about;
	
	public MainMenu() {

		_fileMenu = new JMenu("File");
		_windowMenu = new JMenu("Window");
		_helpMenu = new JMenu("Help");

		_open = new JMenuItem("Open");
		_close = new JMenuItem("Exit");
		
		_preferences = new JMenuItem("Preferences");
		_seperateConfigTool = new JMenuItem("Separate configuration tool");
		_seperateHelpTool = new JMenuItem("Separate help tool");
		
		_faq = new JMenuItem("F.A.Q.");
		_about = new JMenuItem("About");

		_fileMenu.add(_open);
		_fileMenu.addSeparator();
		_fileMenu.add(_close);
		
		_windowMenu.add(_preferences);
		_windowMenu.add(_seperateConfigTool);
		_windowMenu.add(_seperateHelpTool);
		
		_helpMenu.add(_faq);
		_helpMenu.add(_about);

		add(_fileMenu);
		add(_windowMenu);
		add(_helpMenu);
		
		// action listeners 
		_seperateConfigTool.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						GuiService.getInstance().toogleConfTools();
					}
				}
		);
		_seperateHelpTool.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						GuiService.getInstance().toogleHelpTools();
					}
				}
		);
	}
	
	public static MainMenu getInstance() {
		if (_instance == null) {
			_instance = new MainMenu();
		}
		return _instance;
	}

}
