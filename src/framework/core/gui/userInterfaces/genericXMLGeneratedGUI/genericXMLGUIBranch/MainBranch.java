package framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.services.CopyToClipboardNameLabelService;

/**
 * The MainBranch is a GUI panel for every generic settings branch in the configuration tool.
 * 
 * @author Marius Fink
 * @version 08.08.2012
 */
public class MainBranch extends JPanel implements XMLPartGUIContainer {

	private static final long serialVersionUID = -8543582317118578747L;
	private XMLResource xmlSettings;
	private int mainBranchIndex;
	private Set<XMLPartGUIContainer> allSubElements = new HashSet<XMLPartGUIContainer>();
	private CopyToClipboardNameLabelService nameLabelService;

	/**
	 * Creates a new Mainbranch with all the information it needs. Only Constructor.
	 * 
	 * @param xmlSettings
	 *            the settings to extract the main branches from
	 * @param mainBranchIndex
	 *            the index of this particular main branch
	 * @param nameLabelService
	 *            the service that creates the name labels
	 */
	public MainBranch(XMLResource xmlSettings, int mainBranchIndex, CopyToClipboardNameLabelService nameLabelService) {
		assert xmlSettings != null : "Precondition violated: xmlSettings != null";
		assert nameLabelService != null : "Precondition violated: nameLabelService != null";
		
		this.xmlSettings = xmlSettings;
		this.mainBranchIndex = mainBranchIndex;
		this.nameLabelService = nameLabelService;

		this.xmlSettings.setTemporaryPrefix("mainBranch[" + mainBranchIndex + "]/");
		init();
		this.xmlSettings.resetTemporaryPrefix();
	}

	/**
	 * Initializes the GUI
	 */
	private void init() {
		setLayout(new BorderLayout());

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

		// Add Description
		if (xmlSettings.hasProperty("description")) {
			innerPanel.add(new DescriptionPanel(xmlSettings.getPropertyAsString("description")));
		}

		int noOfSettingsGroups = xmlSettings.numberOfElements("settingsGroups/settingGroup");
		// => for each settings group
		for (int groupIndex = 1; groupIndex <= noOfSettingsGroups; groupIndex++) {
			SettingsGroup settingsGroup = new SettingsGroup(xmlSettings, mainBranchIndex, groupIndex, nameLabelService);
			innerPanel.add(settingsGroup);
			allSubElements.add(settingsGroup);
		}
		int noOfMultipleSettings = xmlSettings.numberOfElements("settingsGroups/multipleSetting");
		for (int multipleIndex = 1; multipleIndex <= noOfMultipleSettings; multipleIndex++) {
			MultipleSetting multipleSetting = new MultipleSetting(xmlSettings, mainBranchIndex, multipleIndex);
			innerPanel.add(multipleSetting);
			allSubElements.add(multipleSetting);
		}

		add(innerPanel, BorderLayout.NORTH);
	}

	@Override
	public Set<XMLPart> getCurrentXMLParts() {
		Set<XMLPart> allXmlParts = new HashSet<XMLPart>();
		for (XMLPartGUIContainer generator : allSubElements) {
			allXmlParts.addAll(generator.getCurrentXMLParts());
		}
		return allXmlParts;
	}

	@Override
	public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed) {
		for (XMLPartGUIContainer generator : allSubElements) {
			generator.setCurrentXMLParts(toBeDisplayed);
		}
	}
}
