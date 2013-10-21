package framework.core.gui.model;

/**
 * This is a simple container for a StaticFunction providing minimal information
 * and the accessible xml resource.
 * 
 * @author Marius Fink
 * 
 */
public class StaticFunctionBean extends XMLResourceContainer {

	/**
	 * Creates a new Static Function by its settings description
	 * 
	 * @param resource
	 *            the StaticFunctionSettings.xml resource
	 */
	public StaticFunctionBean(XMLResource resource) {
		this.resource = resource;
		try {
			id = resource.getRawAttributeValue("/staticFunction", "id");
			layer = Integer.parseInt(resource.getRawAttributeValue(
					"/staticFunction", "layer"));
			name = resource.getPropertyAsString("/staticFunction/name");
			home = resource.getRawAttributeValue("/staticFunction", "home");
			type = ResourceType.STATIC_FUNCTION;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"This is no valid StaticFunction description!");
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof StaticFunctionBean) {
			return id.equals(((StaticFunctionBean)other).id);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}
