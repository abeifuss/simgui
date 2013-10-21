package framework.core.gui.model;

/**
 * This is a filter bean
 * 
 * @author Marius Fink
 */
public class PlugInFilterModel {

	private String name;
	private String[] possibleValues;
	private String plugInLocation;

	/**
	 * Create a new Filter
	 * 
	 * @param name
	 *            the (short) name of this filter
	 * @param location
	 *            the (xpath) location of the value (has to start with "<code>plugin/...</code>"
	 * @param possibleValues
	 *            the value that has to match the selected possible value
	 */
	public PlugInFilterModel(String name, String location, String... possibleValues) {
		this.name = name;
		this.plugInLocation = location;
		this.possibleValues = possibleValues;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the possibleValues
	 */
	public String[] getPossibleValues() {
		return possibleValues;
	}

	/**
	 * @return the plugInLocation
	 */
	public String getPlugInLocation() {
		return plugInLocation;
	}
}
