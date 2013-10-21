package framework.core.gui.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.jamesmurty.utils.XMLBuilder;

import framework.core.gui.model.XMLResource;

/**
 * XMLSettingsDocumentCreator is a container for different settings with corresponding XPath which
 * can be build and saved as an XML File.
 * 
 * @author Marius Fink
 * @version 15.08.2012
 */
public class XMLDocumentCreator {

	private static final Logger LOGGER = Logger.getLogger(XMLResource.class);
	private XMLBuilder builder;

	public XMLDocumentCreator(String rootNode) {
		BasicConfigurator.configure();
		try {
			this.builder = XMLBuilder.create(rootNode);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (FactoryConfigurationError e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sets a value object (i.e. the String representative <code>o.toString()</code>) in a given
	 * path as text content. This path has to be absolute and definite. This value is only valid in
	 * this instance. Call <code>saveAs(...)</code> to make all current settings of this instance
	 * persistent.
	 * 
	 * @param xpath
	 *            the path to the parent of the given value
	 * @param value
	 *            the value object on which <code>toString()</code> will be called and saved.
	 * @return true, if successful, false else
	 */
	public boolean addValue(String xpath, Object value) {
		assert xpath.matches("/[/\\[\\]a-zA-Z0-9]+[^/]") : "Precondition violated: xpath.matches(\"/[/\\[\\]a-zA-Z0-9]+[^/]\")";
		assert isPathAbsolute(xpath) : "Precondition violated: isPathAbsolute(xpath)";

		createNodeIfNotExists(xpath);

		try {
			builder.xpathFind(xpath).text(value.toString());
			return true;
		} catch (XPathExpressionException e) {
			LOGGER.error("The value \"" + value.toString() + "\" could not be written into \"" + xpath
					+ "\". Nested exception is: ", e);
			return false;
		}
	}

	/**
	 * Sets a value object (i.e. the String representative <code>o.toString()</code>) in a given
	 * path. This path has to be absolute and definite. This value is only valid in this instance.
	 * Call <code>saveAs(...)</code> to make all current settings of this instance persistent.
	 * 
	 * @param xPathToNode
	 *            the path to the desired node
	 * @param attrName
	 *            the name of the attribute
	 * @param value
	 *            the value object on which <code>toString()</code> will be called and as the
	 *            attribute value
	 * @return true, if successful, false else
	 */
	public boolean addAttribute(String xPathToNode, String attrName, Object value) {
		assert xPathToNode != null : "Precondition violated: xPathToNode != null";
		assert attrName != null : "Precondition violated: attrName != null";
		assert isPathAbsolute(xPathToNode) : "Precondition violated: isPathAbsolute(xPathToNode)";

		createNodeIfNotExists(xPathToNode);

		try {
			builder.xpathFind(xPathToNode).attr(attrName, value.toString());
			return true;
		} catch (XPathExpressionException e) {
			LOGGER.error("The value \"" + value.toString() + "\" could not be written into attribute \"" + attrName
					+ "\" in node \"" + xPathToNode + "\". Nested exception is: ", e);
			return false;
		}
	}

	/**
	 * Saves this document at the given path. Be careful! Other XMLSettings instances won't
	 * recognize those changes. Every existing file will be overwritten without warning.
	 * 
	 * @param path
	 *            the path at which this document will be saved
	 * @throws IOException
	 *             if file could not be writte for any reason or the output could not be generated.
	 */
	public void saveAs(String path) throws IOException {
		try {
			File file = new File(path);
			file.getParentFile().mkdirs();
			PrintWriter writer = new PrintWriter(new FileOutputStream(file));
			Properties outputProperties = new Properties();
			outputProperties.put(javax.xml.transform.OutputKeys.METHOD, "xml");
			outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
			outputProperties.put("{http://xml.apache.org/xslt}indent-amount", "2");
			builder.toWriter(writer, outputProperties);
			writer.close();
		} catch (Exception e) {
			throw new IOException("Could not save into: \"" + path + "\". Nested exception is: ", e);
		}
	}

	/**
	 * Determines if the given XPath-String is absolute regardless the context of this document.
	 * Rules: The path only consists of single <code>/</code>, the first character is <code>/</code>
	 * , the last character is not a <code>/</code>, terms in brackets <code>[</code>...
	 * <code>]</code> only contains of numbers and no functions xyz<code>(</code>...<code>)</code>
	 * 
	 * @param xpath
	 *            the xpath to check
	 * @return true, if this path is absolute, false else
	 */
	public boolean isPathAbsolute(String xpath) {
		xpath = xpath.trim();
		if (!xpath.startsWith("/"))
			return false;
		if (xpath.contains("//"))
			return false;
		if (xpath.endsWith("/"))
			return false;
		if (!hasOnlyNumbersInBrackets(xpath))
			return false;
		if (xpath.contains("(") || xpath.contains(")"))
			return false;

		return true;
	}

	/**
	 * Workaround for text node replacement. JamesMurty's XMLBuilder is unforunately only able to
	 * add text but to replace attributes.
	 * 
	 * @param replacement
	 *            the textnode with the new values
	 * @return true if replacement was without errors, false, else
	 */
	public boolean replaceTextContent(String xpath, String replacement) {
		try {
			Element element = builder.getDocument().getDocumentElement();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPathExpression xpathExp = xpathFactory.newXPath().compile(xpath);
			Node foundNode = (Node) xpathExp.evaluate(element, XPathConstants.NODE);
			if (foundNode == null || foundNode.getNodeType() != Node.ELEMENT_NODE) {
				throw new XPathExpressionException("XPath expression \"" + xpath
						+ "\" does not resolve to an Element in context " + element + ": " + foundNode);
			}
			Element newElement = (Element) foundNode;
			newElement.setTextContent(replacement);

			return true;
		} catch (DOMException e) {
			e.printStackTrace();
			return false;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Replaces an attribute value.
	 * 
	 * @param replacement
	 *            the new value
	 * @param attrName
	 *            the name of the attribute
	 * @return true if replacement was ok.
	 */
	public boolean replaceAttributeContent(String xpath, String attrName, String replacement) {
		try {
			builder.xpathFind(xpath).a(attrName, replacement);
			return true;
		} catch (XPathExpressionException e) {
			return false;
		}
	}

	/**
	 * A definite automaton to determine if there are non numeric chars between brackets.
	 * 
	 * @param xpath
	 *            the String to check
	 * @return true if there are only numbers in the brackets or the string has no brackets, false
	 *         else
	 */
	private boolean hasOnlyNumbersInBrackets(String xpath) {
		final byte in = 0;
		final byte out = 1;
		byte current = 1;

		for (int i = 0; i < xpath.length(); i++) {
			char c = xpath.charAt(i);
			if (c == '[') {
				current = in;
				continue;
			} else if (c == ']') {
				current = out;
				continue;
			}

			if (current == in && !"1234567890".contains(c + "")) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Creates the given node if it doesn't exist by iterating backwards through the xpath
	 * expression slices and testing if the slice exists. If it does it creates the remaining and
	 * skipped parts.
	 * 
	 * @param xpath
	 */
	public void createNodeIfNotExists(String xpath) {
		assert isPathAbsolute(xpath) : "Precondition violated: isPathAbsolute(xpath)";

		if (xpath.endsWith("/")) {
			xpath = xpath.substring(0, xpath.length() - 1);
		}
		if (xpath.startsWith("/")) {
			xpath = xpath.substring(1, xpath.length());
		}

		String[] nodes = xpath.split("/");

		XMLBuilder tempBuilder = builder;
		int lastFoundIndex = 0;
		for (int o = nodes.length - 1; o >= 0; o--) {
			String xpathSlice = "/" + StringUtils.join(Arrays.copyOfRange(nodes, 0, o + 1), "/");

			try {
				XMLBuilder b = builder.xpathFind(xpathSlice);
				tempBuilder = b;
				lastFoundIndex = o;
				break;
			} catch (XPathExpressionException e1) {
				// continuing until node is found
			}
		}

		for (int i = (lastFoundIndex + 1); i < nodes.length; i++) {
			if (nodes[i].matches("[a-zA-Z]+[a-zA-Z0-9]*\\[[0-9]+\\]")) {
				tempBuilder = createSpecificEnumeratedNode(tempBuilder,
						"/" + StringUtils.join(Arrays.copyOfRange(nodes, 0, i), "/"), nodes[i]);
			} else {
				tempBuilder = tempBuilder.element(nodes[i]);
			}
		}
	}

	/**
	 * Prints the current document to the console
	 */
	public void printDocument() {
		Properties outputProperties = new Properties();
		outputProperties.put(javax.xml.transform.OutputKeys.METHOD, "xml");
		outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
		outputProperties.put("{http://xml.apache.org/xslt}indent-amount", "2");
		String s;
		try {
			s = builder.asString(outputProperties);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates an enumerated node, e.g. node[3] at the given path and fills the path with nodes of
	 * this type until there are enough nodes to add the node at the given position.
	 * 
	 * @param path
	 *            the path to the node, which is already created until the latest bit, which
	 *            contains an index in brackets (e.g. <code>4<code>).
	 * @param tempBuilder
	 *            the builder to add the elements to (will also be returned)
	 * @param node
	 *            the name of the node
	 * @return the builder pointing to the right node
	 * 
	 */
	private XMLBuilder createSpecificEnumeratedNode(XMLBuilder tempBuilder, String path, String node)
			throws IllegalArgumentException {
		String pureNodeName = node.substring(0, node.indexOf("["));
		XMLBuilder pointer = null;
		boolean found = false;
		while (!found) {
			try {
				// xpath found - this element exists
				builder.xpathFind(path + "/" + node);
				found = true;
			} catch (XPathExpressionException e1) {
				// xpath not found - create another element of his type
				pointer = tempBuilder.element(pureNodeName);
			}
		}
		return pointer;
	}

	/**
	 * @return this document as InputStream
	 */
	public InputSource getDocumentAsInputStream() {
		Properties outputProperties = new Properties();
		outputProperties.put(javax.xml.transform.OutputKeys.METHOD, "xml");
		outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
		outputProperties.put("{http://xml.apache.org/xslt}indent-amount", "2");
		String s;
		try {
			s = builder.asString(outputProperties);
			return new InputSource(new StringReader(s));
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
	}
}
