package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import framework.core.gui.util.GUIText;

/**
 * BooleanInputField is a panel with two radio buttons for true and false only
 * accepting one of them.
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
public class BooleanInputField extends JPanel implements InputField {

    private static final long serialVersionUID = 5690553550960287902L;
    private boolean trueSelected;
    private JRadioButton trueButton;
    private JRadioButton falseButton;

    /**
     * BooleanInputField has to be instatiated with a preset variable
     * 
     * @param trueSelected
     *            true, if the true button is selected, false, else
     */
    public BooleanInputField(boolean trueSelected) {
	this.trueSelected = trueSelected;
	init();
    }

    /**
     * Initialized this component.
     */
    private void init() {
	ButtonGroup bg = new ButtonGroup();
	trueButton = new JRadioButton(GUIText.getText("true"));
	falseButton = new JRadioButton(GUIText.getText("false"));
	trueButton.setOpaque(false);
	falseButton.setOpaque(false);
	
	bg.add(trueButton);
	bg.add(falseButton);
	trueButton.setSelected(trueSelected);
	falseButton.setSelected(!trueSelected);

	setLayout(new FlowLayout(FlowLayout.LEFT));
	add(trueButton);
	add(falseButton);
	
	setOpaque(false);
    }

    /**
     * (non-Javadoc)
     * 
     * @see framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.InputField#getStringRepresentation()
     */
    @Override
    public String getStringRepresentation() {
	String result = "false";
	if (trueButton.isSelected()) {
	    result = "true";
	}
	
	return result;
    }
    
    @Override
    public void addActionListener(ActionListener al) {
	trueButton.addActionListener(al);
	falseButton.addActionListener(al);
    }

    @Override
    public void setValue(String representation) {
	boolean representationAsBool = (representation.equalsIgnoreCase("true"));
	trueButton.setSelected(representationAsBool);
	falseButton.setSelected(!representationAsBool);
    }

    @Override
    public void empty() {
	setValue(new Boolean(false).toString());
    }

    @Override
    public void disableInput() {
	trueButton.setEnabled(false);
	falseButton.setEnabled(false);
    }
}