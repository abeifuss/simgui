package framework.core.gui.services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import framework.core.config.Paths;
import framework.core.gui.model.StaticFunctionBean;
import framework.core.gui.model.XMLAttribute;
import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLResource;

/**
 * This service is able to filter some items out of the PlugInSettingsXml-File.
 * 
 * @author Marius Fink
 * 
 */
public class PlugInSettingsFilterService {
	private Collection<XMLPart> filterableParts;

	/**
	 * Creates a new Filter.
	 * 
	 * @param toFilter
	 *            the parts that have to be filtered.
	 */
	public PlugInSettingsFilterService(Collection<XMLPart> toFilter) {
		this.filterableParts = toFilter;
	}

	/**
	 * Searches only for capabilities.
	 * 
	 * @return a list containing all the static function beans that are capabilities.
	 */
	public List<StaticFunctionBean> filterForCapabilities() {
		return filterFor("staticFunctionCapabilities");
	}

	/**
	 * Searches only for requirements.
	 * 
	 * @return a list containing all the static function beans that are requirements.
	 */
	public List<StaticFunctionBean> filterForRequirements() {
		return filterFor("staticFunctionRequirements");
	}

	private List<StaticFunctionBean> filterFor(String xPathInner) {
		XMLResource wholeDocument = new XMLResource(Paths.AVAILABLE_RESOURCES_XML_FILE_PATH, false);
		
		List<StaticFunctionBean> list = new LinkedList<StaticFunctionBean>();
		for (XMLPart p : filterableParts) {
			if (p.getLocation().endsWith("staticFunction") && p instanceof XMLAttribute) {
				// find the static function with this id!
				XMLAttribute pa = (XMLAttribute) p;
				String id = pa.getValue().toString();
				// create Static Function Bean
				String path = wholeDocument
						.getRawAttributeValue(
								"/availableResources/availableStaticFunctions/staticFunction[@id='" + id + "']",
								"source");
				list.add(new StaticFunctionBean(new XMLResource(path)));
			}
		}
		return list;
	}

	/**
	 * @return the filterableParts
	 */
	public Collection<XMLPart> getFilterableParts() {
		return filterableParts;
	}

	/**
	 * @param filterableParts
	 *            the filterableParts to set
	 */
	public void setFilterableParts(Collection<XMLPart> filterableParts) {
		this.filterableParts = filterableParts;
	}
}
