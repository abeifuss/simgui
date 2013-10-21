package framework.core.gui.util.components;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * PopUpPrompt is a helper class that creates all the error and info prompts (popups). Sometimes
 * there's just no better way to say "You're a fool, user." than with a loud and startling popup.
 * 
 * @author Marius Fink
 * @version 06.11.2012
 */
public class PopUpPrompt {

	public static void displayIOExceptionMessage(Exception e1) {
		JOptionPane.showConfirmDialog(null, GUIText.getText("IOError") + "\n" + GUIText.getText("nestedException")
				+ " " + e1.getMessage(), GUIText.getText("IOErrorShort"), JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.ERROR_MESSAGE, ImageLoader.loadIcon("error", 50));
	}

	public static void displayGenericExceptionMessage(Exception e1, String headline, String text) {
		JOptionPane.showConfirmDialog(null, text + "\n" + GUIText.getText("nestedException") + " " + e1.getMessage(),
				headline, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, ImageLoader.loadIcon("error", 50));
	}

	public static void displayGenericErrorMessage(String headline, String text) {
		JOptionPane.showConfirmDialog(null, text + "\n", headline, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.ERROR_MESSAGE, ImageLoader.loadIcon("error", 50));
	}

	public static boolean displayGenericYesNoMessage(String headline, String text, ImageIcon loadIcon) {
		int answer = JOptionPane.showConfirmDialog(null, text, headline, JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, loadIcon);

		return answer == JOptionPane.YES_OPTION;
	}
}
