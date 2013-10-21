package framework.core.gui.userInterfaces.mainGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * ToolSelectorMenu is the main menu for the tool selector gui
 * 
 * @author Marius Fink
 * @version 28.06.2012
 */
public class MainMenu extends JMenuBar {
	private static final long serialVersionUID = -7036130135632556694L;
	private List<ActionListener> listeners = new LinkedList<ActionListener>();

	/**
	 * Creates the main menu for the tool selector gui
	 */
	public MainMenu() {
		add(createProgramMenu());
		add(createAboutMenu());
	}

	/**
	 * Creates the help menu
	 * 
	 * @return the help menu
	 */
	private JMenu createAboutMenu() {
		JMenu about = new JMenu("About");

		about.setMnemonic(KeyEvent.VK_A);
		about.getAccessibleContext().setAccessibleDescription("Licence, Homepage, Informations");
		JMenuItem licence = new JMenuItem("Licence information", ImageLoader.loadIcon("script"));
		licence.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyListeners("open https://svs.informatik.uni-hamburg.de/gmix/");
			}
		});
		about.add(licence);

		JMenuItem homepage = new JMenuItem("gMix homepage", ImageLoader.loadIcon("webpage"));
		homepage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyListeners("open https://svs.informatik.uni-hamburg.de/gmix/");
			}
		});
		about.add(homepage);

		JMenuItem github = new JMenuItem("gMix on GitHub", ImageLoader.loadIcon("webpage"));
		github.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyListeners("open https://github.com/kpfuchs/gMix/tree/master/gMixFramework/src");
			}
		});
		about.add(github);

		about.add(createHelpMenu());

		return about;
	}

	/**
	 * The submenu "help" in about.
	 * 
	 * @return the menu
	 */
	private JMenu createHelpMenu() {
		JMenu help = new JMenu("Help");

		JMenuItem homepage = new JMenuItem("gMix homepage", ImageLoader.loadIcon("webpage"));
		homepage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyListeners("open https://svs.informatik.uni-hamburg.de/gmix/");
			}
		});
		help.add(homepage);

		return help;
	}

	/**
	 * Creates the program menu
	 * 
	 * @return the program menu
	 */
	private JMenu createProgramMenu() {
		JMenu program = new JMenu("Program");

		JMenuItem console = new JMenuItem("Toggle Console", ImageLoader.loadIcon("console"));
		console.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyListeners("toggleconsole");
			}
		});
		program.add(console);

		JMenuItem exit = new JMenuItem("Exit", ImageLoader.loadIcon("stop"));
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyListeners("exit");
			}
		});
		program.add(exit);

		return program;
	}

	/**
	 * Adds an Actionlistener to this menu. Everytime an Action is performed this menu will send an
	 * event with the right identifier to the listener. E.g. "exit" or "open http://www.xyz.com".
	 * 
	 * @param al
	 *            the ActionListener to register.
	 */
	public void addActionListener(ActionListener al) {
		listeners.add(al);
	}

	/**
	 * Notifies the listeners
	 * 
	 * @param command
	 *            the command to send to the listener
	 */
	private void notifyListeners(String command) {
		ActionEvent ae = new ActionEvent(this, 0, command);
		for (ActionListener al : listeners) {
			al.actionPerformed(ae);
		}
	}

}
