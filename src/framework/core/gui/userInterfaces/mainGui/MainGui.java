package framework.core.gui.userInterfaces.mainGui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.WindowListenerAdaptor;
import framework.core.gui.util.visualUtilities.GUIStyler;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * MainGui is a GUI that integrates all the tabs (representing a use case each) into one GUI.
 * 
 * @author Marius Fink
 * @version 14.09.2012
 */
public class MainGui {
	private JFrame mainFrame;
	private JTabbedPane tabbedPane;
	private MainMenu menu;

	public MainGui() {
		init();
	}

	/**
	 * Initializes this window.
	 */
	private void init() {
		GUIStyler.style();

		setMainFrame(new JFrame());
		getMainFrame().setTitle(GUIText.getText("mainWindowTitle"));

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

		setTabbedPane(new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT));

		// old: here were the tabs

		mainPanel.add(getTabbedPane(), BorderLayout.CENTER);
		mainPanel.add(new GMixTitlePanel("StartUp"), BorderLayout.NORTH);
		mainPanel.add(StatusBar.getInstance().getPanel(), BorderLayout.SOUTH);

		getMainFrame().setContentPane(mainPanel);

		getMainFrame().addWindowListener(new WindowListenerAdaptor() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		mainFrame.setIconImages(ImageLoader.generateWindowIcons());

		setMenu(new MainMenu());
		getMainFrame().setJMenuBar(getMenu());
	}

	/**
	 * Shows the GUI:
	 */
	public void show() {
		getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getMainFrame().setPreferredSize(new Dimension(900, 600));
		getMainFrame().setSize(new Dimension(900, 600));
		getMainFrame().setLocationRelativeTo(null);
		getMainFrame().setVisible(true);
	}

	/**
	 * @return the mainFrame
	 */
	public JFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * @param mainFrame
	 *            the mainFrame to set
	 */
	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	/**
	 * @return the tabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * @param tabbedPane
	 *            the tabbedPane to set
	 */
	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	/**
	 * @return the menu
	 */
	public MainMenu getMenu() {
		return menu;
	}

	/**
	 * @param menu
	 *            the menu to set
	 */
	public void setMenu(MainMenu menu) {
		this.menu = menu;
	}
}
