package framework.core.gui.util.visualUtilities;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;

/**
 * The class ImageLoader loads images failsafely
 * 
 * @author Marius Fink
 * @version 30.05.2012
 */
public class ImageLoader {

	private static final int STANDARD_ICON_DIMENSION = 18;
	private static String path = "etc" + File.separator + "gui" + File.separator;
	private static String iconPath = "etc" + File.separator + "gui" + File.separator + "img" + File.separator;
	private static Properties iconSet;
	private static String escapedSeparator;

	/**
	 * Initially sets the icons
	 */
	static {
		try {
			// setIconPackage("famfamfam");
			setIconPackage("wireframemono");
			// setIconPackage("mono");
			escapedSeparator = File.separator;
			if (escapedSeparator.equals("\\")) {
				escapedSeparator = "\\\\";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the icon to the corresponding identifier. the returned icon is scaled to the standard
	 * icon size (setting of this class)
	 * 
	 * @param identifier
	 *            the identifier for the icon (the key in the icons_xx.txt document)
	 * @return the image icon - or a blank one on error
	 */
	public static ImageIcon loadIcon(String identifier) {
		return loadIcon(identifier, STANDARD_ICON_DIMENSION);
	}

	/**
	 * Returns the icon to the corresponding identifier with NO rescaling
	 * 
	 * @param identifier
	 *            the identifier for the icon (the key in the icons_xx.txt document)
	 * @return the image icon - or a blank one on error
	 */
	public static ImageIcon loadIconNoRescale(String identifier) {
		String property = iconSet.getProperty(identifier);

		if (iconSet.containsKey(identifier)) {
			File img = new File(iconPath + property.replaceAll("/", escapedSeparator));
			if (img.exists()) {
				return new ImageIcon(img.getPath());
			}
		}
		return new ImageIcon();
	}

	/**
	 * Returns the icon to the corresponding identifier in the given size.
	 * 
	 * @param identifier
	 *            the identifier for the icon (the key in the icons_xx.txt document)
	 * @param dimension
	 *            the max height or width
	 * @return the image icon - or a blank one on error
	 */
	public static ImageIcon loadIcon(String identifier, int dimension) {
		String property = iconSet.getProperty(identifier);

		if (iconSet.containsKey(identifier)) {
			File img = new File(iconPath + property.replaceAll("/", escapedSeparator));
			if (img.exists()) {
				Image i = new ImageIcon(img.getPath()).getImage();
				return new ImageIcon(i.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH));
			}
		}
		return new ImageIcon();
	}

	/**
	 * @return the tray and window icons for the Main Frame
	 */
	public static List<Image> generateWindowIcons() {
		List<Image> icons = new LinkedList<Image>();

		icons.add(ImageLoader.loadIcon("icon128").getImage());
		icons.add(ImageLoader.loadIcon("icon64").getImage());
		icons.add(ImageLoader.loadIcon("icon32").getImage());
		icons.add(ImageLoader.loadIcon("icon16").getImage());

		return icons;
	}

	/**
	 * Changes the icons. The corresponding file has to be at: ./etc/gui/icons_xx.txt
	 * 
	 * @param pack
	 *            the language key e.g. en
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 *             if the file isn't available.
	 */
	public static void setIconPackage(String pack) throws FileNotFoundException, IOException {
		String completeFilePath = path + "icons_" + pack + ".txt";
		File f = new File(completeFilePath);
		if (f.exists()) {
			iconSet = new Properties();
			iconSet.load(new FileInputStream(completeFilePath));
		} else {
			throw new IllegalArgumentException("The file " + completeFilePath + " does not exist in filesystem.");
		}
	}
}
