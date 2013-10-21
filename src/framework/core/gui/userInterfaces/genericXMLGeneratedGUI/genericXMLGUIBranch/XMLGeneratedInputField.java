package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.BooleanInputField;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.FileInputField;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.FloatInputField;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.InputField;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.IntAutoInputField;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.IntInputField;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.ListInputField;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.inputFields.StringInputField;
import framework.core.util.FallbackConversion;

/**
 * XMLGeneratedInputField is a variable inputfield destined by the &lt;content&gt;-node in the
 * possible values xml-file with optional preset. This class also provides validation. Possible
 * input fields are: string, int, float, file, boolean, list (with String values)
 * 
 * @author Marius Fink
 * @version 11.08.2012
 */
class XMLGeneratedInputField extends JPanel implements XMLPartGUIContainer {

	private static final long serialVersionUID = 1936581617357962994L;
	private XMLResource xml;
	private String pathToConcreteContent;
	private InputField actualInputField;
	private XMLPart xmlPart;

	/**
	 * Creates a new Inputfield to the given node in the given XML. Don't forget the temporary path
	 * extensions in the xml object.
	 * 
	 * @param xmlSettings
	 *            the xml object
	 * @param pathToContentNode
	 *            the path to the content node eg. //content
	 */
	XMLGeneratedInputField(XMLResource xmlSettings, String pathToContentNode, XMLPart xmlPartToFill) {
		this.xmlPart = xmlPartToFill;
		xml = xmlSettings;
		if (pathToContentNode.endsWith("/")) {
			pathToContentNode = pathToContentNode.substring(0, pathToContentNode.length() - 1);
		}
		pathToConcreteContent = pathToContentNode + "/*[1]";

		init();
	}

	/**
	 * Initializes this field.
	 */
	private void init() {
		setLayout(new BorderLayout());

		JComponent component = new JPanel();

		String typeOfField = xml.getElementName(pathToConcreteContent);

		if (typeOfField.equalsIgnoreCase("int")) {
			int min = FallbackConversion.parseInt(xml.getRawAttributeValue(pathToConcreteContent, "min"), 0);
			int max = FallbackConversion.parseInt(xml.getRawAttributeValue(pathToConcreteContent, "max"), 100);
			int initialValue = FallbackConversion.parseInt(xml.getRawAttributeValue(pathToConcreteContent, "preset"), min);

			IntInputField intInputField = new IntInputField(initialValue, min, max);
			xmlPart.setValue(new Integer(initialValue));
			actualInputField = intInputField;
			component = intInputField;
		} else if (typeOfField.equalsIgnoreCase("autoint")) {
			int min = FallbackConversion.parseInt(xml.getRawAttributeValue(pathToConcreteContent, "min"), 0);
			int max = FallbackConversion.parseInt(xml.getRawAttributeValue(pathToConcreteContent, "max"), 100);
			String initialValue = xml.getRawAttributeValue(pathToConcreteContent, "preset");

			IntAutoInputField intInputField = new IntAutoInputField(initialValue, min, max);
			xmlPart.setValue(initialValue);
			actualInputField = intInputField;
			component = intInputField;
		} else if (typeOfField.equalsIgnoreCase("float")) {
			float min = FallbackConversion.parseFloat(xml.getRawAttributeValue(pathToConcreteContent, "min"), 0);
			float max = FallbackConversion.parseFloat(xml.getRawAttributeValue(pathToConcreteContent, "max"), 1);
			float initialValue = FallbackConversion.parseFloat(xml.getRawAttributeValue(pathToConcreteContent, "preset"),
					min);
			int precision = FallbackConversion.parseInt(xml.getRawAttributeValue(pathToConcreteContent, "precision"), 2);

			FloatInputField floatInputField = new FloatInputField(initialValue, min, max, precision);
			xmlPart.setValue(new Float(initialValue));
			actualInputField = floatInputField;
			component = floatInputField;
		} else if (typeOfField.equalsIgnoreCase("boolean")) {

			boolean preset = FallbackConversion.parseBoolean(xml.getRawAttributeValue(pathToConcreteContent, "preset"),
					false);

			BooleanInputField booleanInputField = new BooleanInputField(preset);
			xmlPart.setValue(new Boolean(preset));
			actualInputField = booleanInputField;
			component = booleanInputField;
		} else if (typeOfField.equalsIgnoreCase("file")) {
			String preset = xml.getRawAttributeValue(pathToConcreteContent, "preset");

			FileInputField fileInputField = new FileInputField(xml.getRawAttributeValue(pathToConcreteContent, "fileType"));

			xmlPart.setValue(preset);
			actualInputField = fileInputField;
			component = fileInputField;
		} else if (typeOfField.equalsIgnoreCase("list")) {
			String preset = xml.getRawAttributeValue(pathToConcreteContent, "preset");
			ListInputField listInputField = new ListInputField(xml.getSameLayerLeafsAsStringArray(pathToConcreteContent
					+ "/value"), preset);

			xmlPart.setValue(preset);
			actualInputField = listInputField;
			component = listInputField;

		} else { // if not identified choose textfield
			String preset = xml.getRawAttributeValue(pathToConcreteContent, "preset");
			String pattern = xml.getRawAttributeValue(pathToConcreteContent, "pattern");

			StringInputField stringInputField = new StringInputField(preset, pattern);
			if (preset == null) {
				preset = "";
			}
			xmlPart.setValue(preset);
			actualInputField = stringInputField;
			component = stringInputField;
		}

		boolean immutable = FallbackConversion.parseBoolean(xml.getRawAttributeValue(pathToConcreteContent, "immutable"), false);
		if (immutable) {
			actualInputField.disableInput();
		}

		add(component, BorderLayout.CENTER);
	}

	@Override
	public Collection<XMLPart> getCurrentXMLParts() {
		xmlPart.setValue(actualInputField.getStringRepresentation());
		Set<XMLPart> partSet = new HashSet<XMLPart>();
		partSet.add(xmlPart);
		return partSet;
	}

	@Override
	public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed) {
		for (XMLPart p : toBeDisplayed) {
			if (p.referencesSameLocationAs(xmlPart, false)) {
				actualInputField.setValue(p.getValue().toString());
				return;
			}
		}
		// Nothing found?
		//actualInputField.empty(); no! let it be the preset!
	}

	/**
	 * Forces the value of this field to set. Validation is done by the concrete input field.
	 * 
	 * @param value
	 *            the value to set to
	 */
	public void forceSetValue(Object value) {
		actualInputField.setValue(value.toString());
		xmlPart.setValue(actualInputField.getStringRepresentation());
	}
}