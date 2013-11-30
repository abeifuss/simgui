package evaluation.simulator.gui.layout;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.layout.frames.HelpFrame;
import evaluation.simulator.gui.layout.frames.ToolFrame;
import evaluation.simulator.gui.service.GuiService;

@SuppressWarnings("serial")
public class MainGui extends JFrame {

	private static MainGui instance = null;

	public static MainGui getInstance() {
		if (instance == null) {
			instance = new MainGui();
		}
		return instance;
	}

	/*
	 * neue Elemente:
	 */
	private JFrame frame;
	public JSplitPane splitPane;
	public JTabbedPane tabbedPane;
	private HelpFrame helpFrame;

	/*
	 * *********************************************************
	 */

	private int consoleHeight;

	private JPanel consoleView;
	private HomeTab homeTab;
	public boolean homeTabStatus;
	private JSplitPane horizontalSplitPlane;
	private int horizontalSplitPlaneDeviderLocation;

	private int mainGuiHeight;
	private int mainGuiWidth;
	private int mainGuiXPos;
	private int mainGuiYPos;

	private JTabbedPane mainView;
	private SimulationTab simulationTab;
	private JSplitPane verticalSplitPlane;
	private JPanel bottom;

	public MainGui() {
		//		this.getContentPane().setLayout(new BorderLayout());
		//		this.init();
		this.initialize();

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				MainGui.this.safeProperties();
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				MainGui.this.safeProperties();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				MainGui.this.safeProperties();
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		});
	}

	//	public boolean homeTabIsShown() {
	//		return this.homeTabStatus;
	//	}

	private void initialize() {

		this.mainGuiXPos = UserConfigService.getMAINGUI_XPOS();
		this.mainGuiYPos =  UserConfigService.getMAINGUI_YPOS();
		this.mainGuiWidth =  UserConfigService.getMAINGUI_WIDTH();
		this.mainGuiHeight =  UserConfigService.getMAINGUI_HEIGHT();
		this.horizontalSplitPlaneDeviderLocation = UserConfigService.getMAINGUI_HSPLIT_DEVIDER_LOCATION();
		this.consoleHeight = UserConfigService.getMAINGUI_CONSOLE_HEIGHT();

		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 1024, 768);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.splitPane = new JSplitPane();
		this.frame.getContentPane().add(this.splitPane, BorderLayout.CENTER);

		this.splitPane.setLeftComponent(ToolFrame.getInstance().getPanel());

		this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.homeTab = new HomeTab();
		//		this.simulationTab = new SimulationTab();
		this.helpFrame = HelpFrame.getInstance();
		this.tabbedPane.addTab("Simulator", this.simulationTab);
		this.splitPane.setRightComponent(this.tabbedPane);

		JMenuBar menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmShowhideHome = new JMenuItem("Show/Hide Home");
		mnWindow.add(mntmShowhideHome);

		JMenuItem mntmShowhideHelp = new JMenuItem("Show/Hide Help");
		mntmShowhideHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiService.getInstance().toogleHelpTools();
			}
		});
		mnWindow.add(mntmShowhideHelp);

		JMenuItem mntmSeperateConfiguration = new JMenuItem("De-/Seperate Configuration");
		mntmSeperateConfiguration.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiService.getInstance().toogleConfTools();
			}
		});
		mnWindow.add(mntmSeperateConfiguration);

		this.frame.setTitle("gMixSim");
		this.frame.setIconImage(Toolkit.getDefaultToolkit().createImage(
				"etc/img/icons/icon128.png"));

		SystemTray tray = SystemTray.getSystemTray();
		try {
			tray.add(new TrayIcon(Toolkit.getDefaultToolkit().createImage(
					"etc/img/icons/icon16.png")));
		} catch (AWTException e) {
			e.printStackTrace();
		}

		this.setBounds(this.mainGuiXPos, this.mainGuiYPos, this.mainGuiWidth,
				this.mainGuiHeight);

		this.frame.setVisible(true);
	}

	private void safeProperties() {
		UserConfigService.setMAINGUI_CONSOLE_HEIGHT(this.consoleHeight);
		UserConfigService.setMAINGUI_HEIGHT(this.getHeight());
		UserConfigService.setMAINGUI_HSPLIT_DEVIDER_LOCATION(this.horizontalSplitPlaneDeviderLocation);
		UserConfigService.setMAINGUI_WIDTH(this.getWidth());
		UserConfigService.setMAINGUI_XPOS(this.getX());
		UserConfigService.setMAINGUI_YPOS(this.getY());
	}

	//	private void setupMenu() {
	//		this.setJMenuBar(MainMenu.getInstance());
	//	}

	// Close/Open the home frame
	public void toggleHomeTab(boolean b) {
		if (b) {
			this.tabbedPane.addTab("Home", this.homeTab);
			this.homeTabStatus = true;
		} else {
			this.tabbedPane.remove(this.homeTab);
			this.homeTabStatus = false;
		}
	}

	// (De)seperate the configuration tool
	public void toogleConfTool(boolean b) {
		if (b) {
			this.splitPane.setLeftComponent(ToolFrame.getInstance().getPanel());
		} else {
			this.splitPane.remove(ToolFrame.getInstance().getPanel());
		}
	}

	// (De)seperate the help tool
	public void toogleHelpTool(boolean b) {
		if (b) {
			this.tabbedPane.addTab("Help", this.helpFrame.getPanel());
		} else {
			this.tabbedPane.remove(this.helpFrame.getPanel());
		}
	}

	//	public void toogleConsole(boolean b) {
	//		if (b) {
	//			this.consoleView = ConsoleFrame.getInstance().getPanel();
	//			this.bottom = new JPanel();
	//			this.bottom.setLayout(new BorderLayout());
	//			this.bottom.add(this.consoleView, BorderLayout.CENTER);
	//			this.verticalSplitPlane.setBottomComponent(this.bottom);
	//			this.verticalSplitPlane.repaint();
	//		} else {
	//			this.verticalSplitPlane.remove(this.bottom);
	//		}
	//	}

	public void update() {
		this.simulationTab.update();
	}

}
