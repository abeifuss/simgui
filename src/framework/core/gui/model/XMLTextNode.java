package framework.core.gui.model;

import framework.core.gui.services.XMLDocumentCreator;

/**
 * XMLTextNode represents a text value in a specific node.
 * 
 * @author Marius Fink
 * @version 26.08.2012
 */
public class XMLTextNode extends XMLPart {

	/**
	 * (non-Javadoc)
	 * 
	 * @see framework.core.gui.model.XMLPart#storeIn(framework.core.gui.services.XMLDocumentCreator)
	 */
	@Override
	public void storeIn(XMLDocumentCreator destination) {
		assert getLocation() != null : "Precondition violated: getLocation() != null";
		assert getValue() != null : "Precondition violated: getValue() != null";

		destination.addValue(getLocation(), getValue());
	}

	@Override
	public XMLPart clone() {
		XMLTextNode textNode = new XMLTextNode();
		textNode.setLocation(getLocation());
		textNode.setValue(getValue());

		return textNode;
	}

	@Override
	public String getUniqueIDLocation() {
		return getLocation();
	}
}
