package framework.core.gui.model;

import framework.core.gui.services.XMLDocumentCreator;

/**
 * XMLPart is a container for either an xml text node or an attribute XMLParts
 * are comparable - the order is the alphanumerical order of their locations.
 * 
 * @author Marius Fink
 * @version 20.08.2012
 */
public abstract class XMLPart implements Comparable<XMLPart>, Cloneable {

	private String location;
	private Object value;

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Stores this XML Element in th egiven XML-Builder.
	 * 
	 * @param destination
	 *            the XML-Builder to store this element in
	 */
	public abstract void storeIn(XMLDocumentCreator destination);

	/**
	 * Fabric method for creating a matching XMLPart. If the location type is
	 * any Attribute, then the attribute name will be set.
	 * 
	 * @param location
	 *            the location of this part as XPath
	 * @param locationType
	 *            the location type. <code>\@attr</code> for attributes and
	 *            <code>textnode</code> for textnodes
	 * @return the freshly generated XMLPart
	 */
	public static XMLPart createXMLPart(String location, String locationType) {
		assert location != null : "Precondition violated: location != null";

		if (locationType == null) {
			locationType = "unknown";
		}

		XMLPart xmlPart;
		// if attr
		if (locationType != null && locationType.startsWith("@")) {
			XMLAttribute attrXMLPart = new XMLAttribute();
			attrXMLPart.setAttributeName(locationType.substring(1));
			xmlPart = attrXMLPart;
		} else { // else textnode
			xmlPart = new XMLTextNode();
		}
		xmlPart.setLocation(location);

		return xmlPart;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + getLocation() + ": "
				+ getValue();
	}

	/**
	 * Checks if two XMLParts are referencing the same location in the document.
	 * To check, if location is equals would be insufficent since there could be
	 * textnodes with attributes.
	 * 
	 * @param xmlPart
	 *            the XMLPart to compare
	 * @return true, if same location and type, false, else
	 */
	public boolean referencesSameLocationAs(XMLPart xmlPart, boolean ignoreIndex) {
		String loc1 = xmlPart.getLocation();
		String loc2 = getLocation();

		if (ignoreIndex) {
			loc1 = loc1.replaceAll("\\[%index\\]", "").replaceAll("\\[\\d+\\]",
					"");
			loc2 = loc2.replaceAll("\\[%index\\]", "").replaceAll("\\[\\d+\\]",
					"");
		}
		return (this.getClass().equals(xmlPart.getClass()) && loc1.equals(loc2));
	}

	@Override
	public int compareTo(XMLPart o) {
		return location.compareTo(o.getLocation());
	}

	/**
	 * Clones this Part (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public abstract XMLPart clone();

	/**
	 * @return an location string with an unique and explicit location. E.g.
	 *         \a\b for a text node or \a\b@attr for an attribute
	 */
	public abstract String getUniqueIDLocation();
}
