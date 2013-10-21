package framework.core.gui.userInterfaces.pluginRegistryGui.addRessource;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import framework.core.gui.model.ResourceMinimumInformation;
import framework.core.gui.model.ResourceType;
import framework.core.gui.util.GUIText;

/**
 * AddResourceGuiService is the service for asking the user by gui for an layer and path to the
 * given resource type.
 * 
 * @author Marius Fink
 * @version 13.11.2012
 */
public class AddResourceGuiService {

	private AddResourceGui gui = new AddResourceGui();
	private ResourceType type;

	/**
	 * Constructor of AddResourceGuiService
	 * 
	 * @param type
	 *            the type of resource to ask for
	 */
	public AddResourceGuiService(final ResourceType type) {
		this.type = type;

		if (type == ResourceType.PLUGIN) {
			gui.setPathTextOfPart(AddResourceGui.PATH_PART_3, "/" + GUIText.getText("plugInSettingsXml"));
			gui.setPathTextOfPart(AddResourceGui.PATH_PART_1, GUIText.getText("layer1Path"));
		} else {
			gui.setPathTextOfPart(AddResourceGui.PATH_PART_3, "/" + GUIText.getText("staticFunctionSettingsXml"));
			gui.setPathTextOfPart(AddResourceGui.PATH_PART_1, GUIText.getText("layer1staticFunctionPath"));
		}

		gui.getLayerSpinner().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int layer = (Integer) gui.getLayerSpinner().getValue();
				gui.setLayerDescription("\"" + GUIText.getText("layer" + layer) + "\"");

				if (type == ResourceType.PLUGIN) {
					gui.setPathTextOfPart(AddResourceGui.PATH_PART_1, GUIText.getText("layer" + layer + "Path"));
				} else {
					gui.setPathTextOfPart(AddResourceGui.PATH_PART_1,
							GUIText.getText("layer" + layer + "staticFunctionPath"));
				}
			}
		});

		gui.getPathTextField().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				enableDisableButtons();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				enableDisableButtons();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				enableDisableButtons();
			}
		});
		enableDisableButtons();
	}

	/**
	 * Disables the ok-button if necessary
	 */
	private void enableDisableButtons() {
		gui.getOkButton().setEnabled(gui.getPathTextField().getText().matches("[a-zA-Z0-9_/]+"));
	}

	/**
	 * Asks the user for a layer and path to the settings document
	 * 
	 * @return the layer and path object or null if the user canceled the dialog
	 */
	public ResourceMinimumInformation askForMinimumResourceInformation() {
		gui.showDialog();
		ResourceMinimumInformation result = null;
		if (gui.isConfirmed()) {
			Integer layer = (Integer) gui.getLayerSpinner().getValue();
			String path = gui.getPathTextOfPart(AddResourceGui.PATH_PART_1);
			path += gui.getPathTextOfPart(AddResourceGui.PATH_PART_2);
			String home = path;
			path += gui.getPathTextOfPart(AddResourceGui.PATH_PART_3);
			String id = gui.getIdTextField().getStringRepresentation();

			result = new ResourceMinimumInformation(layer, path, type, id, home);
		}
		return result;
	}
}
