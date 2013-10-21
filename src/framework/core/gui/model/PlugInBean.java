package framework.core.gui.model;



/**
 * PlugIn is a container for a pluginSettings.xml descriptor file that extracts
 * the main information to make them accessible trough getter.
 * 
 * @author Marius Fink
 * @version 08.12.2012
 */
public class PlugInBean extends XMLResourceContainer {

	private PlugInType pluginType;

	/**
	 * Constructor of PlugIn extracts the main information and throws an
	 * exception if the file is not a valid plug-in descriptor.
	 * 
	 * @param descriptor
	 *            the PlugInSettings.xml resource of this plug-in
	 */
	public PlugInBean(XMLResource descriptor) {
		try {
			id = descriptor.getRawAttributeValue("/plugIn", "id");
			layer = Integer.parseInt(descriptor.getRawAttributeValue("/plugIn",
					"layer"));
			name = descriptor.getPropertyAsString("/plugIn/name");
			home = descriptor.getRawAttributeValue("/plugIn", "home");
			String typeString = descriptor.getRawAttributeValue("/plugIn",
					"type");
			if (typeString.equals("")) {
				pluginType = PlugInType.MIX;
			} else {
				pluginType = PlugInType.valueOf(typeString);
			}
			type = ResourceType.PLUGIN;
			resource = descriptor;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"This is not a valid PlugInDescriptor!");
		}
	}

	/**
	 * Constructor for extending classes (makes a copy of the given plugin
	 * without revalidating the resource.
	 * 
	 * @param toCopy
	 *            the plug-in to clone
	 */
	protected PlugInBean(PlugInBean toCopy) {
		id = toCopy.getId();
		layer = toCopy.getLayer();
		name = toCopy.getName();
		type = toCopy.getType();
		pluginType = toCopy.getPluginType();
		resource = toCopy.getResource();
		home = toCopy.getHome();
	}

	// @Override
	// public String toString() {
	// return String.format("%s: Name: %s, ID: %s, Layer: %d, Type: %s",
	// this.getClass().getSimpleName(), name, id,
	// layer, type.name());
	// }

	/**
	 * @return the type of this plugin (client/mix)
	 */
	public PlugInType getPluginType() {
		return pluginType;
	}

	/**
	 * @param pluginType
	 *            the type of this plugin (client/mix)
	 */
	public void setPluginType(PlugInType pluginType) {
		this.pluginType = pluginType;
	}
	
}
