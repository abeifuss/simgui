package evaluation.simulator.gui.layout;

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

	private JPanel _configToolView;
	private JPanel _helpToolView;

	private JSplitPane _horrizontalSplitPlane;
	private int _mainGuiHeight;
	private int _mainGuiWidth;
	private int _mainGuiXPos;
	private int _mainGuiYPos;

	private JTabbedPane _mainView;

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
			this._mainGuiXPos = UserConfigService.getInstance().getInteger(
					"MAINGUI_XPOS");
		} catch (Exception e) {
			this._mainGuiXPos = 100;
		}

		try {
			this._mainGuiYPos = UserConfigService.getInstance().getInteger(
					"MAINGUI_YPOS");
		} catch (Exception e) {
			this._mainGuiYPos = 100;
		}

		try {
			this._mainGuiWidth = UserConfigService.getInstance().getInteger(
					"MAINGUI_WIDTH");
		} catch (Exception e) {
			this._mainGuiWidth = 500;
		}

		try {
			this._mainGuiHeight = UserConfigService.getInstance().getInteger(
					"MAINGUI_HEIGTH");
		} catch (Exception e) {
			this._mainGuiHeight = 750;
		}

		this.setBounds(this._mainGuiXPos, this._mainGuiYPos,
				this._mainGuiWidth, this._mainGuiHeight);

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

		this._horrizontalSplitPlane = new JSplitPane();
		this._horrizontalSplitPlane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this._horrizontalSplitPlane.setOneTouchExpandable(true);
		this._horrizontalSplitPlane.setDividerLocation(350);

		JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);
		verticalSplitPlane.setDividerLocation(400);

		this._configToolView = SimConfigPanel.getInstance();
		this._helpToolView = HelpFrame.getInstance().getPanel();

		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.add(verticalSplitPlane, BorderLayout.CENTER);

		this._mainView = new JTabbedPane();
		this._mainView.addTab("Home", new HomeTab());
		this._mainView.addTab("Simulator", new SimulationTab());

		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(ConsolePanel.getInstance(), BorderLayout.CENTER);

		this._horrizontalSplitPlane.setLeftComponent(this._configToolView);
		this._horrizontalSplitPlane.setRightComponent(right);

		verticalSplitPlane.setTopComponent(this._mainView);
		verticalSplitPlane.setBottomComponent(bottom);

		this.getContentPane().add(this._horrizontalSplitPlane,
				BorderLayout.CENTER);

		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		this.setupMenu();
	}

	private void safeProperties() {
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
			this._configToolView = SimConfigPanel.getInstance();
			this._horrizontalSplitPlane.setLeftComponent(this._configToolView);
		} else {
			this._horrizontalSplitPlane.remove(this._configToolView);
		}
	}

	// (De)seperate the help tool
	public void toogleHelpTool(boolean b) {
		if (b) {
			this._helpToolView = HelpFrame.getInstance().getPanel();
			this._mainView.addTab("Tutorial", this._helpToolView);
		} else {
			this._mainView.remove(this._helpToolView);
		}
	}
}
