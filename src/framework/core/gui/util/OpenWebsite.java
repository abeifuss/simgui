package framework.core.gui.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * OpenWebsite offers the Method to open a website on the system as the user
 * expects.
 * 
 * @author Marius Fink
 * @version 28.06.2012
 */
public class OpenWebsite {

    /**
     * Opens up the given website in the default browser.
     * 
     * @param url
     *            the url as string to upen
     * @throws URISyntaxException
     *             if URL is invalid
     * @throws IOException
     *             if location can't be opened
     */
    public static void open(String url) throws URISyntaxException, IOException {
	URI theURI = new URI(url);
	java.awt.Desktop.getDesktop().browse(theURI);
    }
}
