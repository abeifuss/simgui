package framework.core.gui.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import framework.core.config.Paths;
import framework.core.gui.model.PlugInBean;
import framework.core.gui.model.StaticFunctionBean;
import framework.core.gui.model.XMLAttribute;
import framework.core.gui.model.XMLResource;
import framework.core.gui.model.XMLResourceContainer;
import framework.core.gui.util.GUIText;
import framework.core.util.XMLResourceUtils;

/**
 * AvailablePlugInsDAO is a converter of the in the availablePlugIns.xml registered PlugIns to a
 * list of beans
 * 
 * @author Marius Fink
 * @version 08.12.2012
 */
public class AvailableResourcesDAO {

	private static final String STATIC_FUNCTION_NODE = "/availableResources/availableStaticFunctions/staticFunction";
	private static final String PLUGIN_NODE = "/availableResources/availablePlugIns/plugIn";
	private XMLResource availableResources = new XMLResource(Paths.AVAILABLE_RESOURCES_XML_FILE_PATH, false);
	private static final Logger LOGGER = Logger.getLogger(AvailableResourcesDAO.class);

	/**
	 * Stores a plug-in setting by adding a node with foreign source attribute.
	 * 
	 * @param pathToSettings
	 *            the path to the settings document. Ensure that it is relative to make the config
	 *            portable.
	 * @throws IOException
	 *             in case of failure
	 */
	public void registerPlugIn(XMLResourceContainer plugin) throws IOException {
		assert !isRegistered(plugin, PLUGIN_NODE) : "Precondition violated: !isRegistered(path, xpathToNode)";

		int n = availableResources.numberOfElements(PLUGIN_NODE);
		n++;

		XMLAttribute resourceLocator = new XMLAttribute();
		resourceLocator.setAttributeName("source");
		resourceLocator.setLocation(PLUGIN_NODE + "[" + n + "]");
		resourceLocator.setValue(plugin.getHome() + "/" + GUIText.getText("plugInSettingsXml"));

		XMLAttribute idAttr = new XMLAttribute();
		idAttr.setAttributeName("id");
		idAttr.setLocation(PLUGIN_NODE + "[" + n + "]");
		idAttr.setValue(plugin.getId());

		XMLDocumentCreator c = XMLResourceUtils.convert(availableResources);
		resourceLocator.storeIn(c);
		idAttr.storeIn(c);
		c.saveAs(Paths.AVAILABLE_RESOURCES_XML_FILE_PATH);
	}

	/**
	 * Stores a static function setting by adding a node with foreign source attribute.
	 * 
	 * @param pathToSettings
	 *            the path to the settings document. Ensure that it is relative to make the config
	 *            portable.
	 * @throws IOException
	 *             in case of failure
	 */
	public void registerStaticFunction(XMLResourceContainer bean) throws IOException {
		assert !isRegistered(bean, STATIC_FUNCTION_NODE) : "Precondition violated: !isRegistered(path, xpathToNode)";

		int n = availableResources.numberOfElements(STATIC_FUNCTION_NODE);
		n++;

		XMLAttribute resourceLocator = new XMLAttribute();
		resourceLocator.setAttributeName("source");
		resourceLocator.setLocation(STATIC_FUNCTION_NODE + "[" + n + "]");
		resourceLocator.setValue(bean.getHome() + "/" + GUIText.getText("staticFunctionSettingsXml"));

		XMLAttribute idAttr = new XMLAttribute();
		idAttr.setAttributeName("id");
		idAttr.setLocation(STATIC_FUNCTION_NODE + "[" + n + "]");
		idAttr.setValue(bean.getId());

		XMLDocumentCreator c = XMLResourceUtils.convert(availableResources);
		resourceLocator.storeIn(c);
		idAttr.storeIn(c);
		c.saveAs(Paths.AVAILABLE_RESOURCES_XML_FILE_PATH);
	}

	/**
	 * Searches for the given plugin path in the availableResources.xml Document
	 * 
	 * @param pathToPlugInSettings
	 *            the path to search for
	 * @return true, if this path is already linked, false, else
	 */
	public boolean isPlugInAlreadyRegistered(XMLResourceContainer plugin) {
		return isRegistered(plugin, PLUGIN_NODE);
	}

	/**
	 * checks if a plugin-id exists in the document.
	 * 
	 * @param plugInId
	 *            the id to look for
	 * @return true, if this id is registered, false, else
	 */
	public boolean isPlugInAlreadyRegistered(String plugInId) {
		int numberOfReferences = availableResources
				.numberOfElements("/availableResources/availablePlugIns/plugIn[@id = '" + plugInId + "']");
		// return >0 ?
		return numberOfReferences > 0;
	}

	/**
	 * Searches for the given static function path in the availableResources.xml Document
	 * 
	 * @param pathToPlugInSettings
	 *            the path to search for
	 * @return true, if this path is already linked, false, else
	 */
	public boolean isStaticFunctionAlreadyRegistered(XMLResourceContainer bean) {
		return isRegistered(bean, STATIC_FUNCTION_NODE);
	}

	/**
	 * Tests, if a resource is registered
	 * 
	 * @param bean
	 *            the bean to search for in the available resources
	 * @param xpathToNode
	 *            the xpath to the node.
	 * @return true, if this resource is registered, false, else
	 */
	private boolean isRegistered(XMLResourceContainer bean, String xpathToNode) {
		// find nodes with xpath [ends-with(@source,'pathEnd')]
		int numberOfReferences = availableResources.numberOfElements(xpathToNode + "[@id = '" + bean.getId() + "']");

		// return >0 ?
		return numberOfReferences > 0;
	}

	/**
	 * Removes a resource by its id - be sure that the id resolves to only one element as ids should
	 * always do.
	 * 
	 * @param id
	 *            the unique id to the element.
	 * 
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void unregisterResourceById(String id) throws XPathExpressionException, SAXException, IOException,
			ParserConfigurationException, TransformerException {
		availableResources.removeNode("//*[@id='" + id + "']");

		XMLResourceUtils.convert(availableResources).saveAs(Paths.AVAILABLE_RESOURCES_XML_FILE_PATH);
	}

	/**
	 * @return a list of all PlugIns that are registered at the time this method is called.
	 */
	public List<PlugInBean> getAvailablePlugIns() {
		// XMLResource res = new XMLResource(Paths.AVAILABLE_RESOURCES_XML_FILE_PATH, false);

		List<PlugInBean> availablePlugIns = new LinkedList<PlugInBean>();

		for (int i = 1; i <= availableResources.numberOfElements("/availableResources/availablePlugIns/plugIn"); i++) {
			String path = availableResources.getRawAttributeValue("/availableResources/availablePlugIns/plugIn[" + i
					+ "]", "source");

			try {
				availablePlugIns.add(new PlugInBean(new XMLResource(path)));
			} catch (IllegalArgumentException e) {
				LOGGER.error("Could not load the available resource: " + path
						+ ". Did you delete it? Remove the corresponding line in the availableResources.xml-File.");
			}
		}

		return availablePlugIns;
	}

	public PlugInBean getPlugInById(String id) {
		assert isPlugInAlreadyRegistered(id) : "Precondition violated: isPlugInAlreadyRegistered(" + id + ")";
		
		for (int i = 1; i <= availableResources.numberOfElements("/availableResources/availablePlugIns/plugIn"); i++) {
			String currentId = availableResources.getRawAttributeValue("/availableResources/availablePlugIns/plugIn[" + i + "]", "id");
			if (currentId.equals(id)) {
				String path = availableResources.getRawAttributeValue("/availableResources/availablePlugIns/plugIn[" + i + "]", "source");
				return new PlugInBean(new XMLResource(path));
			}
		}
		return null;
	}

	/**
	 * @return a list of all StaticFunctions that are registered at the time this method is called.
	 */
	public List<StaticFunctionBean> getAvailableStaticFunctions() {
		XMLResource res = new XMLResource(Paths.AVAILABLE_RESOURCES_XML_FILE_PATH, false);

		List<StaticFunctionBean> availableStaticFunctions = new LinkedList<StaticFunctionBean>();

		for (int i = 1; i <= res.numberOfElements("/availableResources/availableStaticFunctions/staticFunction"); i++) {
			String path = res.getRawAttributeValue("/availableResources/availableStaticFunctions/staticFunction[" + i
					+ "]", "source");
			try {
				availableStaticFunctions.add(new StaticFunctionBean(new XMLResource(path)));
			} catch (IllegalArgumentException e) {
				LOGGER.error("Could not load the available resource: " + path
						+ ". Did you delete it? Remove the corresponding line in the availableResources.xml-File.");
			}
		}

		return availableStaticFunctions;
	}
}
