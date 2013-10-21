package framework.core.gui.services;

import java.io.File;

import framework.core.config.Paths;
import framework.core.gui.model.ResourceMinimumInformation;
import framework.core.gui.model.ResourceType;
import framework.core.gui.model.XMLResource;
import framework.core.gui.model.XMLResourceContainer;
import framework.core.gui.userInterfaces.pluginRegistryGui.addRessource.AddResourceGuiService;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.PathsUtils;
import framework.core.gui.util.components.PopUpPrompt;
import framework.core.gui.util.visualUtilities.ImageLoader;
import framework.core.util.XMLResourceUtils;

/**
 * ResourceDescriptionManagementIOService is the service that creates and deletes the description
 * (...Settings.xml) files. Note that deletion doesn't include de-registration!
 * 
 * @author Marius Fink
 * @version 17.11.2012
 */
public class ResourceIOManagementService {

	/**
	 * Creates a new fescription file with the extracted structure and preset values of the
	 * correspronding possible values file. Note, that creation doesn't include registration!
	 * 
	 * @param type
	 *            the type of resource to create the description file for
	 * @return true, if creation was successful, false, else
	 */
	public boolean createNewDescriptor(ResourceType type) {

		String path = "";
		String xpathToRoot = "";
		switch (type) {
		case PLUGIN:
			path = Paths.PLUGIN_REGISTRY_GUI_DESCRIPTOR_XML_FILE_PATH;
			xpathToRoot = "/plugIn";
			break;
		case STATIC_FUNCTION:
			path = Paths.STATIC_FUNCTION_REGISTRY_GUI_DESCRIPTOR_XML_FILE_PATH;
			xpathToRoot = "/staticFunction";
			break;
		}

		AddResourceGuiService svc = new AddResourceGuiService(type);
		ResourceMinimumInformation lap = svc.askForMinimumResourceInformation();
		if (lap != null) {
			try {
				XMLDocumentCreator template = XMLResourceUtils
						.extractDefaultTemplateFromPossibleValues(new XMLResource(path));

				// Set path (/plugIn @home), layer (/plugin @layer), id (/plugIn @id)
				template.addAttribute(xpathToRoot, "home", lap.getHome());
				template.addAttribute(xpathToRoot, "layer", lap.getLayer());
				template.addAttribute(xpathToRoot, "id", lap.getId());

				template.saveAs(lap.getPath());
				return true;
			} catch (Exception ex) {
				PopUpPrompt.displayGenericErrorMessage(GUIText.getText("xmlCreationFailed"),
						GUIText.getText("xmlCreationFailedDetail"));
				ex.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Deletes a given descriptor by removing it permanentely from the filesystem. Note, that
	 * deletion doesn't include unregistration!
	 * 
	 * @param selectedItem
	 *            the item to delete
	 * @return true on success, false, else
	 */
	public boolean deleteResource(XMLResourceContainer selectedItem) {
		String append = "";
		switch (selectedItem.getType()) {
		case PLUGIN:
			append = GUIText.getText("plugInSettingsXml");
			break;
		case STATIC_FUNCTION:
			append = GUIText.getText("staticFunctionSettingsXml");
			break;
		}

		boolean yes = PopUpPrompt.displayGenericYesNoMessage(GUIText.getText("deleteResource"),
				GUIText.getText("deleteResourceDetail", selectedItem.getName(), selectedItem.getType().name()),
				ImageLoader.loadIcon("delete", 60));
		if (yes) {
			File f = new File(PathsUtils.toCurrentSystemPath(selectedItem.getHome()) + File.separator + append);
			if (!f.delete()) {
				PopUpPrompt.displayGenericErrorMessage(GUIText.getText("unableToDeleteFile"),
						GUIText.getText("unableToDeleteFileDetail", f.getAbsolutePath()));
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
