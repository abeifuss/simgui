package framework.core.gui.services;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLResource;
import framework.core.gui.userInterfaces.mainGui.StatusBar;
import framework.core.gui.util.GUIText;

/**
 * XMLUserDialogLoadingService asks the user for an xml-file and returns the xml parts
 * 
 * @author Marius Fink
 * @version 22.09.2012
 */
public class XMLUserDialogLoadingService {

	/**
	 * asks and returns the specified file
	 * 
	 * @return the choice of the user
	 */
	private File askForFile() {
		final JFileChooser fc = new JFileChooser();

		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return GUIText.getText("accept", "*.xml");
			}

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().endsWith(".xml") || arg0.isDirectory();
			}
		});
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		return null;
	}

	/**
	 * Asks the user for an xml file and returns all xml parts of this file.
	 * 
	 * @return the xml parts of the user specified file
	 */
	public Collection<XMLPart> loadNewXmlParts() {
		File askForFile = askForFile();
		if (askForFile != null) {
			XMLResource loadingRessource = new XMLResource(askForFile.getAbsolutePath());
			List<XMLPart> allXMLParts = loadingRessource.getAllXMLParts();
			StatusBar.getInstance().showStatus(GUIText.getText("newValuesLoaded", allXMLParts.size()+""));
			return allXMLParts;
		}
		return new LinkedList<XMLPart>();
	}
}