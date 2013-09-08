package gui.layout;

import gui.console.ConsolePanel;
import gui.customElements.SimConfigPanel;
import gui.layout.Frames.HelpFrame;
import gui.layout.menu.MainMenu;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class MainGui extends JFrame {

	JPanel _helpToolView;
	JPanel _configToolView;
	JTabbedPane _mainView;
	JSplitPane _horrizontalSplitPlane;

	private static MainGui instance = null;

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
		this.setBounds(360, 10, 800, 600);
		this.setVisible(true);
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
}
