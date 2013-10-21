/**
 * 
 */
package framework.core.gui.model;

/**
 * This is an actual filter with a filterValue chosen by an user.
 * @author Marius Fink
 */
public class PlugInFilter {
	private final PlugInFilterModel model;
	private String filterValue;
	
	/**
	 * Create a new actual filter
	 * @param theModel the describing model
	 * @param filterValue the actual value
	 */
	public PlugInFilter(PlugInFilterModel theModel, String filterValue) {
		model = theModel;
		this.filterValue = filterValue;
	}

	/**
	 * @return the model
	 */
	public PlugInFilterModel getModel() {
		return model;
	}

	/**
	 * @return the filterValue
	 */
	public String getFilterValue() {
		return filterValue;
	}
}
