package framework.core.gui.services;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLResource;
import framework.core.util.XMLResourceUtils;

/**
 * XmlGuiDescriptorService is a Service that creates a combined XML Resource from a given Path to a
 * plugin or staticfunction home directory. It expects at least the given File, the
 * AdditionalSettingsDescription.xml is optional.
 * 
 * @author Marius Fink
 * @version 03.11.2012
 */
public class XmlGuiDescriptorService {

	private static final String PATH_TO_FIND_ELEMENTS_TO_ADD = "/specificSettings";
	private static final String WHERE_TO_PUT_THOSE_ELEMENTS = "/guiInfo/possibleGuiValues/mainBranch";
	private int index;

	public XMLResource createCompleteGuiDescription(String pathToHomeDir, XMLResource possibleValues,
			String settingsFileName) {
		// 1 merge
		File f = new File(pathToHomeDir + "/AdditionalSettingsDescription.xml");
		XMLResource r = possibleValues;
		index = r.numberOfElements(WHERE_TO_PUT_THOSE_ELEMENTS) + 1;

		if (f.exists()) {
			XMLResource b = new XMLResource(f.getPath());
			r = storeAllIn(r, modifyPartsToMatchInto(r, b));
		}

		return r;
	}

	/**
	 * Clones every part and modifies it's location to match the new paths.
	 * 
	 * @param into
	 *            the resource, those parts have to fit into
	 * @param modify
	 *            the resource whose parts will be modified
	 * @return a list of path-modified and cloned XMLParts from the given Resource 'modify'
	 */
	private List<XMLPart> modifyPartsToMatchInto(XMLResource into, XMLResource modify) {
		Collection<XMLPart> filteredPartsToAdd = Collections2.filter(modify.getAllXMLParts(),
				new Predicate<XMLPart>() {
					@Override
					public boolean apply(XMLPart input) {
						return (input.getLocation().startsWith(PATH_TO_FIND_ELEMENTS_TO_ADD));
					}
				});

		List<XMLPart> results = new LinkedList<XMLPart>();
		for (XMLPart p : filteredPartsToAdd) {
			String location = p.getLocation().replaceFirst(PATH_TO_FIND_ELEMENTS_TO_ADD,
					WHERE_TO_PUT_THOSE_ELEMENTS + "[" + index + "]");

			XMLPart clonedPart = p.clone();
			clonedPart.setLocation(location);

			results.add(clonedPart);
		}

		return results;
	}

	/**
	 * Stores all parts in the given Resource by converting the resource into a creator first.
	 * 
	 * @param a
	 *            the resource, to insert those parts into a copy of it
	 * @param partsToAdd
	 *            the parts to insert into the given resource
	 * @return the copy of the resource a with the inserted parts
	 */
	private XMLResource storeAllIn(XMLResource a, List<XMLPart> partsToAdd) {
		XMLDocumentCreator creator = XMLResourceUtils.convert(a);

		for (XMLPart p : partsToAdd) {
			p.storeIn(creator);
		}

		return XMLResourceUtils.convert(creator);
	}
}
