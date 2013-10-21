package framework.core.util;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLResource;
import framework.core.gui.services.XMLDocumentCreator;

/**
 * XMLResourceUtils is a helper class to convert XMLResources into XMLSettingsDocumentCreators and
 * the other way around. It is also able to extract a template from a possible values specification.
 * 
 * @author Marius Fink
 * @version 03.11.2012
 */
public class XMLResourceUtils {

	/**
	 * Converts to XMLSettingsDocumentCreator
	 * 
	 * @param resource
	 *            the resouce to convert
	 * @return the converted resouce
	 */
	public static XMLDocumentCreator convert(XMLResource resource) {
		XMLDocumentCreator c = new XMLDocumentCreator(resource.getRootNode());
		List<XMLPart> parts = resource.getAllXMLParts();

		for (XMLPart p : parts) {
			p.storeIn(c);
		}

		return c;
	}
	
	
	/**
	 * Converts to XMLSettingsDocumentCreator
	 * 
	 * @param resource
	 *            the resouce to convert
	 * @return the converted resouce
	 */
	/*public static XMLDocumentCreator convert(String rootNode, List<XMLPart> allXmlParts) {
		XMLDocumentCreator c = new XMLDocumentCreator(rootNode);
		//List<XMLPart> parts = resource.getAllXMLParts();

		for (XMLPart p : allXmlParts) {
			p.storeIn(c);
		}

		return c;
	}*/
	

	/**
	 * Converts to XMLResouce
	 * 
	 * @param creator
	 *            the creator to convert
	 * @return the converted creator
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static XMLResource convert(XMLDocumentCreator creator) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document d = builder.build(creator.getDocumentAsInputStream());
			return new XMLResource(d);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Converts to XMLResouce
	 * 
	 * @param creator
	 *            the creator to convert
	 * @return the converted creator
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Document creatorToDocument(XMLDocumentCreator creator) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document d = builder.build(creator.getDocumentAsInputStream());
			return d;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Convert something that follows the "possible values document pattern" into a template with
	 * only default values.
	 * 
	 * @param inputDocument
	 *            the possible values document
	 * @return a raw template with only preset values that matches the structure from the given
	 *         inputDocument
	 */
	public static XMLDocumentCreator extractDefaultTemplateFromPossibleValues(XMLResource inputDocument) {
		String rootnode = inputDocument.getPropertyAsString("/guiInfo/possibleGuiValues/rootNode");
		XMLDocumentCreator creator = new XMLDocumentCreator(rootnode);

		int numberOfMainBranches = inputDocument.numberOfElements("/guiInfo/possibleGuiValues/mainBranch");
		for (int m = 1; m <= numberOfMainBranches; m++) {
			int numberOfSettingsGroups = inputDocument.numberOfElements("/guiInfo/possibleGuiValues/mainBranch[" + m
					+ "]/settingsGroups");
			for (int gg = 1; gg <= numberOfSettingsGroups; gg++) {
				int numberOfSettingGroupElements = inputDocument
						.numberOfElements("/guiInfo/possibleGuiValues/mainBranch[" + m + "]/settingsGroups[" + gg
								+ "]/settingGroup");
				for (int sg = 1; sg <= numberOfSettingGroupElements; sg++) {
					String xpathString = "/guiInfo/possibleGuiValues/mainBranch[" + m + "]/settingsGroups[" + gg
							+ "]/settingGroup[" + sg + "]/settings/singleSetting";
					int numberOfSingleSettings = inputDocument.numberOfElements(xpathString);
					for (int ss = 1; ss <= numberOfSingleSettings; ss++) {
						String location = inputDocument.getPropertyAsString(xpathString + "[" + ss + "]/location");

						String preset = "";
						if (inputDocument.hasProperty(xpathString + "[" + ss + "]/content/*[@preset]")) {
							preset = inputDocument.getRawAttributeValue(xpathString + "[" + ss + "]/content/*[@preset]",
									"preset");
						}

						String locationType = inputDocument.getRawAttributeValue(xpathString + "[" + ss + "]/location",
								"type");
						if (locationType.startsWith("@")) {
							// attr
							String attrName = locationType.substring(1);
							creator.addAttribute(location, attrName, preset);
						} else {
							// contentnode
							creator.addValue(location, preset);
						}

					}

				}

				int numberOfMultipleSettingGroupElements = inputDocument
						.numberOfElements("/guiInfo/possibleGuiValues/mainBranch[" + m + "]/settingsGroups[" + gg
								+ "]/multipleSetting");
				for (int ms = 1; ms <= numberOfMultipleSettingGroupElements; ms++) {
					String xpathString = "/guiInfo/possibleGuiValues/mainBranch[" + m + "]/settingsGroups[" + gg
							+ "]/multipleSetting[" + ms + "]/settings/singleSetting";
					int numberOfSingleSettings = inputDocument.numberOfElements(xpathString);
					for (int ss = 1; ss <= numberOfSingleSettings; ss++) {
						String location = inputDocument.getPropertyAsString(xpathString + "[" + ss + "]/location")
								.replaceAll("%index", "1");

						String preset = "";
						if (inputDocument.hasProperty(xpathString + "[" + ss + "]/content/*[@preset]")) {
							preset = inputDocument.getRawAttributeValue(xpathString + "[" + ss + "]/content/*[@preset]",
									"preset");
						}

						String locationType = inputDocument.getRawAttributeValue(xpathString + "[" + ss + "]/location",
								"type");
						if (locationType.startsWith("@")) {
							// attr
							String attrName = locationType.substring(1);
							creator.addAttribute(location, attrName, preset);
						} else {
							// contentnode
							creator.addValue(location, preset);
						}

					}
				}
			}
		}

		return creator;
	}
}
