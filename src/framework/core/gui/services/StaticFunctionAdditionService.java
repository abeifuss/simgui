package framework.core.gui.services;

import java.util.Collection;

import framework.core.config.Paths;
import framework.core.gui.model.XMLAttribute;
import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLResource;

public class StaticFunctionAdditionService {

	public static void addAvailableStaticFunctions(Collection<XMLPart> partsSoFar) {
		XMLResource available = new XMLResource(Paths.AVAILABLE_RESOURCES_XML_FILE_PATH, false);
		
		for (XMLPart p: available.getAllXMLParts()) {
			if (p.getLocation().startsWith("/availableResources/availableStaticFunctions/") && p instanceof XMLAttribute) {
				XMLAttribute pCon = (XMLAttribute) p;
				pCon.setLocation("/gMixConfiguration" + pCon.getLocation());
				
				partsSoFar.add(pCon);
			}
		}
	}
}
