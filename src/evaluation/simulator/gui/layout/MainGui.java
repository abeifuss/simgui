package evaluation.simulator.gui.layout;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.customElements.SimConfigPanel;
import evaluation.simulator.gui.layout.frames.ConsoleFrame;
import evaluation.simulator.gui.layout.frames.HelpFrame;
import evaluation.simulator.gui.layout.menu.MainMenu;

@SuppressWarnings("serial")
public class MainGui extends JFrame {

	private static MainGui instance = null;

	public static MainGui getInstance() {
		if (instance == null) {
			instance = new MainGui();
		}
		return instance;
	}

	private JPanel configToolView;
	private int consoleHeight;

	private JPanel helpToolView;
	private JPanel consoleView;
	private HomeTab homeTab;
	private boolean homeTabStatus;
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
		this.getContentPane().setLayout(new BorderLayout());
		this.init();

		this.setTitle("gMixSim");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(
				"etc/img/icons/icon128.png"));

		SystemTray tray = SystemTray.getSystemTray();
		try {
			tray.add(new TrayIcon(Toolkit.getDefaultToolkit().createImage(
					"etc/img/icons/icon16.png")));
		} catch (AWTException e) {
			e.printStackTrace();
		}

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);


		
		this.mainGuiXPos = UserConfigService.getMAINGUI_XPOS();
		this.mainGuiYPos =  UserConfigService.getMAINGUI_YPOS();
		this.mainGuiWidth =  UserConfigService.getMAINGUI_WIDTH();
		this.mainGuiHeight =  UserConfigService.getMAINGUI_HEIGHT();
		this.horizontalSplitPlaneDeviderLocation = UserConfigService.getMAINGUI_HSPLIT_DEVIDER_LOCATION();
		this.consoleHeight = UserConfigService.getMAINGUI_CONSOLE_HEIGHT();
	

		this.setBounds(this.mainGuiXPos, this.mainGuiYPos, this.mainGuiWidth,
				this.mainGuiHeight);

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

	public boolean homeTabIsShown() {
		return this.homeTabStatus;
	}

	protected void init() {

		this.horizontalSplitPlane = new JSplitPane();
		this.horizontalSplitPlane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.horizontalSplitPlane.setOneTouchExpandable(true);

		this.verticalSplitPlane = new JSplitPane();
		this.verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.verticalSplitPlane.setOneTouchExpandable(true);

		this.configToolView = SimConfigPanel.getInstance();
		this.helpToolView = HelpFrame.getInstance().getPanel();
		this.consoleView = ConsoleFrame.getInstance().getPanel();

		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.add(this.verticalSplitPlane, BorderLayout.CENTER);

		this.mainView = new JTabbedPane();
		this.homeTab = new HomeTab();
		this.mainView.addTab("Home", this.homeTab);
		this.homeTabStatus = true;
		this.simulationTab = new SimulationTab();
		this.mainView.addTab("Simulator", this.simulationTab);

		this.horizontalSplitPlane.setLeftComponent(this.configToolView);
		this.horizontalSplitPlane.setRightComponent(right);

		this.verticalSplitPlane.setTopComponent(this.mainView);
		this.verticalSplitPlane.setBottomComponent(this.bottom);

		this.getContentPane().add(this.horizontalSplitPlane,
				BorderLayout.CENTER);

		this.horizontalSplitPlane.setVisible(true);
		this.verticalSplitPlane.setVisible(true);

		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		this.setupMenu();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// adjusts the horizontal divider to automatically resize when
				// resizing the window
				MainGui.this.horizontalSplitPlane.setResizeWeight(0.5);
				MainGui.this.verticalSplitPlane.setResizeWeight(0.5);
			}
		});
		this.pack();

		this.horizontalSplitPlane.addPropertyChangeListener(
				JSplitPane.DIVIDER_LOCATION_PROPERTY,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						MainGui.this.horizontalSplitPlaneDeviderLocation = MainGui.this.horizontalSplitPlane
								.getDividerLocation();
					}
				});

		this.verticalSplitPlane.addPropertyChangeListener(
				JSplitPane.DIVIDER_LOCATION_PROPERTY,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						MainGui.this.consoleHeight = MainGui.this.verticalSplitPlane
								.getSize().height
								- MainGui.this.verticalSplitPlane
								.getDividerLocation();
					}
				});

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				MainGui.this.horizontalSplitPlane
				.setDividerLocation(MainGui.this.horizontalSplitPlaneDeviderLocation);
				MainGui.this.verticalSplitPlane
				.setDividerLocation(MainGui.this.verticalSplitPlane
						.getSize().height - MainGui.this.consoleHeight);
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				MainGui.this.horizontalSplitPlane
				.setDividerLocation(MainGui.this.horizontalSplitPlaneDeviderLocation);
				MainGui.this.verticalSplitPlane
				.setDividerLocation(MainGui.this.verticalSplitPlane
						.getSize().height - MainGui.this.consoleHeight);
			}
		});
	}

	private void safeProperties() {
		UserConfigService.setMAINGUI_CONSOLE_HEIGHT(this.consoleHeight);
		UserConfigService.setMAINGUI_HEIGHT(this.getHeight());
		UserConfigService.setMAINGUI_HSPLIT_DEVIDER_LOCATION(this.horizontalSplitPlaneDeviderLocation);
		UserConfigService.setMAINGUI_WIDTH(this.getWidth());
		UserConfigService.setMAINGUI_XPOS(this.getX());
		UserConfigService.setMAINGUI_YPOS(this.getY());
	}

	private void setupMenu() {
		this.setJMenuBar(MainMenu.getInstance());
	}

	// Close/Open the home frame
	public void toggleHomeTab(boolean b) {
		if (b) {
			this.mainView.addTab("Home", this.homeTab);
			this.homeTabStatus = true;
		} else {
			this.mainView.remove(this.homeTab);
			this.homeTabStatus = false;
		}
	}

	// (De)seperate the configuration tool
	public void toogleConfTool(boolean b) {
		if (b) {
			this.configToolView = SimConfigPanel.getInstance();
			this.horizontalSplitPlane.setLeftComponent(this.configToolView);
		} else {
			this.horizontalSplitPlane.remove(this.configToolView);
		}
	}

	// (De)seperate the help tool
	public void toogleHelpTool(boolean b) {
		if (b) {
			this.helpToolView = HelpFrame.getInstance().getPanel();
			this.mainView.addTab("Tutorial", this.helpToolView);
		} else {
			this.mainView.remove(this.helpToolView);
		}
	}

	public void toogleConsole(boolean b) {
		if (b) {
			this.consoleView = ConsoleFrame.getInstance().getPanel();
			this.bottom = new JPanel();
			this.bottom.setLayout(new BorderLayout());
			this.bottom.add(this.consoleView, BorderLayout.CENTER);
			this.verticalSplitPlane.setBottomComponent(this.bottom);
			this.verticalSplitPlane.repaint();
		} else {
			this.verticalSplitPlane.remove(this.bottom);
		}
	}

	public void update() {
		this.simulationTab.update();
	}

}
