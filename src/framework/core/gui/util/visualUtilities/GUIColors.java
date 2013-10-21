package framework.core.gui.util.visualUtilities;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import framework.core.config.Paths;

/**
 * Reads a list of colors and return the colors in different ways.
 * 
 * @author Marius Fink
 * @version 01.07.2012
 */
public class GUIColors {

	private static Properties colors;
	private static int actual = 0;
	private static List<String> names;

	static {
		try {
			colors = new Properties();
			colors.load(new FileInputStream(Paths.COLOR_SCHEME_FILE_PATH));

			names = new LinkedList<String>();
			for (Entry<Object, Object> entry: colors.entrySet()) {
				names.add(entry.getValue().toString());
			}
			Collections.sort(names);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("property file could not be loaded!");
		}
		
	}

	/**
	 * @param name
	 *            the identifier of the color
	 * @return the color with the given name or BLACK on error.
	 */
	public static Color getColorByName(String name) {
		return stringToColor(colors.getProperty(name));
	}

	/**
	 * @param colorString
	 *            pattern: r,g,b
	 */
	private static Color stringToColor(String colorString) {
		try {
			String[] values = colorString.split(",");
			int r = Integer.parseInt(values[0]);
			int g = Integer.parseInt(values[1]);
			int b = Integer.parseInt(values[2]);
			return new Color(r, g, b);
		} catch (Exception e) {
			return Color.BLACK;
		}
	}

	/**
	 * @return the next color ("next" by the last time "next()" was called)
	 */
	public static Color next() {
		return getColorNumber(actual++);
	}

	/**
	 * @param number
	 *            the number of the color
	 * @return a color identified by its incremental index
	 */
	public static Color getColorNumber(int number) {
		
		return getColorByName(names.get(number % names.size()));
	}
}
