package framework.core.gui.userInterfaces.genericXMLGeneratedGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.services.CopyToClipboardNameLabelService;
import framework.core.gui.services.SimpleCopyToClipboardNameLabelService;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch.MainBranch;

/**
 * GenericConfigurationGUIService handles the GenericConfigurationGUI by filling it with tabs and
 * listening to its actions.
 * 
 * @author Marius Fink
 * @version 08.08.2012
 */
public class GenericXMLGUIService implements XMLPartGUIContainer {

	private Set<XMLPartGUIContainer> allMainBranches = new HashSet<XMLPartGUIContainer>();
	private final XMLResource XML_SETTINGS;
	private GenericXMLGUI gui;
	private final CopyToClipboardNameLabelService nameLabelService;

	/**
	 * Constructor of class GenericConfigurationGUIService
	 * 
	 * @param xmlSettings
	 *            the XML-Settings to operate on. Please consider documentation for patterns of the
	 *            corresponding file.
	 */
	public GenericXMLGUIService(XMLResource xmlSettings) {
		assert xmlSettings != null : "Precondition violated: xmlSettings != null";

		XML_SETTINGS = xmlSettings;
		gui = new GenericXMLGUI();
		this.nameLabelService = new SimpleCopyToClipboardNameLabelService();

		addMainBranchesFromXML(gui);
	}

	/**
	 * This is a constructor with a special NameLabelService e.g. for Plug-ins that have IDs in it.
	 * 
	 * @param settings
	 *            the XML-Settings to operate on. Please consider documentation for patterns of the
	 *            corresponding file.
	 * @param nameLabelService
	 *            the service that creates the name labels
	 */
	public GenericXMLGUIService(XMLResource xmlSettings, CopyToClipboardNameLabelService nameLabelService) {
		assert xmlSettings != null : "Precondition violated: xmlSettings != null";
		assert nameLabelService != null : "Precondition violated: nameLabelService != null";

		XML_SETTINGS = xmlSettings;
		gui = new GenericXMLGUI();
		this.nameLabelService = nameLabelService;

		addMainBranchesFromXML(gui);
	}

	/**
	 * Searches for main branches and adds them as tabs to the gui
	 * 
	 * @param gui
	 *            the gui to add the main branches
	 */
	private void addMainBranchesFromXML(GenericXMLGUI gui) {
		assert gui != null : "Precondition violated: gui != null";
		
		int noOfMainBranches = XML_SETTINGS.numberOfElements("mainBranch");

		for (int i = 1; i <= noOfMainBranches; i++) {
			JPanel wrapperPanel = new JPanel(new BorderLayout());
			JPanel innerPanel = new JPanel();
			innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

			String name = XML_SETTINGS.getPropertyAsString("mainBranch[" + i + "]/name");

			MainBranch mainBranch = new MainBranch(XML_SETTINGS, i, nameLabelService);
			allMainBranches.add(mainBranch);
			innerPanel.add(mainBranch);

			wrapperPanel.add(innerPanel, BorderLayout.NORTH);
			gui.addTab(name, wrapperPanel);
		}
	}

	/**
	 * @return Creates the Panel of this GUI.
	 */
	public Component createPanel() {
		return gui.getContentPane();
	}

	/**
	 * Adds an individual tab to this generic GUI
	 * 
	 * @param tab
	 *            the tab content to add
	 * @param title
	 *            the title of the tab
	 */
	public void addIndividualTab(JComponent tab, String title) {
		gui.addTab(title, tab);
	}

	/**
	 * Adds a button at the bottom area of this GUI.
	 * 
	 * @param title
	 *            the title of the button
	 * @param icon
	 *            the icon of this button, may be <code>null</code>
	 * @param action
	 *            the ActionListener for this button
	 */
	public void addBottomButton(String title, ImageIcon icon, ActionListener action) {
		JButton btn = new JButton(title, icon);
		btn.addActionListener(action);
		gui.getSaveButtonPanel().add(btn);
		gui.getSaveButtonPanel().updateUI();
	}

	/**
	 * Discards all displayed values and shows those values that exist in GUI and the given list.
	 * The rest is null.
	 * 
	 * @param toBeDisplayed
	 *            the values to be displayed if existing in GUI
	 */
	@Override
	public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed) {
		for (XMLPartGUIContainer mb : allMainBranches) {
			mb.setCurrentXMLParts(toBeDisplayed);
		}
	}

	@Override
	public Collection<XMLPart> getCurrentXMLParts() {
		Collection<XMLPart> allParts = new HashSet<XMLPart>();

		for (XMLPartGUIContainer c : allMainBranches) {
			allParts.addAll(c.getCurrentXMLParts());
		}

		return allParts;
	}

	public void addXmlPartGenerator(XMLPartGUIContainer container) {
		allMainBranches.add(container);
	}
}
