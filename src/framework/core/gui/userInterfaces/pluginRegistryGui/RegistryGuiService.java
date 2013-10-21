package framework.core.gui.userInterfaces.pluginRegistryGui;

import java.awt.Component;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;

import framework.core.gui.model.ResourceType;
import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.model.XMLResourceContainer;
import framework.core.gui.services.CopyToClipboardNameLabelService;
import framework.core.gui.services.XMLDocumentCreator;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.GenericXMLGUIService;
import framework.core.gui.userInterfaces.mainGui.StatusBar;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.CopyToClipboardNameLabel;
import framework.core.gui.util.components.PopUpPrompt;

/**
 * A generic abstract GUI service for registry settings
 * 
 * @author Marius Fink
 * 
 */
public abstract class RegistryGuiService {

	protected final String path;
	protected final ResourceType type;
	protected final String name;
	protected final XMLResource settings;
	protected final GenericXMLGUIService guiService;

	/**
	 * Creates a new Registry GUI Service
	 * 
	 * @param path
	 *            the path to the resource
	 * @param type
	 *            the type of the resource
	 * @param name
	 *            the name of the resource
	 * @param id
	 *            the id of the resource
	 * @param settings
	 *            the corresponding xml settings
	 */
	public RegistryGuiService(String path, final ResourceType type, final XMLResourceContainer container, XMLResource settings) {
		assert path != null : "Precondition violated: path != null";
		assert type != null : "Precondition violated: type != null";
		assert container != null : "Precondition violated: name != null";
		assert settings != null : "Precondition violated: settings != null";

		this.path = path;
		this.type = type;
		this.name = container.getName();
		this.settings = settings;
		settings.setRootNode("guiInfo/possibleGuiValues/");
		CopyToClipboardNameLabelService nameLabelService = new CopyToClipboardNameLabelService() {
			@Override
			public JComponent createCopyToClipboardNameLabel(XMLPart forPart, String name) {
				String locationIdentifier = forPart.getUniqueIDLocation();
				// from 
				// /plugIn/somewhere/here
				// to
				// /gMixConfiguration/composition/layerN/[client|mix]/plugIn[@id='[ID]']/somewhere/here
				if (type == ResourceType.PLUGIN) {
					String clientMix = container.getResource().getRawAttributeValue("/plugIn", "type").toLowerCase();
					locationIdentifier = locationIdentifier.replaceFirst("/plugIn", "/gMixConfiguration/composition/layer" + container.getLayer() +  "/" + clientMix + "/plugIn[@id='" + container.getId() + "']");
				} else {
					locationIdentifier = locationIdentifier.replaceFirst("/staticFunction", "/gMixConfiguration/availableResources/availableStaticFunctions/staticFunction[@id='" + container.getId() + "']");
				}

				return new CopyToClipboardNameLabel(name, locationIdentifier);
			};
		};
		this.guiService = new GenericXMLGUIService(settings, nameLabelService);
	}

	/**
	 * Creates the panel
	 * 
	 * @return the panel and the action listeners
	 */
	public abstract Component getPanel();

	/**
	 * Displays the given values.
	 * 
	 * @param values
	 *            thw values to display instead of presets.
	 */
	public void replaceDisplayedValuesWith(List<XMLPart> values) {
		guiService.setCurrentXMLParts(values);

		for (XMLPartGUIContainer container : getAdditionalXMLPartContainer()) {
			container.setCurrentXMLParts(values);
		}
	}

	/**
	 * Save the changes.
	 */
	public void save() {
		try {
			String rootnode = settings.getPropertyAsString("rootNode");
			XMLDocumentCreator c = new XMLDocumentCreator(rootnode);

			for (XMLPart part : guiService.getCurrentXMLParts()) {
				part.storeIn(c);
			}
			for (XMLPartGUIContainer pc : getAdditionalXMLPartContainer()) {
				for (XMLPart part : pc.getCurrentXMLParts()) {
					part.storeIn(c);
				}
			}

			c.saveAs(path);
			StatusBar.getInstance().showStatus(GUIText.getText("successfullySaved", name, path));
		} catch (IOException e) {
			PopUpPrompt.displayIOExceptionMessage(e);
			StatusBar.getInstance().showStatus(GUIText.getText("failedSave", name, path));
		} catch (AssertionError e) {
			PopUpPrompt.displayGenericErrorMessage(GUIText.getText("fileCorrupt"),
					GUIText.getText("couldNotGetOrStoreItems", path, e.getMessage()));
			StatusBar.getInstance().showStatus(GUIText.getText("failedSave", name, path));
		}
	}
	protected abstract List<XMLPartGUIContainer> getAdditionalXMLPartContainer();
}
