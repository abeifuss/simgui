package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import framework.core.gui.util.components.ExtendedDoubleJSlider;

/**
 * FloatInputField is an ExtendedDoubleSlider for Floats
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class FloatInputField extends ExtendedDoubleJSlider implements InputField {

    private static final long serialVersionUID = 6883530602150405287L;
    private float start;

    /**
     * Has to be initialized with initial value, min and max.
     * 
     * @param start
     *            the initial selected value (has to be between min and max)
     * @param min
     *            the minimum value (included)
     * @param max
     *            the maximum value (included)
     * @param precision
     *            the count of decimal places (max 8)
     */
    public FloatInputField(float start, float min, float max, int precision) {
	super(start, min, max, precision);
	this.start = start;

	setOpaque(false);
    }

    /**
     * (non-Javadoc)
     * 
     * @see framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.InputField#getStringRepresentation()
     */
    @Override
    public String getStringRepresentation() {
	return "" + ((float) super.getValue());
    }

    @Override
    public void addActionListener(ActionListener al) {
	final ActionListener alCopy = al;
	super.addChangeListener(new ChangeListener() {
	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		alCopy.actionPerformed(new ActionEvent(arg0, 0, "Spinner state changed."));
	    }
	});
    }

    @Override
    public void setValue(String representation) {
	Float valueOf;
	try {
	    valueOf = Float.valueOf(representation);
	} catch (NumberFormatException n) {
	    valueOf = start;
	}
	setValue(valueOf);
    }

    @Override
    public void empty() {
	setValue("0");
    }

    @Override
    public void disableInput() {
	textField.setEnabled(false);
	slider.setEnabled(false);
    }

}
