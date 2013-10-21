package framework.core.gui.services;

import java.io.IOException;
import java.util.Collection;

import javax.swing.JFileChooser;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import framework.core.gui.model.XMLPart;
import framework.core.gui.userInterfaces.mainGui.StatusBar;
import framework.core.gui.util.GUIText;

/**
 * XMLUserDialogSavingService akss
 * 
 * @author Marius Fink
 * @version 17.08.2012
 */
public class XMLUserDialogSavingService {
	private String staticPath = null;
	private Collection<XMLPart> content;
	private String root;
	private static final Logger LOGGER = Logger.getLogger(XMLUserDialogSavingService.class);

	public XMLUserDialogSavingService(Collection<XMLPart> content, String root, String staticPath) {
		this.staticPath = staticPath;
		this.content = content;
		this.root = root;
		BasicConfigurator.configure();
	}

	public XMLUserDialogSavingService(Collection<XMLPart> content, String root) {
		this.content = content;
		this.root = root;
		BasicConfigurator.configure();
	}

	/**
	 * Saves all the given XML Parts into the file identified with the path.
	 * 
	 * @param allXMLParts
	 *            the XML-File content to be
	 * @param path
	 *            the path to store the file at
	 */
	public void save() {

		String path = staticPath;
		if (path == null) {
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				path = fc.getSelectedFile().getAbsolutePath();
			} else {
				return;
			}
		}

		int saved = 0;
		XMLDocumentCreator xsdc = new XMLDocumentCreator(root);
		for (XMLPart xmlPart : content) {
			xmlPart.storeIn(xsdc);
			saved++;
		}

		try {
			xsdc.saveAs(path);
			LOGGER.info("Saved " + saved + " settings successfully in \"" + path + "\".");
			StatusBar.getInstance().showStatus(GUIText.getText("configSaved", path));
		} catch (IOException e) {
			LOGGER.error("Saving of \"" + path + "\" failed. Nested Exception is: ", e);
			StatusBar.getInstance().showStatus(GUIText.getText("configSavedError", e.getMessage()));
		}
	}
}