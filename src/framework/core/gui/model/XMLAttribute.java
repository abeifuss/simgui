package framework.core.gui.model;

import framework.core.gui.services.XMLDocumentCreator;

/**
 * XMLAttribute represents an attribute at a specific node with a name and a value in an XML
 * document.
 * 
 * @author Marius Fink
 * @version 26.08.2012
 */
public class XMLAttribute extends XMLPart {

	private String attributeName;

	/**
	 * (non-Javadoc)
	 * 
	 * @see framework.core.gui.model.XMLPart#storeIn(framework.core.gui.services.XMLDocumentCreator)
	 */
	@Override
	public void storeIn(XMLDocumentCreator destination) {
		assert getLocation() != null : "Precondition violated: getLocation() != null";
		assert getAttributeName() != null : "Precondition violated: getAttributeName() != null";
		assert getValue() != null : "Precondition violated: getValue() != null";

		destination.addAttribute(getLocation(), getAttributeName(), getValue());

	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName
	 *            the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + getLocation() + ": " + getAttributeName() + "=" + getValue();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see framework.core.gui.model.XMLPart#referencesSameLocationAs(framework.core.gui.model.XMLPart,
	 *      boolean)
	 */
	@Override
	public boolean referencesSameLocationAs(XMLPart xmlPart, boolean ignoreIndex) {
		String loc1 = xmlPart.getLocation();
		String loc2 = getLocation();

		if (xmlPart instanceof XMLAttribute) {
			XMLAttribute other = (XMLAttribute) xmlPart;
			if (ignoreIndex) {
				loc1 = loc1.replaceAll("\\[%index\\]", "").replaceAll("\\[\\d+\\]", "");
				loc2 = loc2.replaceAll("\\[%index\\]", "").replaceAll("\\[\\d+\\]", "");
			}
			return (loc1.equals(loc2) && other.getAttributeName().equals(this.getAttributeName()));
		} else {
			return false;
		}

	}

	@Override
	public XMLPart clone() {
		XMLAttribute newPart = new XMLAttribute();
		newPart.setLocation(getLocation());
		newPart.setAttributeName(attributeName);
		newPart.setValue(getValue());

		return newPart;
	}

	@Override
	public String getUniqueIDLocation() {
		return getLocation() + "@" + getAttributeName();
	}

}
