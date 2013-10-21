package framework.core.gui.model;

/**
 * This is the super-bean for every static function or plug-in. Man, I have to
 * stop double implementing everything.
 * 
 * @author Marius Fink
 * 
 */
public class XMLResourceContainer implements Treeable {
	protected String id;
	protected String name;
	protected String home;
	protected int layer;
	protected XMLResource resource;
	protected ResourceType type;

	/**
	 * The display text
	 */
	public String toString() {
		return getId();
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
		this.home = home;
	}

	/**
	 * @return the layer
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}

	/**
	 * @return the resource
	 */
	public XMLResource getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(XMLResource resource) {
		this.resource = resource;
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
}