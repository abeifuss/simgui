package framework.core.gui.util.visualUtilities;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import framework.core.gui.util.GUIText;

/**
 * GUIStyler sets global preferences for all GUIs
 * 
 * @author Marius Fink
 * @version 08.08.2012
 */
public class GUIStyler {

	/**
	 * Sets the Look&Feel as well as the OptionPane preset Texts.
	 */
	public static void style() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("OptionPane.cancelButtonText", GUIText.getText("cancel"));
			UIManager.put("OptionPane.noButtonText", GUIText.getText("no"));
			UIManager.put("OptionPane.okButtonText", GUIText.getText("ok"));
			UIManager.put("OptionPane.yesButtonText", GUIText.getText("yes"));
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (Exception e) {
		}
	}

}
