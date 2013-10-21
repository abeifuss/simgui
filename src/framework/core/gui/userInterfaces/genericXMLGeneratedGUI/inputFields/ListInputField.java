package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields;

import javax.swing.JComboBox;

/**
 * ListInputField is a ComboBox Component
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class ListInputField extends JComboBox<String> implements InputField {

    private static final long serialVersionUID = 320752453076594583L;
    private String[] values;
    private String selectedItem;

    /**
     * @param values
     *            the values to display
     * @param selected
     *            the selected value, can be null for selecting the first one.
     */
    public ListInputField(String[] values, String selected) {
	super(values);
	this.values = values;
	this.selectedItem = selected;

	setValue(selectedItem);
	setOpaque(false);
    }

    /**
     * (non-Javadoc)
     * 
     * @see framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.InputField#getStringRepresentation()
     */
    @Override
    public String getStringRepresentation() {
	return getSelectedItem().toString();
    }

    @Override
    public void setValue(String representation) {
	int index = findElement(values, representation);
	int toSelect = (index != -1) ? index : 0;
	super.setSelectedIndex(toSelect);
    }

    /**
     * Finds the index of the given element in the given array
     * 
     * @param array
     *            the haystack
     * @param element
     *            the needle
     * @return the index or -1 if not found or <code>null</code>
     */
    private int findElement(String[] array, String element) {
	if (element == null) {
	    return -1;
	}
	for (int i = 0; i < array.length; i++) {
	    if (array[i].equals(element)) {
		return i;
	    }
	}
	return -1;
    }

    @Override
    public void empty() {
	super.setSelectedIndex(0); //uses first entry
    }

    @Override
    public void disableInput() {
	setEnabled(false);
    }
}
