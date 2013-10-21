package framework.core.gui.util;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import framework.core.gui.util.components.ConsoleGUI;
import framework.core.gui.util.visualUtilities.GUIColors;

/**
 * Output deals with different outputs from different sources adding them to the logger and writing
 * it on the console with color management.
 * 
 * @author Marius Fink
 * @version 30.06.2012
 */
@SuppressWarnings("rawtypes")
public class Output {
	private Logger logger;
	// private Map<Integer, Color> error_colors = new HashMap<Integer, Color>();
	private static Map<Integer, Color> colors;
	private Class clazz;
	private static Map<Class, Output> outputs = new HashMap<Class, Output>();
	
	static {
		colors = new HashMap<Integer, Color>();
	}
	
	/**
	 * Creates an output for the given class. 
	 * @param clazz the class of the object that uses this output
	 */
	private Output(Class clazz) {
		this.clazz = clazz;
		this.logger = Logger.getLogger(clazz);
		
		try {
			SimpleLayout layout = new SimpleLayout();
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			logger.addAppender(consoleAppender);
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd_HH-mm-ss");
			String filename = "log_" + format.format(new Date());
			FileAppender fileAppender = new FileAppender(layout, "log/" + filename + ".log", false);
			logger.addAppender(fileAppender);
			logger.setLevel(Level.WARN);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param clazz the class of the objetc using the output.
	 * @return a logger for the specified class 
	 */
	public static Output getOutput(Class clazz) {
		if (outputs.containsKey(clazz)) {
			return outputs.get(clazz);
		} else {
			Output output = new Output(clazz);
			outputs.put(clazz, output);
			return output;
		}
	}

	public void err(String errorText) {
		logger.error(errorText);
		
		String string = clazz.getSimpleName() + "| " + errorText;
		ConsoleGUI.out(string, getErrorColor(clazz));
	}

	public void out(String text) {
		logger.info(text);

		String string = clazz.getSimpleName() + "| " + text;
		ConsoleGUI.out(string, getColor(clazz));
	}

	private static Color getColor(Object o) {
		if (colors.containsKey(o.hashCode())) {
			return colors.get(o.hashCode());
		} else {
			Color color = GUIColors.next();
			colors.put(o.hashCode(), color);
			return color;
		}
	}

	private Color getErrorColor(Object o) {
		return Color.RED;
	}
}
