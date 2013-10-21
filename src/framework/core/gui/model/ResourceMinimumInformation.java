package framework.core.gui.model;

import framework.core.gui.util.PathsUtils;

/**
 * ResourceMinimumInformation is a small bean that represents a location of a Settings Document
 * 
 * @author Marius Fink
 * @version 06.11.2012
 */
public class ResourceMinimumInformation {

	private int layer;
	private String path;
	private String id;
	private ResourceType type;
	private String home;

	/**
	 * Constructor of LayerAndPath
	 * 
	 * @param layer
	 *            the layer for this ressource
	 * @param path
	 *            the path to the resource HOME directory
	 * @param resourceType
	 *            the type of this resource
	 * @param id
	 *            the unique id of this resource
	 * @param home
	 *            the path to the home dir of this resource
	 */
	public ResourceMinimumInformation(int layer, String path, ResourceType resourceType, String id,
			String home) {
		super();
		this.setId(id);
		this.layer = layer;
		this.path = path;
		this.type = resourceType;
		this.setHome(home);
	}

	/**
	 * @return the layer
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the type
	 */
	public ResourceType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ResourceType type) {
		this.type = type;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return type.toString() + "; " + layer + "; " + path;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the home
	 */
	public String getHome() {
		return home;
	}

	/**
	 * @param home
	 *            the home to set
	 */
	public void setHome(String home) {
		this.home = PathsUtils.toUnixPath(home);
	}

}
