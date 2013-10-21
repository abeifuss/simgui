package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields;

import java.awt.event.ActionListener;

/**
 * InputField is a pattern for the different types of input fields in the generic gui
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public interface InputField {

    /**
     * @return the value as String representation eg. for storing the final value in the
     *         settings file
     */
    public String getStringRepresentation();

    /**
     * To listen for changes at this field.
     * 
     * @param al
     *            the Actionlistener to register
     */
    public void addActionListener(ActionListener al);

    /**
     * Sets the value of this InputField to the given representation, if possible
     * 
     * @param representation
     *            the representation of a value to set this field to, e.g. "1.123" for
     *            floats or "true" for booleans
     */
    public void setValue(String representation);

    /**
     * Tries to set this field to <code>null</code>, which is represented as empty
     * Strings, 0, false or 0 entries in multiple settings
     */
    public void empty();

    /**
     * Disable this field
     */
    public void disableInput();

}
