package gui.layout;

import gui.console.ConsolePanel;
import gui.customElements.SimConfigPanel;
import gui.customElements.SimHelpContentPanel;
import gui.customElements.accordion.Accordion;
import gui.layout.Frames.HelpFrame;
import gui.pluginRegistry.SimPropRegistry;
import gui.results.LineJFreeChartCreator;
import gui.service.GuiService;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.jfree.chart.ChartPanel;

@SuppressWarnings("serial")
public class MainGui extends JFrame {

	// SimPropRegistry gcr = SimPropRegistry.getInstance();

	// Accordion accordian; // not needed
	JPanel helpPanel;
	//SimHelpContentPanel contentPanel;
	//SimHelpContentPanel menuPanel;
	
	JPanel left;
	JTabbedPane top;
	JSplitPane horrizontalSplitPlane;

	private static MainGui instance = null;

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

		horrizontalSplitPlane = new JSplitPane();
		horrizontalSplitPlane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		horrizontalSplitPlane.setOneTouchExpandable(true);
		horrizontalSplitPlane.setDividerLocation(350);

		JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);
		verticalSplitPlane.setDividerLocation(400);

//		JPanel buttonBar = new JPanel();
//		
//		buttonBar.add(new JButton("Load"), BorderLayout.SOUTH);
//		buttonBar.add(new JButton("Save"), BorderLayout.SOUTH);
//		buttonBar.add(new JButton("Reset"), BorderLayout.SOUTH);
		
		left = SimConfigPanel.getInstance();
		helpPanel = HelpFrame.getInstance().getPanel();

		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.add(verticalSplitPlane, BorderLayout.CENTER);
		
		top = new JTabbedPane();
		top.addTab("Home", new HomeTab());
		top.addTab("Simulator", new SimulationTab());
		//top.addTab("Results", new ChartPanel(LineJFreeChartCreator.createAChart()));
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(ConsolePanel.getInstance(), BorderLayout.CENTER);

		horrizontalSplitPlane.setLeftComponent(left);
		horrizontalSplitPlane.setRightComponent(right);
		
		verticalSplitPlane.setTopComponent(top);
		verticalSplitPlane.setBottomComponent(bottom);
		
		this.getContentPane().add(horrizontalSplitPlane, BorderLayout.CENTER);
		
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

		JMenuBar menu;

		JMenu fileMenu;
		JMenu windowMenu;
		JMenu helpMenu;
		JMenuItem open;
		JMenuItem close;
		JMenuItem preferences;
		JMenuItem configTool;
		JMenuItem helpTool;
		JMenuItem faq;
		JMenuItem about;

		menu = new JMenuBar();

		fileMenu = new JMenu("File");
		windowMenu = new JMenu("Window");
		helpMenu = new JMenu("Help");

		open = new JMenuItem("open");
		close = new JMenuItem("exit");
		
		preferences = new JMenuItem("Preferences");
		configTool = new JMenuItem("Separate configuration tool");
		helpTool = new JMenuItem("Separate help tool");
		
		faq = new JMenuItem("F.A.Q.");
		about = new JMenuItem("About");

		fileMenu.add(open);
		fileMenu.addSeparator();
		fileMenu.add(close);
		
		windowMenu.add(preferences);
		windowMenu.add(configTool);
		windowMenu.add(helpTool);
		
		helpMenu.add(faq);
		helpMenu.add(about);

		menu.add(fileMenu);
		menu.add(windowMenu);
		menu.add(helpMenu);
		
		// action listeners 
		configTool.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						GuiService.getInstance().toogleConfTools();
					}
				}
		);
		
		helpTool.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						GuiService.getInstance().toogleHelpTools();
					}
				}
		);
		
		this.setJMenuBar(menu);
	}

	// TODO: Don't know what this is...
	public void btnAction() {
		System.out.println("pressed");
	}
	
	// (De)seperate the configuration tool
	public void toogleConfTool( boolean b ){
		if ( b ){
			left = SimConfigPanel.getInstance();
			horrizontalSplitPlane.setLeftComponent(left);
		}else{
			horrizontalSplitPlane.remove(left);
		}
	}
	
	// (De)seperate the help tool
	public void toogleHelpTool( boolean b ){
		if ( b ){
			helpPanel = HelpFrame.getInstance().getPanel();
			top.addTab("Tutorial", helpPanel);
		}else{
			top.remove(helpPanel);
		}
	}
}
