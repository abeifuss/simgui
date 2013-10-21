package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.util.visualUtilities.GUIColors;

/**
 * MultipleSettingsLine is one line in a multiple settings group
 * 
 * @author Marius Fink
 * @version 12.08.2012
 */
public class MultipleSettingsLine extends JPanel implements XMLPartGUIContainer {

    private static final Color SELECTED_COLOR = GUIColors.getColorByName("selectionColor");
    private static final Color STANDARD_COLOR = new JPanel().getBackground();
    
    private static final long serialVersionUID = 1384135646286197849L;
    private String path;
    private XMLResource xml;
    private boolean selected;
    private List<ActionListener> listeners = new LinkedList<ActionListener>();
    private int currentIndex = -1;
    private Map<XMLPart, XMLGeneratedInputField> myFields = new HashMap<XMLPart, XMLGeneratedInputField>();

    public MultipleSettingsLine(XMLResource xml, String pathAdditionsToMultipleSettingsElement) {
	this.xml = xml;
	this.path = pathAdditionsToMultipleSettingsElement;

	init();
    }

    /**
     * Initialized the component.
     */
    private void init() {
	FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
	flowLayout.setVgap(1);
	setLayout(flowLayout);

	int noOfSettings = xml.numberOfElements(path + "settings/singleSetting");

	final JCheckBox checkBox = new JCheckBox();
	checkBox.setOpaque(false);
	checkBox.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		setSelected(checkBox.isSelected());
		informListeners();
	    }
	});
	add(checkBox);

	for (int i = 1; i <= noOfSettings; i++) {
	    JLabel jLabel = new JLabel(xml.getPropertyAsString(path + "settings/singleSetting[" + i
		    + "]/name"));
	    String description = xml.getPropertyAsString(path + "settings/singleSetting[" + i
		    + "]/description");
	    if (description != null) {
		jLabel.setToolTipText(description);
	    }
	    add(jLabel);

	    String location = xml.getPropertyAsString("settings/singleSetting[" + i + "]/location");
	    String typeOfLocation = xml.getRawAttributeValue("settings/singleSetting[" + i
		    + "]/location", "type");
	    XMLPart xmlPart = XMLPart.createXMLPart(location, typeOfLocation);

	    XMLGeneratedInputField xgif = new XMLGeneratedInputField(xml, path
		    + "settings/singleSetting[" + i + "]/content", xmlPart);
	    xgif.setOpaque(false);
	    add(xgif);

	    myFields.put(xmlPart, xgif);
	}
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
	return selected;
    }

    /**
     * @param selected
     *            the selected to set
     */
    public void setSelected(boolean selected) {
	this.selected = selected;
	if (selected) {
	    setBackground(SELECTED_COLOR);
	} else {
	    setBackground(STANDARD_COLOR);
	}
    }

    public void addActionListener(ActionListener al) {
	listeners.add(al);
    }

    public void removeActionListener(ActionListener al) {
	listeners.remove(al);
    }

    private void informListeners() {
	ActionEvent ae = new ActionEvent(this, this.hashCode(),
		"This item was checked or unchecked.");
	for (ActionListener al : listeners) {
	    al.actionPerformed(ae);
	}
    }

    @Override
    public Set<XMLPart> getCurrentXMLParts() {
	assert currentIndex != -1 : "Precondition violated: Index of this setting was not set!";

	for (XMLPart part : myFields.keySet()) {
	    part.setLocation(part.getLocation().replaceAll("%index", "" + currentIndex));
	}

	return myFields.keySet();
    }

    /**
     * Sets the current index to i - this is the index that replaces %index in the
     * location
     * 
     * @param i
     *            the current index
     */
    public void setCurrentXmlIndex(int i) {
	//assert currentIndex == -1 : "Precondition violated: The index has been set already!";
	currentIndex = i;
    }

    @Override
    public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed) {
	for (XMLPart p : toBeDisplayed) {
	    setValueFor(p, p.getValue().toString());
	}
    }

    /**
     * Sets the value for a specific input field.
     * 
     * @param xmlPart
     *            the part to determine the correct field
     * @param value
     *            the value to set to
     */
    private void setValueFor(XMLPart xmlPart, String value) {
	for (Entry<XMLPart, XMLGeneratedInputField> e : myFields.entrySet()) {
	    if (e.getKey().referencesSameLocationAs(xmlPart, true)) {
		e.getValue().forceSetValue(value);
	    }
	}
    }
}
