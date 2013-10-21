package framework.core.gui.model;

import java.util.Collection;

/**
 * Classes that implement XMLPartContainer can produce parts of XML-code which can be saved with
 * help of the XML builder. Those classes are also able to load XMLParts.
 * 
 * @author Marius Fink
 * @version 08.08.2012
 */
public interface XMLPartGUIContainer {

	/**
	 * @return a set containing all the xml parts
	 */
	public Collection<XMLPart> getCurrentXMLParts();

	/**
	 * Replaces the currently displayed values with the ones in the list. If the displayed element
	 * is not defined in the list, this method tries to display some "null", empty, 0 or false -
	 * Values
	 * 
	 * @param toBeDisplayed
	 *            the values that are supposed to be displayed in the corresponding InputField
	 */
	public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed);
}
