package framework.core.gui.model;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.airavata.samples.LevenshteinDistanceService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.xml.sax.SAXException;

import com.google.common.primitives.Ints;

import framework.core.config.Paths;
import framework.core.gui.services.AvailableResourcesDAO;
import framework.core.gui.services.XMLDocumentCreator;
import framework.core.gui.userInterfaces.toolSelectorGui.keyValuePopUp.KeyValuePair;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.PathsUtils;
import framework.core.util.XMLResourceUtils;


/**
 * This class provides a bunch of methods for accessing the global config (or other) xml files. The
 * best way to get the properties is a xpath query. This class replaces child nodes with the
 * attribute source, if its not "here".
 * 
 * @author Marius Fink
 * @version 21.07.2012
 */
public class XMLResource {
	
	protected Document document;
	protected HashMap<String, String> temporaryChanges = new HashMap<String, String>();
	protected String prefix = "";
	protected String tempPrefix = "";
	private List<KeyValuePair<String, String>> overwrite;
	
	private static final Logger LOGGER = Logger.getLogger(XMLResource.class);

	
	/**
	 * Constructor of XMLResource with a given document
	 * 
	 * @param d
	 *            the document
	 */
	public XMLResource(Document d) {
		document = d;
		replaceForeignNodes(document);
	}

	
	/**
	 * Constructor with path to config file. Nodes with attribute source not like "here" will be
	 * replaced. It only accepts valid XML ressources.
	 * 
	 * @param pathToPropertyFile
	 *            the path to the document to load
	 */
	public XMLResource(String pathToPropertyFile) {
		this(pathToPropertyFile, true, null);
	}

	
	/**
	 * Constructor with path to config file. Nodes with attribute source not like "here" will be
	 * replaced. It only accepts valid XML ressources.
	 * 
	 * @param pathToPropertyFile
	 *            the path to the document to load
	 */
	public XMLResource(String pathToPropertyFile, List<KeyValuePair<String, String>> overwrite) {
		this(pathToPropertyFile, true, overwrite);
	}
	
	
	/**
	 * Constructor of XMLResource with path to the xml file and node replacment on/off
	 * 
	 * @param pathToPropertyFile
	 *            the path to the xml-document to load
	 * @param replaceReferencedNodes
	 *            true, if nodes with attribute source not like "here" should be replaced, false
	 *            means no replacement
	 */
	public XMLResource(String pathToPropertyFile, boolean replaceReferencedNodes) {
		this(pathToPropertyFile, replaceReferencedNodes, null);
	}
	
		
	/**
	 * Constructor of XMLResource with path to the xml file and node replacment on/off
	 * 
	 * @param pathToPropertyFile
	 *            the path to the xml-document to load
	 * @param replaceReferencedNodes
	 *            true, if nodes with attribute source not like "here" should be replaced, false
	 *            means no replacement
	 */
	public XMLResource(String pathToPropertyFile, boolean replaceReferencedNodes, List<KeyValuePair<String, String>> overwrite) {
		this.overwrite = overwrite;
		this.document = createDocument(pathToPropertyFile, replaceReferencedNodes);
		if (overwrite != null && overwrite.size() != 0)
			overwrite(overwrite);
	}
	
	
	private static Document createDocument(String pathToPropertyFile, boolean replaceReferencedNodes) {
		Document result = null;
		BasicConfigurator.configure();

		SAXBuilder builder = new SAXBuilder();
		pathToPropertyFile = PathsUtils.toCurrentSystemPath(pathToPropertyFile);
		File xmlFile = new File(pathToPropertyFile);
		// builder.setValidation(true);

		try {
			LOGGER.info("Trying to parse '" + pathToPropertyFile + "'.");
			result = (Document) builder.build(xmlFile);
			LOGGER.info("Parsing of '" + pathToPropertyFile + "' successful.");

			if (replaceReferencedNodes) {
				replaceForeignNodes(result);
			}
			LOGGER.info("Success! The whole document builds fine.");

		} catch (JDOMException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("The file could not be parsed with JDOM. Nested Exception is: "
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			throw new IllegalArgumentException("The file could not be opened. Nested IO-Exception is: "
					+ e.getLocalizedMessage());
		}
		return result;
	}

	
	public void overwrite(List<KeyValuePair<String, String>> overwriteSet) {
		this.document = overwrite(this.document, this.prefix, this.tempPrefix, overwriteSet);
	}
	
	
	private static Document overwrite(Document document, String prefix, String tempPrefix, List<KeyValuePair<String, String>> overwriteSet) {
		if (overwriteSet != null && overwriteSet.size() != 0) {
			XMLDocumentCreator creator = convertDocumentToCreator(document, prefix, tempPrefix);
			for (KeyValuePair<String, String> pair: overwriteSet) {
				if (pair.getKey().matches(".*@[a-zA-Z0-9]+")) {
					//attr
					String location = pair.getKey().substring(0, pair.getKey().lastIndexOf("@"));
					String attributeName = pair.getKey().substring(pair.getKey().lastIndexOf("@"), pair.getKey().length());
					creator.replaceAttributeContent(location, attributeName, pair.getValue());
				} else {
					//text
					creator.replaceTextContent(pair.getKey(), pair.getValue());
				}
			}
			SAXBuilder builder = new SAXBuilder();
			try {
				document = builder.build(creator.getDocumentAsInputStream());
				replaceForeignNodes(document);
			} catch (JDOMException e) {
				throw new IllegalArgumentException("The file could not be parsed with JDOM. Nested Exception is: "+ e.getLocalizedMessage());
			} catch (IOException e) {
				throw new IllegalArgumentException("The file could not be opened. Nested IO-Exception is: "+ e.getLocalizedMessage());
			}
		}
		return document;
	}
	
	
	private static XMLDocumentCreator convertDocumentToCreator(Document document, String prefix, String tempPrefix) {
		List<XMLPart> xmlParts = getAllXMLParts(document, prefix, tempPrefix, "/" + document.getRootElement().getName());
		XMLDocumentCreator result = new XMLDocumentCreator(document.getRootElement().getName());
		for (XMLPart p : xmlParts)
			p.storeIn(result);
		return result;
	}
	
	
	private static void replaceForeignNodes(Document document) {
		try {
			// Build the WHOLE XML file
			XPathExpression<Element> xpath = XPathFactory.instance().compile("//*[@source!='here']", new ElementFilter());
			List<Element> results = xpath.evaluate(document);

			LOGGER.info("Found " + results.size() + " nodes with foreign source.");

			for (Element element : results) {
				String path = element.getAttributeValue("source");
				Element parent = (Element) element.getParent();
				int index = parent.indexOf((Content) element);
				LOGGER.info(" -> Trying to parse '" + path + "' for replacement with '" + element.getName() + "' (index "
						+ index + ").");
				SAXBuilder nestedBuilder = new SAXBuilder();
				File xmlReplacementFile = new File(path);
				if (!xmlReplacementFile.exists()) {
					throw new IOException("Can't find file: " + xmlReplacementFile.getAbsolutePath());
				}
				Document replacement = (Document) nestedBuilder.build(xmlReplacementFile);
				LOGGER.info(" -> Parsing of '" + path + "' successful.");
				parent.removeContent(index);
				parent.addContent(replacement.getRootElement().detach());
				LOGGER.info(" -> " + element.getName() + " successfully replaced.");
			}

		} catch (JDOMException e) {
			throw new IllegalArgumentException("The file could not be parsed with JDOM. Nested Exception is: " + e.getLocalizedMessage());
		} catch (IOException e) {
			throw new IllegalArgumentException("The file could not be opened. Nested IO-Exception is: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Prints this current xml-file
	 */
	public void printDocument() {
		System.out.println("Heads up! This output does not contain any temporary changes.");
		XMLOutputter xmlout = new XMLOutputter();
		try {
			xmlout.output(document, System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the root node for all future paths of this object. Use this method to avoid long and
	 * complicated paths.
	 * 
	 * @param prefix
	 *            the xquery-prefix for all future requests
	 */
	public void setRootNode(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * A temporary prefix addition between the current prefix and the variable XPath-String. This
	 * prefix can easyly be resettet.
	 * 
	 * @param tempPrefix
	 */
	public void setTemporaryPrefix(String tempPrefix) {
		this.tempPrefix = tempPrefix;
	}

	
	public String getTemporaryPrefix() {
		return this.tempPrefix;
	}
	
	
	/**
	 * Deletes the temporary prefix.
	 */
	public void resetTemporaryPrefix() {
		this.tempPrefix = "";
	}

	/**
	 * Searches with the simple xpath (which is described in the class comment) for a String.
	 * Returns the text value of the node, but only if it is on the lowest level. For a bunch of
	 * properties try <code>getAllStrings</code>.
	 * 
	 * @param xpathString
	 *            the simple xpath (which is described in the class comment) to the entry
	 * @return the found String or <code>Assertion Error</code> if nothing is found or it is not the
	 *         lowest node
	 */
	public String getPropertyAsString(String xpathString) {
		try {
			return getRawTextContent(xpathString);
		} catch (Exception e) {
			LOGGER.warn("Could not parse the content of: '" + xpathString + "', returning 'null'", e);
			return null;
		}
	}

	/**
	 * Puts the given value in a temporary map in this instance of XMLResource. It will be used
	 * before the original document will be considered.
	 * 
	 * @param xpath
	 *            the xpath to the element. (can (but should not) be fictional. Remember that
	 *            xpath@attr means the attribute with name "attr" under the XPath: "xpath"
	 * @param value
	 *            the value.
	 */
	public void addTemporaryValue(String xpath, String value) {
		temporaryChanges.put(prefix + tempPrefix + xpath, value);
	}

	private boolean propertyInTempChanges(String xpath) {
		return temporaryChanges.containsKey(prefix + tempPrefix + xpath);
	}

	/**
	 * @param xpath
	 *            the xpath to the element
	 * @return the raw String content or the temporary value (if exists)
	 * @throws JDOMException
	 */
	public String getRawTextContent(String xpath) throws JDOMException {
		assert xpath != null : "Precondition violated: xpath != null";

		if (propertyInTempChanges(xpath)) {
			return temporaryChanges.get(prefix + tempPrefix + xpath);
		} else {
			XPathExpression<Element> xpathExpression = XPathFactory.instance().compile(prefix + tempPrefix + xpath,
					new ElementFilter());

			assert hasProperty(xpath, true) : "Precondition violated: hasProperty(xpath)";
			assert isLowestNode(xpathExpression) : "Precondition violated: isLowestNode(xpath)";
			assert isDefiniteNode(xpathExpression) : "Precondition violated: isDefiniteNode(xpath)";

			Element result = (Element) xpathExpression.evaluateFirst(document);

			String resultString = null;
			if (result != null) {
				resultString = result.getTextTrim();
			}
			return resultString;
		}
	}

	/**
	 * Finds the nearest n xpaths to the given xpath by levenshtein distance.
	 * 
	 * @param xpath
	 *            the xpath
	 * @param n
	 *            the number of maximal returned elements
	 * @return a list of strings ordered by their resemblance to the given xpath.
	 */
	private List<String> getBestMatches(final String xpath, int n) {
		final LevenshteinDistanceService levenshtein = new LevenshteinDistanceService();

		List<String> result = new LinkedList<String>(temporaryChanges.keySet());
		for (XMLPart p : getAllXMLParts()) {
			result.add(p.getLocation());
		}
		Collections.sort(result, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				Integer o1Distance = levenshtein.computeDistance(o1, xpath);
				Integer o2Distance = levenshtein.computeDistance(o2, xpath);

				return o1Distance.compareTo(o2Distance);
			}
		});

		return result.subList(0, Math.min(result.size(), n));
	}

	/**
	 * @param xPathToElement
	 *            the Xpath-Expression to the desired element.
	 * @param nameOfAttribute
	 *            the name of the attribute
	 * @return the value of the given attribute identified by element and name as a String.
	 */
	public String getRawAttributeValue(String xpath, String attributeName) {
		assert xpath != null : "Precondition violated: xpath != null";

		if (propertyInTempChanges(xpath)) {
			return temporaryChanges.get(xpath + "@" + attributeName);
		} else {
			try {
				XPathExpression<Element> xPathExp = XPathFactory.instance().compile(prefix + tempPrefix + xpath,
						new ElementFilter());

				assert hasProperty(xpath, true) : "Precondition violated: hasProperty(" + xpath + "@" + attributeName + ")";
				assert isDefiniteNode(xPathExp) : "Precondition violated: isDefiniteNode(" + xpath + ")";

				Element result = xPathExp.evaluateFirst(document);

				return result.getAttributeValue(attributeName);
			} catch (JDOMException jdomE) {
				return null;
			}
		}
	}

	/**
	 * Searches with the simple xpath (which is described in the class comment) for a boolean.
	 * Returns the text value of the node, but only if it is on the lowest level. For a bunch of
	 * properties try <code>getPropertiesAsXXArray</code>.
	 * 
	 * @param xpathString
	 *            the simple xpath (which is described in the class comment) to the entry
	 * @return the found boolean which is <code>false</code> if nothing is found or it is not the
	 *         lowest node or the text content is anything else then "true" (case-insensitive)
	 */
	public boolean getPropertyAsBoolean(String xpathString) {
		try {
			return Boolean.parseBoolean(getRawTextContent(xpathString));
		} catch (JDOMException e) {
			LOGGER.warn("Could not parse the content of: '" + xpathString + "', returning 'false'", e);
			return false;
		}
	}

	/**
	 * Searches with the simple xpath (which is described in the class comment) for an int. Returns
	 * the parsed text value of the node, but only if it is on the lowest level. For a bunch of
	 * properties try <code>getPropertiesAsXXArray</code>.
	 * 
	 * @param xpathString
	 *            the simple xpath (which is described in the class comment) to the entry
	 * @return the found int which is <code>0</code> if nothing is found or it is not the lowest
	 *         node or the text content is anything inparsable
	 */
	public int getPropertyAsInt(String xpathString) {
		try {
			String textTrim = getRawTextContent(xpathString);
			try {
				return Integer.parseInt(textTrim);
			} catch (NumberFormatException e) {
				LOGGER.warn("Tried to parse " + textTrim + " - it appears to be no Integer. Returning 0.");
				return 0;
			}
		} catch (JDOMException e) {
			LOGGER.warn("Could not parse the content of: '" + xpathString + "', returning 0", e);
			return 0;
		}
	}

	/**
	 * TODO
	 * 
	 * @param xpathString
	 * @return
	 */
	public InetAddress getPropertyAsInetAddress(String xpathString) {
		try {
			try {
				return InetAddress.getByName(getRawTextContent(xpathString));
			} catch (NumberFormatException e) {
				LOGGER.warn("Tried to parse " + xpathString + " - it appears to be no InetAdress. Returning null.");
				return null;
			} catch (UnknownHostException e) {
				LOGGER.error("Tried to parse " + xpathString + " - it appears to be no InetAdress. Returning null.", e);
				return null;
			}
		} catch (JDOMException e) {
			LOGGER.warn("Could not parse the content of: '" + xpathString + "', returning 0", e);
			return null;
		}
	}

	/**
	 * Searches with the simple xpath (which is described in the class comment) for an float.
	 * Returns the parsed text value of the node, but only if it is on the lowest level and there is
	 * only a single node. For a bunch of properties try <code>getPropertiesAsXXArray</code>.
	 * 
	 * @param xpathString
	 *            the simple xpath (which is described in the class comment) to the entry
	 * @return the found float which is <code>0</code> if nothing is found or it is not the lowest
	 *         node or the text content is anything unparsable, remember the format of floats is:
	 *         "-123.456"
	 */
	public float getPropertyAsFloat(String xpathString) {
		try {
			String textTrim = getRawTextContent(xpathString);
			try {
				return Float.parseFloat(textTrim);
			} catch (NumberFormatException e) {
				LOGGER.warn("Tried to parse " + textTrim + " - it appears to be no Float, returning 0f");
				return 0f;
			}
		} catch (JDOMException e) {
			LOGGER.warn("Could not parse the content of: '" + xpathString + "', returning 0f", e);
			return 0f;
		}
	}

	public long getPropertyAsLong(String xpathString) {
		try {
			String textTrim = getRawTextContent(xpathString);
			try {
				return Long.parseLong(textTrim);
			} catch (NumberFormatException e) {
				LOGGER.warn("Tried to parse " + textTrim + " - it appears to be no long, returning 0");
				return 0l;
			}
		} catch (JDOMException e) {
			LOGGER.warn("Could not parse the content of: '" + xpathString + "', returning 0", e);
			return 0l;
		}
	}

	private boolean isLowestNode(XPathExpression<Element> xpath) throws JDOMException {
		Element result = (Element) xpath.evaluateFirst(document);
		return (result.getChildren().size() == 0);
	}

	private boolean isDefiniteNode(XPathExpression<Element> xpath) throws JDOMException {
		List<?> results = xpath.evaluate(document);
		return (results.size() == 1);
	}

	/**
	 * Detects whether the key is given in the file - this does not detect whether the xpath leads
	 * to a distinct single node (leaf)
	 * 
	 * @param xpathString
	 *            the xpath to search for
	 * @return true if key exists, false else
	 */
	public boolean hasProperty(String xpathString) {
		return hasProperty(xpathString, false);
	}
	
	/**
	 * Detects whether the key is given in the file - this does not detect whether the xpath leads
	 * to a distinct single node (leaf)
	 * 
	 * @param xpathString
	 *            the xpath to search for
	 * @return true if key exists, false else
	 */
	public boolean hasProperty(String xpathString, boolean suggestions) {
		try {
			XPathExpression<Element> xpath = XPathFactory.instance().compile(prefix + tempPrefix + xpathString,
					new ElementFilter());
			
			boolean hasProperty = (!xpath.evaluate(document).isEmpty()) || propertyInTempChanges(prefix + tempPrefix + xpathString);
			
			if (!hasProperty && suggestions) {
				StringBuilder errorString = new StringBuilder("Did not find the property: " + prefix + tempPrefix + xpathString + "\n");
				errorString.append("  Maybe you mean one of these:\n");
				for (String possibleMatch : getBestMatches(prefix + tempPrefix + xpathString, 5)) {
					errorString.append("    - ");
					errorString.append(possibleMatch);
					errorString.append("\n");
				}
				LOGGER.error(errorString);
			}
			return hasProperty;
		} catch (Exception e) {
			LOGGER.warn("Could not parse the content of: '" + xpathString + "'", e);
			return false;
		}
	}

	/**
	 * Fetches the raw String splits it by , (comma) and returns the elements parsed to int in an
	 * array.
	 * 
	 * @param xpath
	 *            the xpath to the element
	 * @return the elements parsed to int in an array.
	 */
	public int[] getPropertyAsIntArray(String xpath) {
		try {
			String textTrim = getRawTextContent(xpath);

			List<Integer> listOfInts = new LinkedList<Integer>();
			for (String s : textTrim.split(",")) {
				try {
					listOfInts.add(Integer.parseInt(textTrim));
				} catch (NumberFormatException e) {
					LOGGER.warn("Tried to parse " + textTrim + " (element: " + s
							+ "- it appears to be no int, skipping this one.");
				}
			}
			return Ints.toArray(listOfInts);
		} catch (JDOMException e) {
			LOGGER.warn("Could not parse the content of: '" + xpath + "', returning 0f", e);
			return new int[0];
		}
	}

	/**
	 * Evaluates the given xpath and returns all text contents of leaf elements in an Array of
	 * Strings. Example: //globalCapabilites/duplexCapability[@type='client'] can return {"true",
	 * "false"}
	 * 
	 * @param xpathString
	 *            the xPath to evaluate
	 * @return the found integers
	 */
	public String[] getSameLayerLeafsAsStringArray(String xpathString) {
		assert hasProperty(xpathString, true) : "Precondition violated: hasProperty(xpathString)";

		try {
			XPathExpression<Element> xpath = XPathFactory.instance().compile(prefix + tempPrefix + xpathString,
					new ElementFilter());
			List<Element> results = xpath.evaluate(document);

			List<String> properties = new ArrayList<String>();
			for (Element element : results) {
				String text = element.getText().trim();
				properties.add(text);
			}

			return properties.toArray(new String[0]);
		} catch (Exception e) {
			LOGGER.warn("Could not parse the content of: '" + xpathString + "'", e);
			throw new RuntimeException("could not parse " + prefix + tempPrefix + xpathString
					+ " from property file; nested exception is: " + e.getMessage());
		}
	}

	// TODO float, boolean, String arrays (not used until 22.02.2013)

	/**
	 * @param xPathString
	 *            the xpath to execute
	 * @return the number of the matching elements
	 */
	public static int numberOfElements(Document document, String prefix, String tempPrefix, String xpathString) {
		try {
			XPathExpression<Element> xpath = XPathFactory.instance().compile(prefix + tempPrefix + xpathString,
					new ElementFilter());
			List<Element> results = xpath.evaluate(document);
			return results.size();
		} catch (Exception e) {
			LOGGER.warn("Could not parse the content of: '" + prefix + tempPrefix + xpathString + "'", e);
			return 0;
		}
	}

	
	/**
	 * @param xPathString
	 *            the xpath to execute
	 * @return the number of the matching elements
	 */
	public int numberOfElements(String xpathString) {
		return numberOfElements(document, prefix, tempPrefix, xpathString);
	}
	
	
	/**
	 * Gets the element type of a node (if it is a node) - e.g. "name" on "&lt;name ...&gt;"
	 * 
	 * @param xPathString
	 *            the xpath to evaluate
	 * @return the name of the element
	 */
	public String getElementName(String xpathString) {
		assert hasProperty(xpathString, true) : "Precondition violated: hasProperty(" + xpathString + ")";

		try {
			XPathExpression<Element> xpath = XPathFactory.instance().compile(prefix + tempPrefix + xpathString,
					new ElementFilter());
			Element result = xpath.evaluateFirst(document);

			return result.getName().trim();
		} catch (Exception e) {
			LOGGER.warn("Could not get the element: '" + prefix + tempPrefix + xpathString + "'", e);
			throw new RuntimeException("Could not get the element: " + prefix + tempPrefix + xpathString
					+ " from property file; nested exception is: " + e.getMessage());
		}
	}

	/**
	 * Determines if the given element is the root element to proove if this is the right document.
	 * 
	 * @param rootElement
	 *            the name of the root element
	 * @return true, if this document matches the given root element, false, else
	 */
	public boolean isDocumentRoot(String rootElement) {
		try {
			return (document.getRootElement().getName().equals(rootElement));
		} catch (Exception e) {
			LOGGER.warn("Didn't find root value. Nested exception is: ", e);
			return false;
		}
	}

	
	/**
	 * 
	 * @param xpathString
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 * @throws TransformerException
	 */
	public void removeNode(String xpathString) throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException, TransformerException {
		XPathExpression<Element> xpath = XPathFactory.instance().compile(xpathString, new ElementFilter());
		List<Element> results = xpath.evaluate(document);
		if (results.size() != 1) {
			throw new IllegalArgumentException("Xpath does not resolve to a single element.");
		}

		results.get(0).getParent().removeContent(results.get(0));
	}

	
	/**
	 * Retrieves the complete Path Value Map
	 * 
	 * @return a List of XML Parts containing the paths as key and the corresponding values
	 */
	public List<XMLPart> getAllXMLParts() {
		return fillPathMapWithDescendantsFrom(document, prefix, tempPrefix, "/" + document.getRootElement().getName(), new LinkedList<XMLPart>());
	}

	private static List<XMLPart> getAllXMLParts(Document document, String prefix, String tempPrefix, String xpathString) {
		return fillPathMapWithDescendantsFrom(document, prefix, tempPrefix, "/" + document.getRootElement().getName(), new LinkedList<XMLPart>());
	}
	
	/*private static List<XMLPart> getAllXMLParts(Document document, String prefix, String tempPrefix, String xpathString, List<XMLPart> list) {
		return fillPathMapWithDescendantsFrom(document, prefix, tempPrefix, "/" + document.getRootElement().getName(), list);
	}*/
	
	//private static List<XMLPart> getAllXMLParts(Document document, String prefix, String tempPrefix, String xpathString, List<XMLPart> list) {
	//	fillPathMapWithDescendantsFrom(document, prefix, tempPrefix, "/" + document.getRootElement().getName(), list);
	//	return list;
	//}
	//Document document, String prefix, String tempPrefix, String xpathString, List<XMLPart> list
	
	
	/**
	 * Fills the given list of all the descendands of the given xpath-value. Initialize this with
	 * "/[rootnode]" to traverse the whole document tree.
	 * 
	 * @param xpathString
	 *            the xpath to search and add its descendants
	 * @param list
	 *            the list to fill
	 */
	private static List<XMLPart> fillPathMapWithDescendantsFrom(Document document, String prefix, String tempPrefix, String xpathString, List<XMLPart> list) {
		XPathExpression<Element> xpath = XPathFactory.instance().compile(xpathString, new ElementFilter());
		List<Element> results = xpath.evaluate(document);
		for (Element e : results) {
			List<Element> children = e.getChildren();
			Map<String, Integer> childCount = new HashMap<String, Integer>();
			for (Element child : children) {
				int noOfElement = 1;
				if (childCount.containsKey(child.getName())) {
					noOfElement = ((int) childCount.get(child.getName())) + 1;
				}
				childCount.put(child.getName(), noOfElement);

				if (numberOfElements(document, prefix, tempPrefix, xpathString + "/" + child.getName()) == 1) {
					fillPathMapWithDescendantsFrom(document, prefix, tempPrefix, xpathString + "/" + child.getName(), list);
				} else {
					fillPathMapWithDescendantsFrom(document, prefix, tempPrefix, xpathString + "/" + child.getName() + "[" + noOfElement + "]", list);
				}
			}
			for (Attribute a : e.getAttributes()) {
				XMLAttribute xmlAttribute = new XMLAttribute();
				xmlAttribute.setAttributeName(a.getName());
				xmlAttribute.setLocation(xpathString);
				xmlAttribute.setValue(a.getValue());

				list.add(xmlAttribute);
			}
			String textContent = e.getTextTrim();
			if (!textContent.matches(" *")) {
				XMLTextNode textNode = new XMLTextNode();
				textNode.setLocation(xpathString);
				textNode.setValue(textContent);

				list.add(textNode);
			}
		}
		return list;
	}

	/**
	 * @return the concatenated prefix and temporary prefix
	 */
	public String getCurrentPrefix() {
		return prefix + tempPrefix;
	}

	/**
	 * @return the root node name
	 */
	public String getRootNode() {
		return document.getRootElement().getName();
	}
	
	
	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}
	
	
	/**
	 * Changes a plug-in in an existing xml-configuration. The exchange of plug-ins is complicated
	 * due to the distributed plugInSettings.xml-files.
	 * 
	 * @param settings the XMLResource to change.
	 * @param plugInId the id of the new plug-in
	 * @param layer the layer of the plug-in
	 * @param type the type of the plug-in (CLIENT/MIX)
	 */
	public void setPlugIn(String plugInId, int layer, PlugInType type) {
		// check if in availableResources.xml
		AvailableResourcesDAO dao = new AvailableResourcesDAO();
		if (!dao.isPlugInAlreadyRegistered(plugInId)) {
			throw new IllegalArgumentException("PlugIn is not registered in " + Paths.AVAILABLE_RESOURCES_XML_FILE_PATH);
		}
		
		String xpathBase = "/gMixConfiguration/composition/layer" + layer + "/" + type.name().toLowerCase() + "/plugIn";
		XMLDocumentCreator creator = convertDocumentToCreator(this.document, this.prefix, this.tempPrefix);
		// set id in xml
		creator.addAttribute(xpathBase, "id", plugInId);
		creator.addAttribute(xpathBase, "id", plugInId);
		// set source in xml
		creator.addAttribute(xpathBase, "source", dao.getPlugInById(plugInId).getHome() + "/" + GUIText.getText("plugInSettingsXml"));
		this.document = XMLResourceUtils.creatorToDocument(creator);
		replaceForeignNodes(document);
		if (overwrite != null && overwrite.size() != 0)
			overwrite(overwrite);
	}
	
	
	
	public XMLResource clone() {
		Document docClone = this.document.clone();
		XMLResource clone = new XMLResource(docClone); // TODO: ggf ohne load sub-shit (boolean)
		clone.prefix = new String(this.prefix);
		clone.tempPrefix = new String(this.tempPrefix);
		if (this.temporaryChanges.size() > 0)
			for (String key: this.temporaryChanges.keySet())
				clone.temporaryChanges.put(key, new String(this.temporaryChanges.get(key)));
		return clone;
	}
	
	/*public void overwirte(XMLResource xmlResource) {
		this.document = xmlResource.document;
		this.temporaryChanges = xmlResource.temporaryChanges;
		this.prefix = xmlResource.prefix;
		this.tempPrefix = xmlResource.tempPrefix;
	}*/
	
	
	public static void main(String[] args) {
		new XMLResource(Paths.GENERAL_CONFIG_XML_FILE_PATH).printDocument();
	}

}