package gui.layout;

import gui.console.ConsolePanel;
import gui.customElements.SimConfigPanel;
import gui.layout.frames.HelpFrame;
import gui.layout.menu.MainMenu;
import gui.service.GuiService;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import conf.service.UserConfigService;

@SuppressWarnings("serial")
public class MainGui extends JFrame {

	private JPanel _helpToolView;
	private JPanel _configToolView;
	private JTabbedPane _mainView;
	private JSplitPane _horrizontalSplitPlane;

	private static MainGui instance = null;
	private int _mainGuiXPos;
	private int _mainGuiYPos;
	private int _mainGuiWidth;
	private int _mainGuiHeight;

	public MainGui() {
		this.getContentPane().setLayout(new BorderLayout());
		
		this.init();
		
		this.setTitle("gMixSim");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("etc/img/icons/icon128.png"));

		SystemTray tray = SystemTray.getSystemTray();
		try {
			tray.add(new TrayIcon(Toolkit.getDefaultToolkit().createImage("etc/img/icons/icon16.png")));
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);
		
		try {
			_mainGuiXPos = UserConfigService.getInstance().getInteger("MAINGUI_XPOS");
		} catch (Exception e) {
			_mainGuiXPos = 100;
		}
		
		try {
			_mainGuiYPos = UserConfigService.getInstance().getInteger("MAINGUI_YPOS");
		} catch (Exception e) {
			_mainGuiYPos = 100;
		}
		
		try {
			_mainGuiWidth = UserConfigService.getInstance().getInteger("MAINGUI_WIDTH");
		} catch (Exception e) {
			_mainGuiWidth = 500;
		}
		
		try {
			_mainGuiHeight = UserConfigService.getInstance().getInteger("MAINGUI_HEIGTH");
		} catch (Exception e) {
			_mainGuiHeight = 750;
		}

		this.setBounds(_mainGuiXPos, _mainGuiYPos, _mainGuiWidth, _mainGuiHeight);
		
		this.addWindowListener( new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				safeProperties();
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				safeProperties();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				safeProperties();
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
	}

	public static MainGui getInstance() {
		if (instance == null) {
			instance = new MainGui();
		}
		return instance;
	}

	protected void init() {

		_horrizontalSplitPlane = new JSplitPane();
		_horrizontalSplitPlane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		_horrizontalSplitPlane.setOneTouchExpandable(true);
		_horrizontalSplitPlane.setDividerLocation(350);

		JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);
		verticalSplitPlane.setDividerLocation(400);
		
		_configToolView = SimConfigPanel.getInstance();
		_helpToolView = HelpFrame.getInstance().getPanel();

		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.add(verticalSplitPlane, BorderLayout.CENTER);
		
		_mainView = new JTabbedPane();
		_mainView.addTab("Home", new HomeTab());
		_mainView.addTab("Simulator", new SimulationTab());
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(ConsolePanel.getInstance(), BorderLayout.CENTER);

		_horrizontalSplitPlane.setLeftComponent(_configToolView);
		_horrizontalSplitPlane.setRightComponent(right);
		
		verticalSplitPlane.setTopComponent(_mainView);
		verticalSplitPlane.setBottomComponent(bottom);
		
		this.getContentPane().add(_horrizontalSplitPlane, BorderLayout.CENTER);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
		setupMenu();
	}

	private void setupMenu() {
		this.setJMenuBar( MainMenu.getInstance() );
	}
	
	// (De)seperate the configuration tool
	public void toogleConfTool( boolean b ){
		if ( b ){
			_configToolView = SimConfigPanel.getInstance();
			_horrizontalSplitPlane.setLeftComponent(_configToolView);
		}else{
			_horrizontalSplitPlane.remove(_configToolView);
		}
	}
	
	// (De)seperate the help tool
	public void toogleHelpTool( boolean b ){
		if ( b ){
			_helpToolView = HelpFrame.getInstance().getPanel();
			_mainView.addTab("Tutorial", _helpToolView);
		}else{
			_mainView.remove(_helpToolView);
		}
	}
	
	private void safeProperties(){
		UserConfigService.getInstance().setInteger("MAINGUI_XPOS", this.getX());
		UserConfigService.getInstance().setInteger("MAINGUI_YPOS", this.getY());
		UserConfigService.getInstance().setInteger("MAINGUI_WIDTH", this.getWidth());
		UserConfigService.getInstance().setInteger("MAINGUI_HEIGTH", this.getHeight());
	}
}
