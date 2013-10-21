package framework.core.gui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * A helper class with ability to get the right GUI text for a property. Aims to lay out every
 * String from the GUI and provides the option of additional languages
 * 
 * @author Marius Fink
 * @version 02.06.2012
 */
public class GUIText {
	private static String path = "." + File.separator + "etc" + File.separator + "gui" + File.separator;
	private static Properties guiText;

	/**
	 * Initially sets the language to EN
	 */
	static {
		try {
			setLanguage("en");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the String from the language file to the corresponding property
	 * 
	 * @param property
	 *            the property to search for
	 * @return the String from the language file or an empty String
	 */
	public static String getText(String property) {
		if (guiText.containsKey(property)) {
			return guiText.getProperty(property);
		} else {
			return "";
		}
	}

	/**
	 * Loads the property and replaces every replacement set by the matching String. E.g.
	 * "This is a %1 test. %2 %2" will produce by getText(text, "nice", "Foo"):
	 * "This is a nice test. Foo Foo" or on getText(text, "nice"): "This is a nice test. %2 %2"
	 * 
	 * @param property
	 *            the property to search for
	 * @param replacements
	 *            the replacements. Check order!
	 * @return thr replaced String from the language file
	 */
	public static String getText(String property, String... replacements) {
		assert property != null : "Precondition violated: property != null";
		if (!guiText.containsKey(property)) {
			return "";
		}

		String text = guiText.getProperty(property);

		for (int i = 0; i < replacements.length; ++i) {
			int pos = i + 1;
			text = text.replaceAll("%" + pos, StringEscapeUtils.escapeJava(replacements[i]));
		}

		return text;
	}

	/**
	 * Changes the language. The language file has to be at: ./etc/gui/language_xx.txt
	 * 
	 * @param language
	 *            the language key e.g. en
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws IllegalArgumentException
	 *             if the language file isn't available.
	 */
	public static void setLanguage(String language) throws FileNotFoundException, IOException {
		String completeFilePath = path + "language_" + language + ".txt";
		File f = new File(completeFilePath);
		if (f.exists()) {
			guiText = new Properties();
			guiText.load(new FileInputStream(completeFilePath));
		} else {
			throw new IllegalArgumentException("The file " + completeFilePath + " does not exist in filesystem.");
		}
	}
}
