package framework.core.gui.services;

import javax.swing.JComponent;

import framework.core.gui.model.XMLPart;

/**
 * A simple interface to enable user-specified settings name labels.
 * 
 * @author Marius Fink
 */
public interface CopyToClipboardNameLabelService {

	/**
	 * Create a label for the given setting.
	 * 
	 * @param forPart
	 *            the corresponding part
	 * @param name
	 *            the name of the label
	 * @return a label for the given setting
	 */
	public JComponent createCopyToClipboardNameLabel(XMLPart forPart, String name);
}
