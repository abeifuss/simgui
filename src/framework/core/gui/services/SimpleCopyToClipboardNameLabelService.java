package framework.core.gui.services;

import javax.swing.JComponent;
import javax.swing.JLabel;

import framework.core.gui.model.XMLPart;
import framework.core.gui.util.components.CopyToClipboardNameLabel;

/**
 * This is the standard implementation for the CopyToClipborad label. It creates a label with the
 * displayed name and the unique location identificator of the given xml part.
 * 
 * @author Marius Fink
 * 
 */
public class SimpleCopyToClipboardNameLabelService implements CopyToClipboardNameLabelService {

	@Override
	public JComponent createCopyToClipboardNameLabel(XMLPart forPart, String name) {
		JLabel descriptionLabel = new CopyToClipboardNameLabel(name, forPart.getUniqueIDLocation());
		return descriptionLabel;
	}
}
