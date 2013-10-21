package framework.core.gui.util.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import framework.core.gui.userInterfaces.mainGui.StatusBar;
import framework.core.gui.util.ClipboardManager;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * CopyToClipboardNameLabel is a Label with a copy icon and a name. It copies the given text into
 * the system's clipboard on click.
 * 
 * @author Marius Fink
 * @version 03.11.2012
 */
public class CopyToClipboardNameLabel extends JLabel {
	private static final long serialVersionUID = 5437060142248880509L;

	/**
	 * Constructor of CopyToClipboardNameLabel
	 * 
	 * @param displayName
	 *            the String to display on this Label
	 * @param toCopy
	 *            the String that will be copied into the system's clipboard on click
	 */
	public CopyToClipboardNameLabel(String displayName, final String toCopy) {

		super(displayName);

		final ImageIcon copyIcon = ImageLoader.loadIcon("copyLocation", 10);
		final ImageIcon okIcon = ImageLoader.loadIcon("ok", 10);

		setIcon(copyIcon);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ClipboardManager.copyToClipboard(toCopy);
				StatusBar.getInstance().showStatus(GUIText.getText("copiedToClipboard", toCopy));

				setIcon(okIcon);

				Timer timer = new Timer(0, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						setIcon(copyIcon);
					}
				});
				timer.setInitialDelay(1000);
				timer.setRepeats(false);
				timer.start();
			}
		});
	}
	
}
