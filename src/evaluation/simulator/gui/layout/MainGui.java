package evaluation.simulator.gui.layout;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import evaluation.simulator.gui.console.ConsolePanel;
import evaluation.simulator.gui.customElements.SimConfigPanel;
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
	private JPanel helpToolView;

	private JSplitPane horizontalSplitPlane;
	private int mainGuiHeight;
	private int mainGuiWidth;
	private int mainGuiXPos;
	private int mainGuiYPos;
	private int consoleHeight;
	private int horizontalSplitPlaneDeviderLocation;

	private JTabbedPane mainView;
	private HomeTab homeTab;
	private boolean homeTabStatus;

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

		try {
			this.mainGuiXPos = UserConfigService.getInstance().getInteger(
					"MAINGUI_XPOS");
		} catch (Exception e) {
			this.mainGuiXPos = 100;
		}

		try {
			this.mainGuiYPos = UserConfigService.getInstance().getInteger(
					"MAINGUI_YPOS");
		} catch (Exception e) {
			this.mainGuiYPos = 100;
		}

		try {
			this.mainGuiWidth = UserConfigService.getInstance().getInteger(
					"MAINGUI_WIDTH");
		} catch (Exception e) {
			this.mainGuiWidth = 500;
		}

		try {
			this.mainGuiHeight = UserConfigService.getInstance().getInteger(
					"MAINGUI_HEIGTH");
		} catch (Exception e) {
			this.mainGuiHeight = 750;
		}

		try {
			this.horizontalSplitPlaneDeviderLocation = UserConfigService
					.getInstance().getInteger("HSPLIT_DEVIDER_LOCATION");
		} catch (Exception e) {
			this.horizontalSplitPlaneDeviderLocation = 150;
		}

		try {
			this.consoleHeight = UserConfigService.getInstance().getInteger(
					"CONSOLE_HEIGHT");
		} catch (Exception e) {
			this.consoleHeight = 650;
		}

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

	protected void init() {

		this.horizontalSplitPlane = new JSplitPane();
		this.horizontalSplitPlane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.horizontalSplitPlane.setOneTouchExpandable(true);

		final JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);

		this.configToolView = SimConfigPanel.getInstance();
		this.helpToolView = HelpFrame.getInstance().getPanel();

		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.add(verticalSplitPlane, BorderLayout.CENTER);

		this.mainView = new JTabbedPane();
		homeTab = new HomeTab();
		this.mainView.addTab("Home", homeTab);
		this.homeTabStatus = true;
		this.mainView.addTab("Simulator", new SimulationTab());

		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(ConsolePanel.getInstance(), BorderLayout.CENTER);

		this.horizontalSplitPlane.setLeftComponent(this.configToolView);
		this.horizontalSplitPlane.setRightComponent(right);

		verticalSplitPlane.setTopComponent(this.mainView);
		verticalSplitPlane.setBottomComponent(bottom);

		this.getContentPane().add(this.horizontalSplitPlane,
				BorderLayout.CENTER);

		this.horizontalSplitPlane.setVisible(true);
		verticalSplitPlane.setVisible(true);

		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		this.setupMenu();

		pack();

		this.horizontalSplitPlane.addPropertyChangeListener(
				JSplitPane.DIVIDER_LOCATION_PROPERTY,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						horizontalSplitPlaneDeviderLocation = horizontalSplitPlane
								.getDividerLocation();
					}
				});

		verticalSplitPlane.addPropertyChangeListener(
				JSplitPane.DIVIDER_LOCATION_PROPERTY,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						consoleHeight = verticalSplitPlane.getSize().height
								- verticalSplitPlane.getDividerLocation();
					}
				});

		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent arg0) {
				horizontalSplitPlane
						.setDividerLocation(horizontalSplitPlaneDeviderLocation);
				verticalSplitPlane.setDividerLocation(verticalSplitPlane
						.getSize().height - consoleHeight);
			}

			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				horizontalSplitPlane
						.setDividerLocation(horizontalSplitPlaneDeviderLocation);
				verticalSplitPlane.setDividerLocation(verticalSplitPlane
						.getSize().height - consoleHeight);
			}
		});
	}

	private void safeProperties() {
		UserConfigService.getInstance().setInteger("CONSOLE_HEIGHT",
				this.consoleHeight);
		UserConfigService.getInstance().setInteger("HSPLIT_DEVIDER_LOCATION",
				this.horizontalSplitPlaneDeviderLocation);
		UserConfigService.getInstance().setInteger("MAINGUI_XPOS", this.getX());
		UserConfigService.getInstance().setInteger("MAINGUI_YPOS", this.getY());
		UserConfigService.getInstance().setInteger("MAINGUI_WIDTH",
				this.getWidth());
		UserConfigService.getInstance().setInteger("MAINGUI_HEIGTH",
				this.getHeight());
	}

	private void setupMenu() {
		this.setJMenuBar(MainMenu.getInstance());
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

	// Close/Open the home frame
	public void toggleHomeTab(boolean b) {
		if (b) {
			this.mainView.addTab("Home", homeTab);
			this.homeTabStatus = true;
		} else {
			this.mainView.remove(homeTab);
			this.homeTabStatus = false;
		}
	}

	public boolean hTisShown() {
		return this.homeTabStatus;
	}

}
