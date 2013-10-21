package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.services.CopyToClipboardNameLabelService;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.SpringLayoutUtilities;

/**
 * The SettingsGroup is a panel for a group of single settings or multiple settings.
 * 
 * @author Marius Fink
 * @version 08.08.2012
 */
public class SettingsGroup extends JPanel implements XMLPartGUIContainer {

	private static final long serialVersionUID = -2811423589767027384L;
	private XMLResource xml;
	private Set<XMLPartGUIContainer> allXMLGeneratedInputFields = new HashSet<XMLPartGUIContainer>();
	private CopyToClipboardNameLabelService nameLabelService;

	/**
	 * Creates a settings group panel that extracts the gui values by itself from the xml file.
	 * 
	 * @param xml
	 *            the XML-Settings-Container
	 * @param mainBranchIndex
	 *            the index of the corresponding main branch
	 * @param groupIndex
	 *            the index of this group
	 * @param nameLabelService
	 *            the service that creates the name labels
	 */
	public SettingsGroup(XMLResource xml, int mainBranchIndex, int groupIndex,
			CopyToClipboardNameLabelService nameLabelService) {
		assert nameLabelService != null : "Precondition violated: nameLabelService != null";
		assert xml != null : "Precondition violated: xml != null";
		
		this.xml = xml;
		this.nameLabelService = nameLabelService;
		this.xml.setTemporaryPrefix("mainBranch[" + mainBranchIndex + "]/settingsGroups/settingGroup[" + groupIndex
				+ "]/");
		init();
		this.xml.resetTemporaryPrefix();
	}

	/**
	 * Initializes the GUI
	 */
	private void init() {
		setLayout(new SpringLayout());

		String nameOfGroup = xml.getPropertyAsString("name");
		setBorder(BorderFactory.createTitledBorder(nameOfGroup));
		// MFTASK: where to put the group description?

		int noOfSettings = xml.numberOfElements("settings/singleSetting");
		for (int settingsIndex = 1; settingsIndex <= noOfSettings; settingsIndex++) {
			String location = xml.getPropertyAsString("settings/singleSetting[" + settingsIndex + "]/location");
			String typeOfLocation = xml.getRawAttributeValue("settings/singleSetting[" + settingsIndex + "]/location",
					"type");
			XMLPart xmlPart = XMLPart.createXMLPart(location, typeOfLocation);

			String name = xml.getPropertyAsString("settings/singleSetting[" + settingsIndex + "]/name");
			JComponent descriptionLabel = nameLabelService.createCopyToClipboardNameLabel(xmlPart, name);
			add(descriptionLabel);
			if (xml.hasProperty("settings/singleSetting[" + settingsIndex + "]/description")) {
				descriptionLabel.setToolTipText(xml.getPropertyAsString("settings/singleSetting[" + settingsIndex
						+ "]/description")
						+ " - ");
			}
			descriptionLabel.setToolTipText(descriptionLabel.getToolTipText() + GUIText.getText("copyLocation"));

			XMLGeneratedInputField xmlGeneratedInputField = new XMLGeneratedInputField(xml, "settings/singleSetting["
					+ settingsIndex + "]/content", xmlPart);
			add(xmlGeneratedInputField);
			allXMLGeneratedInputFields.add(xmlGeneratedInputField);
		}

		SpringLayoutUtilities.makeCompactGrid(this, // parent
				noOfSettings, 2, // rows, cols
				0, 0, // initX, initY
				30, 3); // xPad, yPad
	}

	@Override
	public Set<XMLPart> getCurrentXMLParts() {
		Set<XMLPart> parts = new HashSet<XMLPart>();
		for (XMLPartGUIContainer partContainer : allXMLGeneratedInputFields) {
			parts.addAll(partContainer.getCurrentXMLParts());
		}
		return parts;
	}

	@Override
	public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed) {
		for (XMLPartGUIContainer partContainer : allXMLGeneratedInputFields) {
			partContainer.setCurrentXMLParts(toBeDisplayed);
		}
	}
}
